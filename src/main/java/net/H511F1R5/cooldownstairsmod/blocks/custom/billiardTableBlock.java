package net.H511F1R5.cooldownstairsmod.blocks.custom;

import net.H511F1R5.cooldownstairsmod.blocks.blockLogic.ConnectedTextureBlock;
import net.H511F1R5.cooldownstairsmod.blocks.blockLogic.blockPixelDetection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class billiardTableBlock extends ConnectedTextureBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public billiardTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos,
            CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!state.hasProperty(TYPE)) return;

        // Uses the new structural checking matrix to determine if the entity's hitbox center
        // translates to a 0-15 'pocket' pixel index inside this specific block configuration!
        if (blockPixelDetection.isEntityInPocket(entity, pos, state.getValue(TYPE))) {
            // Do your magic here! e.g., sink the entity.
            System.out.println("Entity is in a pocket!");
        }
    }
}
