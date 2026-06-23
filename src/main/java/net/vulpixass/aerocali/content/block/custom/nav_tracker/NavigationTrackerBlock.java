package net.vulpixass.aerocali.content.block.custom.nav_tracker;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.block.IBE;
import dev.simulated_team.simulated.content.blocks.nav_table.NavTableBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.item.custom.NavigationElementItem;

import java.util.List;

public class NavigationTrackerBlock extends DirectionalKineticBlock implements IBE<NavigationTrackerBlockEntity> {
    public NavigationTrackerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        return defaultBlockState().setValue(FACING, facing);
    }

    @Override
    public Class<NavigationTrackerBlockEntity> getBlockEntityClass() {
        return NavigationTrackerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends NavigationTrackerBlockEntity> getBlockEntityType() {
        return AerocaliBlockEntities.TRACKER.get();
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
        return new NavigationTrackerBlockEntity(blockPos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof NavigationTrackerBlockEntity tracker) {
            ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack stackInSlot = tracker.getInventory().getStackInSlot(0);

            // If hand is empty and slot has item -> Take it
            if (stackInHand.isEmpty() && !stackInSlot.isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, tracker.getInventory().extractItem(0, 1, false));
                return ItemInteractionResult.SUCCESS;
            }
            // If hand has compass and slot is empty -> Put it in
            else if (stackInHand.getItem() instanceof NavigationElementItem && stackInSlot.isEmpty()) {
                tracker.getInventory().insertItem(0, player.getItemInHand(InteractionHand.MAIN_HAND).split(1), false);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    // Drops the Compass once the Tracker is broken
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof NavigationTrackerBlockEntity tracker) {
                ItemStackHandler inv = tracker.getInventory();
                for (int i = 0; i < inv.getSlots(); i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack.copy());
                        inv.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
            return;
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }


    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof NavigationTrackerBlockEntity tracker) {
            return tracker.getRedstoneLevel();
        }
        return 0;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(-2, 1, -2, 18, 24, 18);
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return false;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockEntity be = level.getBlockEntity(belowPos);
        return be instanceof NavTableBlockEntity;
    }

    // Destroys all Blocks above itself that are NOT a shaft (and itself if the Navigation Table isn't there)
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (level instanceof Level world && !world.isClientSide) {
            if (!state.canSurvive(level, currentPos)) {
                world.destroyBlock(currentPos, true);
            }
            if (direction == Direction.UP && !neighborState.isAir()) {
                if (!(neighborState.getBlock() instanceof ShaftBlock)) {
                    world.destroyBlock(neighborPos, true);
                }
            }
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }
}
