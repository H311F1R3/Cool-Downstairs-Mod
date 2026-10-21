package net.H511F1R5.cooldownstairsmod.blocks.blockLogic;

import net.minecraft.util.StringRepresentable;

public enum MatrixBlockType implements StringRepresentable {
    SINGLE("single"),
    
    // 1x2 North-South
    DIM_1X2_1A("1x2_1a"),
    DIM_1X2_2A("1x2_2a"),
    
    // 2x1 East-West
    DIM_2X1_1A("2x1_1a"),
    DIM_2X1_1B("2x1_1b"),
    
    // 2x2 Square
    DIM_2X2_1A("2x2_1a"),
    DIM_2X2_1B("2x2_1b"),
    DIM_2X2_2A("2x2_2a"),
    DIM_2X2_2B("2x2_2b"),
    
    // 2x3 North-South (2 wide, 3 deep)
    DIM_2X3_1A("2x3_1a"),
    DIM_2X3_1B("2x3_1b"),
    DIM_2X3_2A("2x3_2a"),
    DIM_2X3_2B("2x3_2b"),
    DIM_2X3_3A("2x3_3a"),
    DIM_2X3_3B("2x3_3b"),
    
    // 3x2 East-West (3 wide, 2 deep)
    DIM_3X2_1A("3x2_1a"),
    DIM_3X2_1B("3x2_1b"),
    DIM_3X2_1C("3x2_1c"),
    DIM_3X2_2A("3x2_2a"),
    DIM_3X2_2B("3x2_2b"),
    DIM_3X2_2C("3x2_2c"),

    // 1x1x2 Vertical (1 wide, 1 deep, 2 high)
    DIM_1X1X2_1A("1x1x2_1a"),
    DIM_1X1X2_2A("1x1x2_2a"),
    
    // 2x2x2 Cube (2 wide, 2 deep, 2 high)
    DIM_2X2X2_1A("2x2x2_1a"),
    DIM_2X2X2_1B("2x2x2_1b"),
    DIM_2X2X2_2A("2x2x2_2a"),
    DIM_2X2X2_2B("2x2x2_2b"),
    DIM_2X2X2_3A("2x2x2_3a"),
    DIM_2X2X2_3B("2x2x2_3b"),
    DIM_2X2X2_4A("2x2x2_4a"),
    DIM_2X2X2_4B("2x2x2_4b");

    private final String name;

    MatrixBlockType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
