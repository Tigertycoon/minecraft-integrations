package de.niklas.legendaryindustrialsurvival.registry;

import de.niklas.legendaryindustrialsurvival.LegendaryIndustrialSurvival;
import de.niklas.legendaryindustrialsurvival.mekanism.ModuleHydrationUnit;
import de.niklas.legendaryindustrialsurvival.mekanism.ModuleMedicalTreatmentUnit;
import de.niklas.legendaryindustrialsurvival.mekanism.ModuleThermoregulatorUnit;
import mekanism.api.MekanismAPI;
import mekanism.api.gear.IModuleHelper;
import mekanism.api.gear.ModuleData;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMekanismModules {
    public static final DeferredRegister<ModuleData<?>> MODULES =
            DeferredRegister.create(MekanismAPI.MODULE_REGISTRY_NAME, LegendaryIndustrialSurvival.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, LegendaryIndustrialSurvival.MOD_ID);

    public static final DeferredHolder<ModuleData<?>, ModuleData<ModuleThermoregulatorUnit>> THERMOREGULATOR_UNIT =
            MODULES.register("thermoregulator_unit", () -> new ModuleData<>(
                    ModuleData.ModuleDataBuilder.customInstanced(
                            ModuleThermoregulatorUnit::new,
                            ModMekanismModules.THERMOREGULATOR_ITEM)));
    public static final DeferredHolder<ModuleData<?>, ModuleData<ModuleHydrationUnit>> HYDRATION_UNIT =
            MODULES.register("hydration_unit", () -> new ModuleData<>(
                    ModuleData.ModuleDataBuilder.customInstanced(
                            ModuleHydrationUnit::new,
                            ModMekanismModules.HYDRATION_ITEM)));
    public static final DeferredHolder<ModuleData<?>, ModuleData<ModuleMedicalTreatmentUnit>> MEDICAL_TREATMENT_UNIT =
            MODULES.register("medical_treatment_unit", () -> new ModuleData<>(
                    ModuleData.ModuleDataBuilder.customInstanced(
                            ModuleMedicalTreatmentUnit::new,
                            ModMekanismModules.MEDICAL_TREATMENT_ITEM)));

    public static final DeferredHolder<Item, Item> THERMOREGULATOR_ITEM =
            ITEMS.register("module_thermoregulator_unit", () -> IModuleHelper.INSTANCE.createModuleItem(
                    () -> untyped(THERMOREGULATOR_UNIT),
                    new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, Item> HYDRATION_ITEM =
            ITEMS.register("module_hydration_unit", () -> IModuleHelper.INSTANCE.createModuleItem(
                    () -> untyped(HYDRATION_UNIT),
                    new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, Item> MEDICAL_TREATMENT_ITEM =
            ITEMS.register("module_medical_treatment_unit", () -> IModuleHelper.INSTANCE.createModuleItem(
                    () -> untyped(MEDICAL_TREATMENT_UNIT),
                    new Item.Properties().rarity(Rarity.EPIC)));

    private ModMekanismModules() {
    }

    public static void register(IEventBus bus) {
        MODULES.register(bus);
        ITEMS.register(bus);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Holder<ModuleData<?>> untyped(Holder<? extends ModuleData<?>> holder) {
        return (Holder) holder;
    }
}
