package de.niklas.legendaryindustrialsurvival.blockentity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import de.niklas.legendaryindustrialsurvival.block.KineticClimateBlock;
import de.niklas.legendaryindustrialsurvival.config.IndustrialConfig;
import de.niklas.legendaryindustrialsurvival.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class KineticClimateBlockEntity extends KineticBlockEntity {
    private float lastCheckedSpeed = Float.NaN;

    public KineticClimateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KINETIC_CLIMATE.get(), pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (level != null && !level.isClientSide && Float.compare(lastCheckedSpeed, getSpeed()) != 0) {
            updatePowerLevel();
        }
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        updatePowerLevel();
    }

    @Override
    public float calculateStressApplied() {
        float impact = IndustrialConfig.STRESS_IMPACT.get().floatValue();
        lastStressApplied = impact;
        return impact;
    }

    private void updatePowerLevel() {
        if (level == null || level.isClientSide) {
            return;
        }

        lastCheckedSpeed = getSpeed();
        BlockState state = getBlockState();
        if (!state.hasProperty(KineticClimateBlock.POWER_LEVEL)) {
            return;
        }

        int newLevel = isOverStressed() ? 0 : IndustrialConfig.levelForSpeed(lastCheckedSpeed);
        if (state.getValue(KineticClimateBlock.POWER_LEVEL) != newLevel) {
            level.setBlock(worldPosition, state.setValue(KineticClimateBlock.POWER_LEVEL, newLevel), Block.UPDATE_ALL);
            setChanged();
        }
    }
}
