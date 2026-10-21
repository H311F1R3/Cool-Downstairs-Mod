package net.H511F1R5.cooldownstairsmod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nullable;

public class rastananaBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public rastananaBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.UPPER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, HALF);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();

        // Ensure we only place the block if the space BELOW is empty, because we are
        // placing the top half first against the ceiling.
        if (blockpos.getY() > level.getMinBuildHeight()
                && level.getBlockState(blockpos.below()).canBeReplaced(pContext)) {
            return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer,
            ItemStack pStack) {
        // Place the bottom half below the block the player just placed (the upper half)
        pLevel.setBlock(pPos.below(), pState.setValue(HALF, DoubleBlockHalf.LOWER), 3);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel,
            BlockPos pCurrentPos, BlockPos pFacingPos) {
        DoubleBlockHalf doubleblockhalf = pState.getValue(HALF);

        // If this is the upper half and the block below (which should be the lower
        // half) changes, OR
        // If this is the lower half and the block above (which should be the upper
        // half) changes:
        if (pFacing.getAxis() == Direction.Axis.Y
                && doubleblockhalf == DoubleBlockHalf.LOWER == (pFacing == Direction.UP)) {
            // Check if the other half is still our block
            return pFacingState.is(this) && pFacingState.getValue(HALF) != doubleblockhalf ? pState
                    : Blocks.AIR.defaultBlockState();
        } else {
            // Check standard survival logic
            return doubleblockhalf == DoubleBlockHalf.UPPER && pFacing == Direction.UP
                    && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState()
                            : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        }
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        if (pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            // Upper half requires a solid ceiling above it.
            BlockPos blockposAbove = pPos.above();
            return pLevel.getBlockState(blockposAbove).isFaceSturdy(pLevel, blockposAbove, Direction.DOWN);
        } else {
            // Lower half survives solely based on the upper half existing above it
            BlockState blockstate = pLevel.getBlockState(pPos.above());
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.UPPER;
        }
    }
}
