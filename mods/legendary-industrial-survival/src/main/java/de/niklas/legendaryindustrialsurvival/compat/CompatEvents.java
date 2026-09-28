package de.niklas.legendaryindustrialsurvival.compat;

import de.niklas.legendaryindustrialsurvival.config.IndustrialConfig;
import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;

public final class CompatEvents {
    @SubscribeEvent
    public void onSpellHeal(SpellHealEvent event) {
        if (!(event.getTargetEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }
        float limbHealing = (float) Math.min(
                event.getHealAmount() * IndustrialConfig.SPELL_LIMB_HEAL_MULTIPLIER.get(),
                IndustrialConfig.SPELL_LIMB_HEAL_CAP.get());
        BodyHealthCompat.healMostInjuredLimb(player, limbHealing);
    }

    @SubscribeEvent
    public void onEquipmentChanged(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            synchronizeHealth(player);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide
                && player.tickCount % IndustrialConfig.HEALTH_SYNC_INTERVAL.get() == 0) {
            synchronizeHealth(player);
        }
    }

    private static void synchronizeHealth(Player player) {
        HealthUtil.updatePlayerMaxHealthAttribute(player);
        BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);
    }
}
