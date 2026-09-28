package de.niklas.legendaryindustrialsurvival;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Runs in a real GameTest world, including Mekanism's world-backed tick handlers. */
@GameTestHolder(LegendaryIndustrialSurvival.MOD_ID)
@PrefixGameTestTemplate(false)
public final class IndustrialGameTests {
    @GameTest(template = "empty")
    public static void allCraftedItemsAndRecipesLoad(GameTestHelper helper) {
        String[] items = {"kinetic_heater", "kinetic_cooler", "drink_upgrade", "advanced_drink_upgrade",
                "medigel", "nanite_injector", "module_hydration_unit", "module_medical_treatment_unit",
                "module_thermoregulator_unit"};
        for (String name : items) {
            var id = ResourceLocation.fromNamespaceAndPath(LegendaryIndustrialSurvival.MOD_ID, name);
            helper.assertTrue(BuiltInRegistries.ITEM.containsKey(id), "Missing registered item: " + id);
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(id).isPresent(),
                    "Recipe failed to load: " + id);
        }
        helper.succeed();
    }
}
