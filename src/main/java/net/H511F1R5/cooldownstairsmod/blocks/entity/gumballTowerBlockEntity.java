package net.H511F1R5.cooldownstairsmod.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class gumballTowerBlockEntity extends BlockEntity {

    public gumballTowerBlockEntity(BlockPos pos, BlockState state) {
        super(coolDownstairsBlockEntities.GUMBALL_TOWER_BE.get(), pos, state);
    }

}
