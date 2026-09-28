package de.niklas.legendaryindustrialsurvival.registry;

import de.niklas.legendaryindustrialsurvival.LegendaryIndustrialSurvival;
import de.niklas.legendaryindustrialsurvival.block.KineticClimateBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, LegendaryIndustrialSurvival.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, LegendaryIndustrialSurvival.MOD_ID);

    public static final DeferredHolder<Block, KineticClimateBlock> KINETIC_HEATER =
            registerBlock("kinetic_heater", () -> new KineticClimateBlock(properties()));
    public static final DeferredHolder<Block, KineticClimateBlock> KINETIC_COOLER =
            registerBlock("kinetic_cooler", () -> new KineticClimateBlock(properties()));

    private ModBlocks() {
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    private static BlockBehaviour.Properties properties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_ORANGE)
                .strength(3.5F)
                .sound(SoundType.COPPER)
                .requiresCorrectToolForDrops();
    }

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> supplier) {
        DeferredHolder<Block, T> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
}
