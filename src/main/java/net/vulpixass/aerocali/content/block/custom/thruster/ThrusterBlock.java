package net.vulpixass.aerocali.content.block.custom.thruster;

import dev.eriksonn.aeronautics.content.blocks.propeller.small.BasePropellerBlock;
import dev.eriksonn.aeronautics.content.blocks.propeller.small.BasePropellerBlockEntity;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.content.item.custom.IonUpgradeItem;

public class ThrusterBlock extends BasePropellerBlock {
    private static final VoxelShape SOUTH_SHAPE = Shapes.or(
            Block.box(1, 1, 0, 15, 15, 2),
            Block.box(0, 0, 4, 16, 16, 8),
            Block.box(1, 1, 8, 15, 15, 10),
            Block.box(2, 2, 10, 14, 14, 13),
            Block.box(3, 3, 13, 13, 13, 17),
            Block.box(4, 4, 17, 12, 12, 18),
            Block.box(4, 4, 18, 12, 12, 20),
            Block.box(10, 6, 18, 12, 10, 20),
            Block.box(4, 10, 18, 12, 12, 20),
            Block.box(4, 6, 18, 6, 10, 20),
            Block.box(4, 4, 18, 12, 6, 20)
    );
    private static final VoxelShaper SHAPER = VoxelShaper.forDirectional(SOUTH_SHAPE, Direction.SOUTH);

    public static final BooleanProperty ION_MODE = BooleanProperty.create("ion_mode");
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    // Defines and sets custom Values to the Blocks Properties
    public ThrusterBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(REVERSED, false)
                .setValue(ION_MODE, false)
                .setValue(POWERED, false));
    }

    @Override
    public BlockEntityType<? extends BasePropellerBlockEntity> getBlockEntityType() {
        return AerocaliBlockEntities.THRUSTER.get();
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = ctx.getHorizontalDirection().getOpposite();

        if (ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown()) facing = facing.getOpposite();
        return this.defaultBlockState().setValue(FACING, facing).setValue(ION_MODE, false).setValue(POWERED, false);
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ThrusterBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // sets the custom Shape of the block for every direction
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        Direction dir = state.getValue(FACING);
        return SHAPER.get(dir);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, REVERSED, ION_MODE, POWERED);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        // Enables Ion Mode
        if (stack.getItem() instanceof IonUpgradeItem && !state.getValue(ION_MODE)) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(ION_MODE, true), 3);
                if (!player.isCreative()) stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        // Disables Ion Mode
        if (player.isShiftKeyDown() && stack.isEmpty() && state.getValue(ION_MODE)) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(ION_MODE, false), 3);
                player.addItem(new ItemStack(AerocaliItems.IONIZED_THERMAL_MECHANISM.get()));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

    }


}
