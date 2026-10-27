package net.H511F1R5.cooldownstairsmod.blocks;

import net.H511F1R5.cooldownstairsmod.blocks.entity.gumballTowerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class gumballTowerBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final IntegerProperty ANIMATION_STATE = IntegerProperty.create("animation_state", 0, 2);

    protected static final net.minecraft.world.phys.shapes.VoxelShape SHAPE_LOWER = Block.box(4.0D, 0.0D, 4.0D, 12.0D,
            16.0D, 12.0D);
    protected static final net.minecraft.world.phys.shapes.VoxelShape SHAPE_UPPER = Block.box(4.0D, 0.0D, 4.0D, 12.0D,
            14.0D, 12.0D);

    public gumballTowerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(ANIMATION_STATE, 0));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new gumballTowerBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide()
                ? null
                : (level, pos, state, blockEntity) -> {
                    if (blockEntity instanceof gumballTowerBlockEntity gumballTowerBlockEntity) {
                        gumballTowerBlockEntity.serverTick(level, pos, state, gumballTowerBlockEntity);
                    }
                };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, HALF, ANIMATION_STATE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();

        if (blockpos.getY() < level.getMaxBuildHeight() - 1
                && level.getBlockState(blockpos.above()).canBeReplaced(pContext)) {
            return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite())
                    .setValue(HALF, DoubleBlockHalf.LOWER)
                    .setValue(ANIMATION_STATE, 0);
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer,
                            ItemStack pStack) {
        pLevel.setBlock(pPos.above(), pState.setValue(HALF, DoubleBlockHalf.UPPER).setValue(ANIMATION_STATE, 0), 3);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
                                 BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof gumballTowerBlockEntity blockEntity) {
            return blockEntity.use(pPlayer, pHand);
        }

        return InteractionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel,
                                  BlockPos pCurrentPos, BlockPos pFacingPos) {
        DoubleBlockHalf doubleblockhalf = pState.getValue(HALF);

        // Standard double block logic: if the other half is gone, this half should also
        // disappear.
        if (pFacing.getAxis() == Direction.Axis.Y
                && (doubleblockhalf == DoubleBlockHalf.LOWER) == (pFacing == Direction.UP)) {
            return pFacingState.is(this) && pFacingState.getValue(HALF) != doubleblockhalf ? pState
                    : Blocks.AIR.defaultBlockState();
        } else {
            return doubleblockhalf == DoubleBlockHalf.LOWER && pFacing == Direction.DOWN
                    && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState()
                    : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        }
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        if (pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            // Upper half requires the lower half below it
            BlockState blockstate = pLevel.getBlockState(pPos.below());
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        } else {
            // Lower half survives based on standard block survival (on ground)
            return super.canSurvive(pState, pLevel, pPos);
        }
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return false;
    }

    @Override
    public net.minecraft.world.phys.shapes.VoxelShape getVisualShape(BlockState pState,
                                                                     BlockGetter pLevel, BlockPos pPos,
                                                                     net.minecraft.world.phys.shapes.CollisionContext pContext) {
        return net.minecraft.world.phys.shapes.Shapes.empty();
    }

    @Override
    public net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState pState,
                                                               BlockGetter pLevel, BlockPos pPos,
                                                               net.minecraft.world.phys.shapes.CollisionContext pContext) {
        return pState.getValue(HALF) == DoubleBlockHalf.UPPER ? SHAPE_UPPER : SHAPE_LOWER;
    }
}