package de.niklas.legendaryindustrialsurvival.registry;

import de.niklas.legendaryindustrialsurvival.LegendaryIndustrialSurvival;
import de.niklas.legendaryindustrialsurvival.item.DrinkUpgradeItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, LegendaryIndustrialSurvival.MOD_ID);

    public static final DeferredHolder<Item, DrinkUpgradeItem> DRINK_UPGRADE =
            ITEMS.register("drink_upgrade", () -> new DrinkUpgradeItem(false));
    public static final DeferredHolder<Item, DrinkUpgradeItem> ADVANCED_DRINK_UPGRADE =
            ITEMS.register("advanced_drink_upgrade", () -> new DrinkUpgradeItem(true));
    public static final DeferredHolder<Item, BodyHealingItem> MEDIGEL =
            ITEMS.register("medigel", () -> new BodyHealingItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, BodyHealingItem> NANITE_INJECTOR =
            ITEMS.register("nanite_injector", () -> new BodyHealingItem(new Item.Properties().rarity(Rarity.RARE)));

    private ModItems() {
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
