package de.niklas.legendaryarcanesurvival.config;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ArcaneSurvivalConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue ENABLED;
    public static final ModConfigSpec.BooleanValue IGNORE_CREATIVE;
    public static final ModConfigSpec.DoubleValue BASE_EXHAUSTION;
    public static final ModConfigSpec.DoubleValue MANA_EXHAUSTION;
    public static final ModConfigSpec.DoubleValue TIER_EXHAUSTION;
    public static final ModConfigSpec.DoubleValue CAST_DURATION_EXHAUSTION;
    public static final ModConfigSpec.DoubleValue MAX_EXHAUSTION;
    public static final ModConfigSpec.DoubleValue MAX_TEMPERATURE_CHANGE;
    public static final ModConfigSpec.IntValue MINIMUM_CAST_INTERVAL;
    public static final ModConfigSpec.BooleanValue TARGET_EFFECTS;
    public static final ModConfigSpec.DoubleValue TARGET_TEMPERATURE_MULTIPLIER;
    public static final ModConfigSpec.IntValue TARGET_EFFECT_COOLDOWN;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SCHOOL_TEMPERATURE_EFFECTS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SCHOOL_WETNESS_EFFECTS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("general");
        ENABLED = builder
                .comment("Master switch for all survival effects from magic.")
                .define("enabled", true);
        IGNORE_CREATIVE = builder
                .comment("Do not apply spell survival effects to creative or spectator players.")
                .define("ignoreCreativeAndSpectator", true);
        MINIMUM_CAST_INTERVAL = builder
                .comment("Prevents the same spell event from charging twice in a very short interval.")
                .defineInRange("minimumCastIntervalTicks", 2, 0, 100);
        builder.pop();

        builder.push("caster");
        BASE_EXHAUSTION = builder
                .comment("Base Legendary Survival Overhaul thirst exhaustion for every active spell.")
                .defineInRange("baseThirstExhaustion", 0.35, 0.0, 20.0);
        MANA_EXHAUSTION = builder
                .comment("Additional thirst exhaustion per Iron's Spells mana point.")
                .defineInRange("thirstExhaustionPerMana", 0.0025, 0.0, 1.0);
        TIER_EXHAUSTION = builder
                .comment("Additional thirst exhaustion per spell level or Spell Engine tier.")
                .defineInRange("thirstExhaustionPerTier", 0.04, 0.0, 5.0);
        CAST_DURATION_EXHAUSTION = builder
                .comment("Additional thirst exhaustion per second of Spell Engine cast duration.")
                .defineInRange("thirstExhaustionPerCastSecond", 0.04, 0.0, 5.0);
        MAX_EXHAUSTION = builder
                .comment("Maximum thirst exhaustion charged by a single spell.")
                .defineInRange("maximumThirstExhaustionPerCast", 1.25, 0.0, 20.0);
        MAX_TEMPERATURE_CHANGE = builder
                .comment("Maximum absolute body-temperature change from one spell cast.")
                .defineInRange("maximumTemperatureChangePerCast", 0.60, 0.0, 10.0);
        builder.pop();

        builder.push("targets");
        TARGET_EFFECTS = builder
                .comment("Elemental spells can warm, cool, wet, or dry player targets.")
                .define("enabled", true);
        TARGET_TEMPERATURE_MULTIPLIER = builder
                .comment("Multiplier applied to the caster temperature value when affecting a target.")
                .defineInRange("temperatureMultiplier", 0.50, 0.0, 10.0);
        TARGET_EFFECT_COOLDOWN = builder
                .comment("Minimum delay between elemental target effects on one player.")
                .defineInRange("cooldownTicks", 10, 0, 1200);
        builder.pop();

        builder.push("schools");
        SCHOOL_TEMPERATURE_EFFECTS = builder
                .comment("Body-temperature changes in school_id=value form.",
                        "Positive values warm the player; negative values cool the player.")
                .defineList("temperatureEffects", List.of(
                        "irons_spellbooks:fire=0.35",
                        "irons_spellbooks:ice=-0.35",
                        "irons_spellbooks:lightning=0.10",
                        "spell_power:fire=0.30",
                        "spell_power:frost=-0.30",
                        "spell_power:water=-0.25",
                        "spell_power:air=-0.12",
                        "spell_power:lightning=0.08"
                ), ArcaneSurvivalConfig::isEffectEntry);
        SCHOOL_WETNESS_EFFECTS = builder
                .comment("Target wetness changes in school_id=value form.",
                        "Positive values make targets wetter; negative values dry them.")
                .defineList("wetnessEffects", List.of(
                        "irons_spellbooks:fire=-18",
                        "irons_spellbooks:ice=6",
                        "spell_power:fire=-18",
                        "spell_power:frost=8",
                        "spell_power:water=24"
                ), ArcaneSurvivalConfig::isEffectEntry);
        builder.pop();

        SPEC = builder.build();
    }

    private ArcaneSurvivalConfig() {
    }

    public static double temperatureEffect(ResourceLocation schoolId) {
        return parseEffects(SCHOOL_TEMPERATURE_EFFECTS.get()).getOrDefault(schoolId, 0.0);
    }

    public static int wetnessEffect(ResourceLocation schoolId) {
        return (int) Math.round(parseEffects(SCHOOL_WETNESS_EFFECTS.get()).getOrDefault(schoolId, 0.0));
    }

    private static Map<ResourceLocation, Double> parseEffects(List<? extends String> entries) {
        Map<ResourceLocation, Double> effects = new HashMap<>();
        for (String entry : entries) {
            String[] parts = entry.split("=", 2);
            ResourceLocation id = ResourceLocation.tryParse(parts[0].trim());
            if (id != null) {
                effects.put(id, Double.parseDouble(parts[1].trim()));
            }
        }
        return effects;
    }

    private static boolean isEffectEntry(Object value) {
        if (!(value instanceof String entry)) {
            return false;
        }
        String[] parts = entry.split("=", 2);
        if (parts.length != 2 || ResourceLocation.tryParse(parts[0].trim()) == null) {
            return false;
        }
        try {
            return Double.isFinite(Double.parseDouble(parts[1].trim()));
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
