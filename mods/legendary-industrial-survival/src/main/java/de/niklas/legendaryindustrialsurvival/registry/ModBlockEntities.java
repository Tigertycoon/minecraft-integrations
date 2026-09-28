package de.niklas.legendaryindustrialsurvival.registry;

import de.niklas.legendaryindustrialsurvival.LegendaryIndustrialSurvival;
import de.niklas.legendaryindustrialsurvival.blockentity.KineticClimateBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, LegendaryIndustrialSurvival.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KineticClimateBlockEntity>> KINETIC_CLIMATE =
            BLOCK_ENTITIES.register("kinetic_climate", () -> BlockEntityType.Builder.of(
                    KineticClimateBlockEntity::new,
                    ModBlocks.KINETIC_HEATER.get(),
                    ModBlocks.KINETIC_COOLER.get()).build(null));

    private ModBlockEntities() {
    }

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
