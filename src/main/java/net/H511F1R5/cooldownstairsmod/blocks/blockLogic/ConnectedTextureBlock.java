package net.H511F1R5.cooldownstairsmod.blocks.blockLogic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class ConnectedTextureBlock extends Block {
    public static final EnumProperty<MatrixBlockType> TYPE = EnumProperty.create("type", MatrixBlockType.class);

    public ConnectedTextureBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(TYPE, MatrixBlockType.SINGLE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TYPE);
    }

    // Helper to identify our blocks
    public boolean isTable(BlockState state) {
        return state.is(this);
    }

    public boolean isTable(BlockGetter level, BlockPos pos) {
        return isTable(level.getBlockState(pos));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return calculateBestState(context.getLevel(), context.getClickedPos(), this.defaultBlockState());
    }

    protected boolean supports3D() {
        return false;
    }

    protected boolean supports2D() {
        return true;
    }

    protected boolean matchesMultiblockRequirements(BlockState state, BlockState otherState) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level,
            BlockPos currentPos, BlockPos neighborPos) {
        if (!direction.getAxis().isHorizontal() && !supports3D()) {
            return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        }
        return calculateBestState(level, currentPos, state);
    }

    private BlockState calculateBestState(BlockGetter level, BlockPos pos, BlockState currentState) {
        MatrixBlockType type = null;

        if (supports2D()) {
            type = tryForm2x3(level, pos);
            if (type != null)
                return currentState.setValue(TYPE, type);

            type = tryForm2x2(level, pos);
            if (type != null)
                return currentState.setValue(TYPE, type);

            type = tryForm1x2(level, pos);
            if (type != null)
                return currentState.setValue(TYPE, type);
        }

        if (supports3D()) {
            type = tryForm2x2x2(level, pos, currentState);
            if (type != null)
                return currentState.setValue(TYPE, type);

            type = tryForm1x1x2(level, pos, currentState);
            if (type != null)
                return currentState.setValue(TYPE, type);
        }

        return currentState.setValue(TYPE, MatrixBlockType.SINGLE);
    }

    // =========================================================================================
    // STRICT MATRIX FORMATION TEMPLATES
    // =========================================================================================

    private MatrixBlockType tryForm2x3(BlockGetter level, BlockPos pos) {
        // EW (3x2) check
        for (int dx = -2; dx <= 0; dx++) {
            for (int dz = -1; dz <= 0; dz++) {
                if (checkArea(level, pos.offset(dx, 0, dz), 3, 2)) {
                    int col = -dx; // 0=W, 1=C, 2=E
                    int row = -dz; // 0=N, 1=S
                    if (row == 0 && col == 0)
                        return MatrixBlockType.DIM_3X2_1A;
                    if (row == 0 && col == 1)
                        return MatrixBlockType.DIM_3X2_1B;
                    if (row == 0 && col == 2)
                        return MatrixBlockType.DIM_3X2_1C;
                    if (row == 1 && col == 0)
                        return MatrixBlockType.DIM_3X2_2A;
                    if (row == 1 && col == 1)
                        return MatrixBlockType.DIM_3X2_2B;
                    return MatrixBlockType.DIM_3X2_2C;
                }
            }
        }
        // NS (2x3) check
        for (int dx = -1; dx <= 0; dx++) {
            for (int dz = -2; dz <= 0; dz++) {
                if (checkArea(level, pos.offset(dx, 0, dz), 2, 3)) {
                    int col = -dx; // 0=W, 1=E
                    int row = -dz; // 0=N, 1=C, 2=S
                    if (row == 0 && col == 0)
                        return MatrixBlockType.DIM_2X3_1A;
                    if (row == 0 && col == 1)
                        return MatrixBlockType.DIM_2X3_1B;
                    if (row == 1 && col == 0)
                        return MatrixBlockType.DIM_2X3_2A;
                    if (row == 1 && col == 1)
                        return MatrixBlockType.DIM_2X3_2B;
                    if (row == 2 && col == 0)
                        return MatrixBlockType.DIM_2X3_3A;
                    return MatrixBlockType.DIM_2X3_3B;
                }
            }
        }
        return null;
    }

    private MatrixBlockType tryForm2x2(BlockGetter level, BlockPos pos) {
        for (int dx = -1; dx <= 0; dx++) {
            for (int dz = -1; dz <= 0; dz++) {
                if (checkArea(level, pos.offset(dx, 0, dz), 2, 2)) {
                    int col = -dx; // 0=W, 1=E
                    int row = -dz; // 0=N, 1=S
                    if (row == 0 && col == 0)
                        return MatrixBlockType.DIM_2X2_1A;
                    if (row == 0 && col == 1)
                        return MatrixBlockType.DIM_2X2_1B;
                    if (row == 1 && col == 0)
                        return MatrixBlockType.DIM_2X2_2A;
                    return MatrixBlockType.DIM_2X2_2B;
                }
            }
        }
        return null;
    }

    private MatrixBlockType tryForm1x2(BlockGetter level, BlockPos pos) {
        // Priority: EAST, SOUTH, WEST, NORTH
        if (isTable(level, pos.east()) && !isPartOfLargerFormation(level, pos.east(), pos))
            return MatrixBlockType.DIM_2X1_1A; // West side of EW
        if (isTable(level, pos.south()) && !isPartOfLargerFormation(level, pos.south(), pos))
            return MatrixBlockType.DIM_1X2_1A; // North side of NS
        if (isTable(level, pos.west()) && !isPartOfLargerFormation(level, pos.west(), pos))
            return MatrixBlockType.DIM_2X1_1B; // East side of EW
        if (isTable(level, pos.north()) && !isPartOfLargerFormation(level, pos.north(), pos))
            return MatrixBlockType.DIM_1X2_2A; // South side of NS
        return null;
    }

    private MatrixBlockType tryForm2x2x2(BlockGetter level, BlockPos pos, BlockState currentState) {
        for (int dx = -1; dx <= 0; dx++) {
            for (int dy = -1; dy <= 0; dy++) {
                for (int dz = -1; dz <= 0; dz++) {
                    if (checkArea3D(level, pos.offset(dx, dy, dz), 2, 2, 2, currentState)) {
                        int col = -dx; // 0=W, 1=E
                        int row = -dz; // 0=N, 1=S
                        int height = -dy; // 0=Bottom, 1=Top
                        if (height == 0) {
                            if (row == 0 && col == 0) return MatrixBlockType.DIM_2X2X2_1A;
                            if (row == 0 && col == 1) return MatrixBlockType.DIM_2X2X2_1B;
                            if (row == 1 && col == 0) return MatrixBlockType.DIM_2X2X2_2A;
                            return MatrixBlockType.DIM_2X2X2_2B;
                        } else {
                            if (row == 0 && col == 0) return MatrixBlockType.DIM_2X2X2_3A;
                            if (row == 0 && col == 1) return MatrixBlockType.DIM_2X2X2_3B;
                            if (row == 1 && col == 0) return MatrixBlockType.DIM_2X2X2_4A;
                            return MatrixBlockType.DIM_2X2X2_4B;
                        }
                    }
                }
            }
        }
        return null;
    }

    private MatrixBlockType tryForm1x1x2(BlockGetter level, BlockPos pos, BlockState currentState) {
        for (int dy = -1; dy <= 0; dy++) {
            if (checkArea3D(level, pos.offset(0, dy, 0), 1, 2, 1, currentState)) {
                int height = -dy; // 0=Bottom, 1=Top
                if (height == 0) {
                    return MatrixBlockType.DIM_1X1X2_1A;
                } else {
                    return MatrixBlockType.DIM_1X1X2_2A;
                }
            }
        }
        return null;
    }

    private boolean checkArea(BlockGetter level, BlockPos startPos, int sizeX, int sizeZ) {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeZ; j++) {
                if (!isTable(level, startPos.offset(i, 0, j)))
                    return false;
            }
        }
        return true;
    }

    private boolean checkArea3D(BlockGetter level, BlockPos startPos, int sizeX, int sizeY, int sizeZ, BlockState currentState) {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeZ; j++) {
                for (int k = 0; k < sizeY; k++) {
                    BlockState pState = level.getBlockState(startPos.offset(i, k, j));
                    if (!isTable(pState) || !matchesMultiblockRequirements(currentState, pState))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean checkAreaIgnoring(BlockGetter level, BlockPos startPos, int sizeX, int sizeZ, BlockPos ignoring) {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeZ; j++) {
                BlockPos p = startPos.offset(i, 0, j);
                if (p.equals(ignoring))
                    return false;
                if (!isTable(level, p))
                    return false;
            }
        }
        return true;
    }

    private boolean checkAreaIgnoring3D(BlockGetter level, BlockPos startPos, int sizeX, int sizeY, int sizeZ, BlockPos ignoring, BlockState currentState) {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeZ; j++) {
                for (int k = 0; k < sizeY; k++) {
                    BlockPos p = startPos.offset(i, k, j);
                    if (p.equals(ignoring))
                        return false;
                    BlockState pState = level.getBlockState(p);
                    if (!isTable(pState) || !matchesMultiblockRequirements(currentState, pState))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean isPartOfLargerFormation(BlockGetter level, BlockPos neighbor, BlockPos ignoring) {
        BlockState currentState = level.getBlockState(neighbor);
        if (supports3D()) {
            for (int dx = -1; dx <= 0; dx++) {
                for (int dy = -1; dy <= 0; dy++) {
                    for (int dz = -1; dz <= 0; dz++) {
                        if (checkAreaIgnoring3D(level, neighbor.offset(dx, dy, dz), 2, 2, 2, ignoring, currentState)) return true;
                    }
                }
            }
            for (int dy = -1; dy <= 0; dy++) {
                if (checkAreaIgnoring3D(level, neighbor.offset(0, dy, 0), 1, 2, 1, ignoring, currentState)) return true;
            }
        }

        // Check if neighbor is part of a 2x3, 3x2, or 2x2.
        for (int dx = -2; dx <= 0; dx++) {
            for (int dz = -2; dz <= 0; dz++) {
                if (dx >= -1 && dz >= -1 && checkAreaIgnoring(level, neighbor.offset(dx, 0, dz), 2, 2, ignoring))
                    return true;
                if (checkAreaIgnoring(level, neighbor.offset(dx, 0, Math.max(dz, -1)), 3, 2, ignoring))
                    return true;
                if (checkAreaIgnoring(level, neighbor.offset(Math.max(dx, -1), 0, dz), 2, 3, ignoring))
                    return true;
            }
        }

        // Check if neighbor is already in an independent 1x2 or 2x1 pair.
        BlockState nState = level.getBlockState(neighbor);
        if (nState.hasProperty(TYPE)) {
            MatrixBlockType nType = nState.getValue(TYPE);
            BlockPos itsPartner = null;
            if (nType == MatrixBlockType.DIM_1X2_1A)
                itsPartner = neighbor.south();
            else if (nType == MatrixBlockType.DIM_1X2_2A)
                itsPartner = neighbor.north();
            else if (nType == MatrixBlockType.DIM_2X1_1A)
                itsPartner = neighbor.east();
            else if (nType == MatrixBlockType.DIM_2X1_1B)
                itsPartner = neighbor.west();
            else if (supports3D()) {
                if (nType == MatrixBlockType.DIM_1X1X2_1A)
                    itsPartner = neighbor.above();
                else if (nType == MatrixBlockType.DIM_1X1X2_2A)
                    itsPartner = neighbor.below();
            }

            if (itsPartner != null && !itsPartner.equals(ignoring))
                return true;
        }

        return false;
    }
}
