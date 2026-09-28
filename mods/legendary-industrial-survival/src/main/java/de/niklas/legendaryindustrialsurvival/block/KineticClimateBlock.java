package de.niklas.legendaryindustrialsurvival.block;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import de.niklas.legendaryindustrialsurvival.blockentity.KineticClimateBlockEntity;
import de.niklas.legendaryindustrialsurvival.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public final class KineticClimateBlock extends HorizontalKineticBlock implements IBE<KineticClimateBlockEntity> {
    public static final IntegerProperty POWER_LEVEL = IntegerProperty.create("power_level", 0, 3);

    public KineticClimateBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
                .setValue(POWER_LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWER_LEVEL);
    }

    @Override
    public boolean hasShaftTowards(LevelReader level, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    public Class<KineticClimateBlockEntity> getBlockEntityClass() {
        return KineticClimateBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends KineticClimateBlockEntity> getBlockEntityType() {
        return ModBlockEntities.KINETIC_CLIMATE.get();
    }
}
