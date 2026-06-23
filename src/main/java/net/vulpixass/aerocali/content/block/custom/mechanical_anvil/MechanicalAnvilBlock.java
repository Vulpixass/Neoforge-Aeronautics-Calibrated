package net.vulpixass.aerocali.content.block.custom.mechanical_anvil;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;

public class MechanicalAnvilBlock extends KineticBlock implements IBE<MechanicalAnvilBlockEntity> {
    public static final IntegerProperty MASS = IntegerProperty.create("mass", 0, 3);
    public MechanicalAnvilBlock(Properties properties, int value) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(MASS, value));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MASS);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction direction) {return direction.getAxis().isHorizontal();}

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return false;
    }

    @Override
    public Class<MechanicalAnvilBlockEntity> getBlockEntityClass() {
        return MechanicalAnvilBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalAnvilBlockEntity> getBlockEntityType() {return AerocaliBlockEntities.MECHANICAL_ANVIL.get();}

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // This took me... 20 minutes...
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(1, 4, 12, 4, 12, 15),
            Block.box(1, 4, 1, 4, 12, 4),
            Block.box(12, 4, 1, 15, 12, 4),
            Block.box(12, 4, 12, 15, 12, 15),
            Block.box(14.5, 4, 4, 15.5, 12, 12),
            Block.box(4, 4, 0.5, 12, 12, 1.5),
            Block.box(4, 4, 14.5, 12, 12, 15.5),
            Block.box(0.4, 4, 4, 1.4, 12, 12),
            Block.box(0, 12, 0, 16, 13, 4),
            Block.box(0, 12, 12, 16, 13, 16),
            Block.box(12, 12, 4, 16, 13, 12),
            Block.box(0, 12, 4, 4, 13, 12)
    );

}
