package de.niklas.legendaryarcanesurvival;

import de.niklas.legendaryarcanesurvival.config.ArcaneSurvivalConfig;
import de.niklas.legendaryarcanesurvival.integration.IronsSpellsIntegration;
import de.niklas.legendaryarcanesurvival.integration.SpellEngineIntegration;
import de.niklas.legendaryarcanesurvival.service.SurvivalEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(LegendaryArcaneSurvival.MOD_ID)
public final class LegendaryArcaneSurvival {
    public static final String MOD_ID = "legendary_arcane_survival";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public LegendaryArcaneSurvival(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, ArcaneSurvivalConfig.SPEC);
        NeoForge.EVENT_BUS.register(SurvivalEffects.class);

        if (ModList.get().isLoaded("irons_spellbooks")) {
            NeoForge.EVENT_BUS.register(new IronsSpellsIntegration());
            LOGGER.info("Enabled Iron's Spells integration");
        }

        if (ModList.get().isLoaded("spell_engine")) {
            SpellEngineIntegration.register();
            LOGGER.info("Enabled Spell Engine integration");
        }

        LOGGER.info("Legendary Arcane Survival initialized");
    }
}
