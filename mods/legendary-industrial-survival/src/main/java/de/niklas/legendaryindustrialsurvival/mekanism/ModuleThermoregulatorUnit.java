package de.niklas.legendaryindustrialsurvival.mekanism;

import de.niklas.legendaryindustrialsurvival.config.IndustrialConfig;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.IModuleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

public final class ModuleThermoregulatorUnit implements ICustomModule<ModuleThermoregulatorUnit> {
    @Override
    public void tickServer(
            IModule<ModuleThermoregulatorUnit> module,
            IModuleContainer moduleContainer,
            ItemStack stack,
            Player player) {
        if (!Config.Baked.temperatureEnabled || player.tickCount % 20 != 0) {
            return;
        }

        var temperature = AttachmentUtil.getTempAttachment(player);
        float current = temperature.getTemperatureLevel();
        float normal = TemperatureEnum.NORMAL.getValue();
        float difference = normal - current;
        if (Math.abs(difference) < 0.01F) {
            return;
        }

        long energy = IndustrialConfig.THERMOREGULATOR_ENERGY.get();
        if (!module.canUseEnergy(player, stack, energy)) {
            return;
        }
        module.useEnergy(player, stack, energy);

        float step = IndustrialConfig.THERMOREGULATOR_STEP.get().floatValue();
        temperature.setTemperatureLevel(current + Math.copySign(Math.min(Math.abs(difference), step), difference));
    }
}
