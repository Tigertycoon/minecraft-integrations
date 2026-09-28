package de.niklas.legendaryindustrialsurvival.compat;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public final class BodyHealthCompat {
    private BodyHealthCompat() {
    }

    public static boolean healMostInjuredLimb(Player player, float amount) {
        if (!Config.Baked.localizedBodyDamageEnabled || amount <= 0) {
            return false;
        }

        BodyPartEnum mostInjured = null;
        float lowestRatio = 1.0F;
        for (BodyPartEnum bodyPart : BodyPartEnum.values()) {
            float ratio = BodyDamageUtil.getHealthRatio(player, bodyPart);
            if (ratio < lowestRatio) {
                lowestRatio = ratio;
                mostInjured = bodyPart;
            }
        }

        if (mostInjured == null) {
            return false;
        }
        BodyDamageUtil.healBodyPart(player, mostInjured, amount);
        BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);
        return true;
    }
}
