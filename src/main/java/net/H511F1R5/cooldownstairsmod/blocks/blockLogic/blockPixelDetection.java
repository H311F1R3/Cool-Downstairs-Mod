package net.H511F1R5.cooldownstairsmod.blocks.blockLogic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

/**
 * A utility framework designed to convert global floating-point XYZ coordinates
 * into a local 16x16 pixel grid representation for dynamic block-entity interactions.
 */
public class blockPixelDetection {

    /**
     * Converts a global floating-point coordinate to a local 0-15 pixel value
     * within whatever block space that coordinate currently occupies.
     * 
     * @param coord The global coordinate (X, Y, or Z)
     * @return An integer representing the pixel index (0 to 15)
     */
    public static int getPixelFromGlobalCoordinate(double coord) {
        // Utilizing Math.floor ensures that negative coordinates map correctly.
        // E.g., -0.25 falls into block -1, leaving a local positive decimal of 0.75.
        double localCoord = coord - Math.floor(coord);
        
        // Multiply by 16 and floor to map to 0-15 index
        return (int) Math.floor(localCoord * 16.0);
    }

    /**
     * Returns the entity's exact pixel coordinate on the X axis of the block it is currently inside.
     */
    public static int getEntityPixelX(Entity entity) {
        return getPixelFromGlobalCoordinate(entity.getX());
    }

    /**
     * Returns the entity's exact pixel coordinate on the Y axis of the block it is currently inside.
     */
    public static int getEntityPixelY(Entity entity) {
        return getPixelFromGlobalCoordinate(entity.getY());
    }

    /**
     * Returns the entity's exact pixel coordinate on the Z axis of the block it is currently inside.
     */
    public static int getEntityPixelZ(Entity entity) {
        return getPixelFromGlobalCoordinate(entity.getZ());
    }

    /**
     * Returns an array [X, Y, Z] representing the entity's 16x16 pixel position inside its current block.
     */
    public static int[] getEntityPixelCoords(Entity entity) {
        return new int[]{
            getEntityPixelX(entity),
            getEntityPixelY(entity),
            getEntityPixelZ(entity)
        };
    }

    /**
     * Calculates an entity's pixel position RELATIVE to a specific referenced BlockPos.
     * Useful for block classes (`entityInside`, `stepOn`, etc.) to determine where inside
     * that specific block an entity's center point is hovering. 
     * Note: Entities with large hitboxes could return values < 0 or > 15 if their 
     * center coordinate is hovering just outside the block.
     * 
     * @param entity The entity interacting with the block
     * @param blockPos The block position to calculate relative to
     * @return [X, Y, Z] pixel distances relative to the block's 0,0,0 corner.
     */
    public static int[] getRelativePixelCoords(Entity entity, BlockPos blockPos) {
        double localX = entity.getX() - blockPos.getX();
        double localY = entity.getY() - blockPos.getY();
        double localZ = entity.getZ() - blockPos.getZ();

        return new int[]{
            (int) Math.floor(localX * 16.0),
            (int) Math.floor(localY * 16.0),
            (int) Math.floor(localZ * 16.0)
        };
    }

    /**
     * Checks if an entity's X and Z coordinate centers are currently located over a specified 
     * target pixel (0-15) relative to the passed BlockPos.
     * 
     * @param entity The interacting entity
     * @param pos The block position being checked
     * @param targetPixelX The desired pixel on the X axis (0-15)
     * @param targetPixelZ The desired pixel on the Z axis (0-15)
     * @return True if the entity is over the target pixel.
     */
    public static boolean isEntityOnRelativePixel(Entity entity, BlockPos pos, int targetPixelX, int targetPixelZ) {
        int[] relativePixels = getRelativePixelCoords(entity, pos);
        return relativePixels[0] == targetPixelX && relativePixels[2] == targetPixelZ;
    }

    /**
     * Determines if an entity has fallen into a pocket based on the block's current multiblock layout.
     * Uses the MatrixBlockType to understand which part of the table it is currently interacting with.
     * 
     * @param entity The entity interacting with the block
     * @param pos The base block position
     * @param type The matrix state of the current block
     * @return True if the entity's center is hovering over a pocket pixel
     */
    public static boolean isEntityInPocket(Entity entity, BlockPos pos, net.H511F1R5.cooldownstairsmod.blocks.blockLogic.MatrixBlockType type) {
        int[] relativePixels = getRelativePixelCoords(entity, pos);
        int px = relativePixels[0]; // 0 to 15 (West to East)
        int pz = relativePixels[2]; // 0 to 15 (North to South)

        // Switch through the different table pieces. 
        // NOTE: These X and Z pixel coordinate ranges are placeholders (e.g. treating the corners as 2x2 pockets). 
        // You will need to fine-tune these numbers to perfectly match the black pixels on your textures!
        switch (type) {
            case SINGLE:
                // Pockets in all 4 corners
                return (px <= 2 && pz <= 2) || // NW Corner
                       (px >= 13 && pz <= 2) || // NE Corner
                       (px <= 2 && pz >= 13) || // SW Corner
                       (px >= 13 && pz >= 13);  // SE Corner

            case DIM_1X2_1A: // North half of 1x2
                return (px <= 2 && pz <= 2) || (px >= 13 && pz <= 2);
            case DIM_1X2_2A: // South half of 1x2
                 return (px <= 2 && pz >= 13) || (px >= 13 && pz >= 13);

            case DIM_2X1_1A: // West half of 2x1
                return (px <= 2 && pz <= 2) || (px <= 2 && pz >= 13);
            case DIM_2X1_1B: // East half of 2x1
                return (px >= 13 && pz <= 2) || (px >= 13 && pz >= 13);

            case DIM_2X3_1A: // NW corner of 2x3
                return (px <= 2 && pz <= 2);
            case DIM_2X3_1B: // NE corner of 2x3
                return (px >= 13 && pz <= 2);
            case DIM_2X3_2A: // Left Middle of 2x3 (pocket on the West edge)
                 return (px <= 2 && pz >= 7 && pz <= 8);
            case DIM_2X3_2B: // Right Middle of 2x3 (pocket on the East edge)
                 return (px >= 13 && pz >= 7 && pz <= 8);
            case DIM_2X3_3A: // SW corner of 2x3
                return (px <= 2 && pz >= 13);
            case DIM_2X3_3B: // SE corner of 2x3
                return (px >= 13 && pz >= 13);

            // Add similar cases for your DIM_3X2 layouts (which would have N/S middle pockets instead of E/W)
            // case DIM_3X2_1B: // Middle North edge
            //     return (px >= 7 && px <= 8 && pz <= 2);

            default:
                return false; // Safely ignore unmapped shapes until you plot them out
        }
    }
}
