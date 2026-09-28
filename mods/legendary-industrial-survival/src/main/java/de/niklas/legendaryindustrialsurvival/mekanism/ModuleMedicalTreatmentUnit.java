package de.niklas.legendaryindustrialsurvival.mekanism;

import de.niklas.legendaryindustrialsurvival.compat.BodyHealthCompat;
import de.niklas.legendaryindustrialsurvival.config.IndustrialConfig;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.IModuleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ModuleMedicalTreatmentUnit implements ICustomModule<ModuleMedicalTreatmentUnit> {
    @Override
    public void tickServer(
            IModule<ModuleMedicalTreatmentUnit> module,
            IModuleContainer moduleContainer,
            ItemStack stack,
            Player player) {
        if (player.tickCount % IndustrialConfig.MEDICAL_INTERVAL.get() != 0) {
            return;
        }

        long energy = IndustrialConfig.MEDICAL_ENERGY.get();
        if (!module.canUseEnergy(player, stack, energy)) {
            return;
        }
        if (BodyHealthCompat.healMostInjuredLimb(
                player,
                IndustrialConfig.MEDICAL_HEALING.get().floatValue())) {
            module.useEnergy(player, stack, energy);
        }
    }
}
