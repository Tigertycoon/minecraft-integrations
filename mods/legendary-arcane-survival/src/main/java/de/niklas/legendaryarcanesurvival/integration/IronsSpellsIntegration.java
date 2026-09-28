package de.niklas.legendaryarcanesurvival.integration;

import de.niklas.legendaryarcanesurvival.config.ArcaneSurvivalConfig;
import de.niklas.legendaryarcanesurvival.service.SurvivalEffects;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;

public final class IronsSpellsIntegration {
    @SubscribeEvent
    public void onSpellCast(SpellOnCastEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || event.getCastSource() == CastSource.COMMAND
                || event.getCastSource() == CastSource.MOB
                || event.getCastSource() == CastSource.NONE) {
            return;
        }

        double exhaustion = ArcaneSurvivalConfig.BASE_EXHAUSTION.get()
                + event.getManaCost() * ArcaneSurvivalConfig.MANA_EXHAUSTION.get()
                + event.getSpellLevel() * ArcaneSurvivalConfig.TIER_EXHAUSTION.get();
        double temperatureScale = 1.0 + Math.max(0, event.getSpellLevel()) * 0.05;

        SurvivalEffects.applyCast(
                player,
                event.getSpellId(),
                event.getSchoolType().getId(),
                exhaustion,
                temperatureScale);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onSpellDamage(SpellDamageEvent event) {
        if (event.getEntity() instanceof ServerPlayer target) {
            SurvivalEffects.applyTarget(
                    target,
                    event.getSpellDamageSource().spell().getSchoolType().getId());
        }
    }
}
