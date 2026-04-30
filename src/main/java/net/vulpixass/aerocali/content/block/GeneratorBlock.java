package net.vulpixass.aerocali.content.block;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;

public class GeneratorBlock extends DirectionalKineticBlock implements IBE<GeneratorBlockEntity> {

    public GeneratorBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(FACING).getAxis();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return blockState.getValue(FACING).getAxis();
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }


    @Override
    public Class<GeneratorBlockEntity> getBlockEntityClass() {
        return GeneratorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GeneratorBlockEntity> getBlockEntityType() {
        return AerocaliBlockEntities.GENERATOR.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new GeneratorBlockEntity(p_153215_, p_153216_);
    }
}
