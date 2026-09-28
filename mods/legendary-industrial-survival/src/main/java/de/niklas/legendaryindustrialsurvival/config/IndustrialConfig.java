package de.niklas.legendaryindustrialsurvival.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class IndustrialConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.IntValue LEVEL_ONE_RPM;
    public static final ModConfigSpec.IntValue LEVEL_TWO_RPM;
    public static final ModConfigSpec.IntValue LEVEL_THREE_RPM;
    public static final ModConfigSpec.DoubleValue STRESS_IMPACT;
    public static final ModConfigSpec.IntValue BASIC_DRINK_THRESHOLD;
    public static final ModConfigSpec.IntValue ADVANCED_DRINK_THRESHOLD;
    public static final ModConfigSpec.LongValue THERMOREGULATOR_ENERGY;
    public static final ModConfigSpec.DoubleValue THERMOREGULATOR_STEP;
    public static final ModConfigSpec.LongValue HYDRATION_ENERGY;
    public static final ModConfigSpec.IntValue HYDRATION_THRESHOLD;
    public static final ModConfigSpec.LongValue MEDICAL_ENERGY;
    public static final ModConfigSpec.IntValue MEDICAL_INTERVAL;
    public static final ModConfigSpec.DoubleValue MEDICAL_HEALING;
    public static final ModConfigSpec.DoubleValue SPELL_LIMB_HEAL_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue SPELL_LIMB_HEAL_CAP;
    public static final ModConfigSpec.IntValue HEALTH_SYNC_INTERVAL;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("kineticClimateMachines");
        LEVEL_ONE_RPM = builder
                .comment("Minimum Create RPM for heating or cooling level 1.")
                .defineInRange("levelOneRpm", 32, 1, 256);
        LEVEL_TWO_RPM = builder
                .comment("Minimum Create RPM for heating or cooling level 2.")
                .defineInRange("levelTwoRpm", 96, 1, 256);
        LEVEL_THREE_RPM = builder
                .comment("Minimum Create RPM for heating or cooling level 3.")
                .defineInRange("levelThreeRpm", 192, 1, 256);
        STRESS_IMPACT = builder
                .comment("Create stress impact of each kinetic climate machine.")
                .defineInRange("stressImpact", 16.0, 0.0, 4096.0);
        builder.pop();

        builder.push("backpackDrinkUpgrades");
        BASIC_DRINK_THRESHOLD = builder
                .comment("The basic Drink Upgrade starts drinking at or below this hydration level.")
                .defineInRange("basicThreshold", 10, 0, 19);
        ADVANCED_DRINK_THRESHOLD = builder
                .comment("The Advanced Drink Upgrade starts drinking at or below this hydration level.")
                .defineInRange("advancedThreshold", 16, 0, 19);
        builder.pop();

        builder.push("mekaSuitModules");
        THERMOREGULATOR_ENERGY = builder
                .comment("FE used by the Thermoregulator Unit per temperature correction.")
                .defineInRange("thermoregulatorEnergy", 5000L, 0L, Long.MAX_VALUE);
        THERMOREGULATOR_STEP = builder
                .comment("Maximum body-temperature correction every second.")
                .defineInRange("thermoregulatorStep", 0.35, 0.0, 100.0);
        HYDRATION_ENERGY = builder
                .comment("FE used by the Hydration Unit for each half-drop restored.")
                .defineInRange("hydrationEnergy", 7500L, 0L, Long.MAX_VALUE);
        HYDRATION_THRESHOLD = builder
                .comment("The Hydration Unit restores hydration at or below this level.")
                .defineInRange("hydrationThreshold", 16, 0, 19);
        MEDICAL_ENERGY = builder
                .comment("FE used by the Medical Treatment Unit per treatment.")
                .defineInRange("medicalEnergy", 25000L, 0L, Long.MAX_VALUE);
        MEDICAL_INTERVAL = builder
                .comment("Ticks between automatic Medical Treatment Unit treatments.")
                .defineInRange("medicalInterval", 100, 1, 72000);
        MEDICAL_HEALING = builder
                .comment("Immediate limb health restored per Medical Treatment Unit treatment.")
                .defineInRange("medicalHealing", 1.0, 0.0, 100.0);
        builder.pop();

        builder.push("healthCompatibility");
        SPELL_LIMB_HEAL_MULTIPLIER = builder
                .comment("Fraction of Iron's spell healing also applied to the most injured LSO limb.")
                .defineInRange("spellLimbHealMultiplier", 0.5, 0.0, 10.0);
        SPELL_LIMB_HEAL_CAP = builder
                .comment("Maximum limb healing applied by a single Iron's SpellHealEvent.")
                .defineInRange("spellLimbHealCap", 6.0, 0.0, 1000.0);
        HEALTH_SYNC_INTERVAL = builder
                .comment("Ticks between safety resynchronizations of external max-health modifiers with LSO.")
                .defineInRange("healthSyncInterval", 100, 20, 72000);
        builder.pop();
        SPEC = builder.build();
    }

    private IndustrialConfig() {
    }

    public static int levelForSpeed(float speed) {
        float absolute = Math.abs(speed);
        if (absolute >= LEVEL_THREE_RPM.get()) {
            return 3;
        }
        if (absolute >= LEVEL_TWO_RPM.get()) {
            return 2;
        }
        if (absolute >= LEVEL_ONE_RPM.get()) {
            return 1;
        }
        return 0;
    }
}
