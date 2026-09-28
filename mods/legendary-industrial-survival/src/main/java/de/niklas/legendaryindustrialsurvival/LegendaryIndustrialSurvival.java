package de.niklas.legendaryindustrialsurvival;

import de.niklas.legendaryindustrialsurvival.config.IndustrialConfig;
import de.niklas.legendaryindustrialsurvival.registry.ModBlockEntities;
import de.niklas.legendaryindustrialsurvival.registry.ModBlocks;
import de.niklas.legendaryindustrialsurvival.registry.ModCreativeTabs;
import de.niklas.legendaryindustrialsurvival.registry.ModItems;
import de.niklas.legendaryindustrialsurvival.registry.ModMekanismModules;
import de.niklas.legendaryindustrialsurvival.compat.CompatEvents;
import mekanism.api.MekanismIMC;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(LegendaryIndustrialSurvival.MOD_ID)
public final class LegendaryIndustrialSurvival {
    public static final String MOD_ID = "legendary_industrial_survival";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public LegendaryIndustrialSurvival(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, IndustrialConfig.SPEC);
        ModBlocks.register(modBus);
        ModBlockEntities.register(modBus);
        ModItems.register(modBus);
        ModMekanismModules.register(modBus);
        ModCreativeTabs.register(modBus);
        modBus.addListener(this::enqueueInterModCommunication);
        NeoForge.EVENT_BUS.register(new CompatEvents());
        LOGGER.info("Legendary Industrial Survival initialized");
    }

    private void enqueueInterModCommunication(InterModEnqueueEvent event) {
        MekanismIMC.addMekaSuitBodyarmorModules(
                ModMekanismModules.THERMOREGULATOR_UNIT,
                ModMekanismModules.MEDICAL_TREATMENT_UNIT);
        MekanismIMC.addMekaSuitHelmetModules(ModMekanismModules.HYDRATION_UNIT);
    }
}
