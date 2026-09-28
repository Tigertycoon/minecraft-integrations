package de.niklas.legendaryarcanesurvival.service;

import de.niklas.legendaryarcanesurvival.config.ArcaneSurvivalConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.api.wetness.WetnessUtil;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

public final class SurvivalEffects {
    private static final SurvivalCooldowns COOLDOWNS = new SurvivalCooldowns();

    private SurvivalEffects() {
    }

    public static void applyCast(ServerPlayer player, String spellId, ResourceLocation schoolId,
                                 double exhaustion, double temperatureScale) {
        if (!canAffect(player) || !COOLDOWNS.allowCast(player.getUUID(), spellId,
                player.serverLevel().getGameTime(), ArcaneSurvivalConfig.MINIMUM_CAST_INTERVAL.get())) {
            return;
        }

        if (ThirstUtil.isThirstActive(player)) {
            float clampedExhaustion = (float) Math.min(
                    ArcaneSurvivalConfig.MAX_EXHAUSTION.get(),
                    Math.max(0.0, exhaustion));
            if (clampedExhaustion > 0.0F) {
                ThirstUtil.addExhaustion(player, clampedExhaustion);
            }
        }

        double temperature = ArcaneSurvivalConfig.temperatureEffect(schoolId) * temperatureScale;
        applyTemperature(player, temperature);
    }

    public static void applyTarget(ServerPlayer player, ResourceLocation schoolId) {
        if (!ArcaneSurvivalConfig.TARGET_EFFECTS.get() || !canAffect(player)) {
            return;
        }

        double temperature = ArcaneSurvivalConfig.temperatureEffect(schoolId)
                * ArcaneSurvivalConfig.TARGET_TEMPERATURE_MULTIPLIER.get();
        int wetness = ArcaneSurvivalConfig.wetnessEffect(schoolId);
        if ((temperature == 0.0 && wetness == 0)
                || !COOLDOWNS.allowTarget(player.getUUID(), player.serverLevel().getGameTime(),
                ArcaneSurvivalConfig.TARGET_EFFECT_COOLDOWN.get())) {
            return;
        }
        applyTemperature(player, temperature);
        if (wetness != 0 && WetnessUtil.isWetnessActive(player)) {
            WetnessUtil.addWetness(player, wetness);
        }
    }

    private static void applyTemperature(ServerPlayer player, double rawChange) {
        double maximum = ArcaneSurvivalConfig.MAX_TEMPERATURE_CHANGE.get();
        float change = (float) Math.max(-maximum, Math.min(maximum, rawChange));
        if (change == 0.0F) {
            return;
        }

        var attachment = AttachmentUtil.getTempAttachment(player);
        attachment.setTemperatureLevel(TemperatureUtil.clampTemperature(
                attachment.getTemperatureLevel() + change));
    }

    private static boolean canAffect(ServerPlayer player) {
        if (!ArcaneSurvivalConfig.ENABLED.get()) {
            return false;
        }
        return !ArcaneSurvivalConfig.IGNORE_CREATIVE.get()
                || (!player.isCreative() && !player.isSpectator());
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        COOLDOWNS.forget(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        COOLDOWNS.clear();
    }
}
