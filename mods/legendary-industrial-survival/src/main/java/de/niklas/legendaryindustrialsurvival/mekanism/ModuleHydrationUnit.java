package de.niklas.legendaryindustrialsurvival.mekanism;

import de.niklas.legendaryindustrialsurvival.config.IndustrialConfig;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.IModuleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

public final class ModuleHydrationUnit implements ICustomModule<ModuleHydrationUnit> {
    @Override
    public void tickServer(
            IModule<ModuleHydrationUnit> module,
            IModuleContainer moduleContainer,
            ItemStack stack,
            Player player) {
        if (player.tickCount % 20 != 0 || !ThirstUtil.isThirstActive(player)) {
            return;
        }

        var thirst = AttachmentUtil.getThirstAttachment(player);
        if (thirst.getHydrationLevel() > IndustrialConfig.HYDRATION_THRESHOLD.get()) {
            return;
        }

        long energy = IndustrialConfig.HYDRATION_ENERGY.get();
        if (!module.canUseEnergy(player, stack, energy)) {
            return;
        }
        module.useEnergy(player, stack, energy);
        thirst.addHydrationLevel(1);
        thirst.setDirty();
    }
}
