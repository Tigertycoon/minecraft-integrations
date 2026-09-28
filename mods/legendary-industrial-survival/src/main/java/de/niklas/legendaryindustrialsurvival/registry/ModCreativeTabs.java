package de.niklas.legendaryindustrialsurvival.registry;

import de.niklas.legendaryindustrialsurvival.LegendaryIndustrialSurvival;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LegendaryIndustrialSurvival.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN =
            TABS.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.legendary_industrial_survival.main"))
                    .icon(() -> new ItemStack(ModBlocks.KINETIC_HEATER.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.KINETIC_HEATER.get());
                        output.accept(ModBlocks.KINETIC_COOLER.get());
                        output.accept(ModItems.DRINK_UPGRADE.get());
                        output.accept(ModItems.ADVANCED_DRINK_UPGRADE.get());
                        output.accept(ModMekanismModules.THERMOREGULATOR_ITEM.get());
                        output.accept(ModMekanismModules.HYDRATION_ITEM.get());
                        output.accept(ModMekanismModules.MEDICAL_TREATMENT_ITEM.get());
                        output.accept(ModItems.MEDIGEL.get());
                        output.accept(ModItems.NANITE_INJECTOR.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
