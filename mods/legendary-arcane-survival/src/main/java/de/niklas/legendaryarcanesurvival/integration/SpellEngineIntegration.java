package de.niklas.legendaryarcanesurvival.integration;

import de.niklas.legendaryarcanesurvival.config.ArcaneSurvivalConfig;
import de.niklas.legendaryarcanesurvival.service.SurvivalEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellEvents;
import net.spell_engine.internals.casting.SpellCast;

public final class SpellEngineIntegration {
    private SpellEngineIntegration() {
    }

    public static void register() {
        SpellEvents.SPELL_CAST.register(SpellEngineIntegration::onSpellCast);
    }

    private static void onSpellCast(SpellEvents.SpellCastEvent.Args args) {
        if (!(args.caster() instanceof ServerPlayer player)
                || args.action() != SpellCast.Action.RELEASE) {
            return;
        }

        Spell spell = args.spell().value();
        if (spell.type != Spell.Type.ACTIVE || spell.school == null) {
            return;
        }

        ResourceLocation schoolId = spell.school.id;
        String spellId = args.spell().unwrapKey()
                .map(key -> key.location().toString())
                .orElse("spell_engine:unregistered");
        int tier = Math.max(0, spell.tier);
        float castDuration = spell.active != null && spell.active.cast != null
                ? Math.max(0.0F, spell.active.cast.duration)
                : 0.0F;
        double exhaustion = ArcaneSurvivalConfig.BASE_EXHAUSTION.get()
                + tier * ArcaneSurvivalConfig.TIER_EXHAUSTION.get()
                + castDuration * ArcaneSurvivalConfig.CAST_DURATION_EXHAUSTION.get();
        double temperatureScale = 1.0 + tier * 0.08;

        SurvivalEffects.applyCast(player, spellId, schoolId, exhaustion, temperatureScale);

        if (isDirect(spell)) {
            for (Entity target : args.targets()) {
                if (target instanceof ServerPlayer targetPlayer && targetPlayer != player) {
                    SurvivalEffects.applyTarget(targetPlayer, schoolId);
                }
            }
        }
    }

    private static boolean isDirect(Spell spell) {
        return spell.deliver == null
                || spell.deliver.type == null
                || spell.deliver.type == Spell.Delivery.Type.DIRECT;
    }
}
