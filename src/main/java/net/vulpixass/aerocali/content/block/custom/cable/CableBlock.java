package net.vulpixass.aerocali.content.block.custom.cable;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.network.NetworkDimensionHandler;
import net.vulpixass.aerocali.content.network.NetworkManager;
import net.vulpixass.aerocali.content.network.cable.EnergyNetwork;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CableBlock extends Block implements IBE<CableBlockEntity> {
    // Just don't ask... I know it's terrible
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    private static final VoxelShape CORE_SHAPE = Block.box(5.0, 5.0, 5.0, 11.0, 11.0, 11.0);

    private static final VoxelShape NORTH_SHAPE = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 5.0);
    private static final VoxelShape SOUTH_SHAPE = Block.box(6.0, 6.0, 11.0, 10.0, 10.0, 16.0);
    private static final VoxelShape EAST_SHAPE  = Block.box(11.0, 6.0, 6.0, 16.0, 10.0, 10.0);
    private static final VoxelShape WEST_SHAPE  = Block.box(0.0, 6.0, 6.0, 5.0, 10.0, 10.0);
    private static final VoxelShape UP_SHAPE    = Block.box(6.0, 11.0, 6.0, 10.0, 16.0, 10.0);
    private static final VoxelShape DOWN_SHAPE  = Block.box(6.0, 0.0, 6.0, 10.0, 5.0, 10.0);

    public CableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(SOUTH, false)
                .setValue(EAST, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false).setValue(ACTIVE, false));
    }

    private boolean shouldConnectTo(LevelAccessor level, BlockPos currentPos, Direction direction) {
        BlockPos targetPos = currentPos.relative(direction);
        BlockState targetState = level.getBlockState(targetPos);

        if (targetState.getBlock() instanceof CableBlock) return true;

        if (level instanceof Level regularLevel) {
            var cap = regularLevel.getCapability(Capabilities.EnergyStorage.BLOCK, targetPos, direction.getOpposite());
            return cap != null;
        }

        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return this.defaultBlockState()
                .setValue(NORTH, shouldConnectTo(level, pos, Direction.NORTH))
                .setValue(SOUTH, shouldConnectTo(level, pos, Direction.SOUTH))
                .setValue(EAST, shouldConnectTo(level, pos, Direction.EAST))
                .setValue(WEST, shouldConnectTo(level, pos, Direction.WEST))
                .setValue(UP, shouldConnectTo(level, pos, Direction.UP))
                .setValue(DOWN, shouldConnectTo(level, pos, Direction.DOWN));
    }

    //Just creates the dynamic Shape
    @Override
    protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = CORE_SHAPE;

        if (state.getValue(NORTH)) shape = Shapes.or(shape, NORTH_SHAPE);
        if (state.getValue(SOUTH)) shape = Shapes.or(shape, SOUTH_SHAPE);
        if (state.getValue(EAST))  shape = Shapes.or(shape, EAST_SHAPE);
        if (state.getValue(WEST))  shape = Shapes.or(shape, WEST_SHAPE);
        if (state.getValue(UP))    shape = Shapes.or(shape, UP_SHAPE);
        if (state.getValue(DOWN))  shape = Shapes.or(shape, DOWN_SHAPE);

        return shape;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        BooleanProperty property = getPropertyForDirection(direction);
        return state.setValue(property, shouldConnectTo(level, currentPos, direction));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, ACTIVE);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {return true;}
    @Override
    protected int getLightBlock(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {return 0;}


    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.isClientSide()) return;

        NetworkManager manager = NetworkDimensionHandler.get(level);
        if (manager == null) return;

        Set<UUID> neighboringNetworkIDs = new HashSet<>();

        // gets the Neighboring Networks
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);

            UUID neighborNetworkId = manager.getNetworkUUID(neighborPos);
            if (neighborNetworkId != null) {
                neighboringNetworkIDs.add(neighborNetworkId);
            }
        }

        // Creates a new Network if there are no neighbors
        if (neighboringNetworkIDs.isEmpty()) {
            Set<BlockPos> initialBlocks = new HashSet<>();
            initialBlocks.add(pos);

            manager.createNetwork(UUID.randomUUID(), initialBlocks, 0);
        } // Adds the Block to the Neighbor network
        else if (neighboringNetworkIDs.size() == 1) {
            UUID singleNetworkID = neighboringNetworkIDs.iterator().next();
            manager.addBlockToNetwork(pos, singleNetworkID);
        } else {
            UUID masterID = neighboringNetworkIDs.iterator().next();
            EnergyNetwork masterNetwork = manager.getNetwork(masterID);
            if (masterNetwork != null) {
                masterNetwork.addBlock(pos);
            }

            manager.mergeNetworks(neighboringNetworkIDs);
        }

        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide() && !state.is(newState.getBlock())) {
            NetworkManager manager = NetworkDimensionHandler.get(level);
            if (manager != null) {
                UUID networkId = manager.getNetworkUUID(pos);
                if (networkId != null) {
                    manager.splitNetwork(pos, networkId);
                }
            }

            super.onRemove(state, level, pos, newState, movedByPiston);
        } else {
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    public static BooleanProperty getPropertyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }

    @Override
    public Class<CableBlockEntity> getBlockEntityClass() {
        return CableBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CableBlockEntity> getBlockEntityType() {
        return AerocaliBlockEntities.CABLE.get();
    }
}
