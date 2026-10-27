package net.H511F1R5.cooldownstairsmod.blocks.entity;

import net.H511F1R5.cooldownstairsmod.blocks.gumballTowerBlock;
import net.H511F1R5.cooldownstairsmod.item.coolDownstairsItems;
import net.H511F1R5.cooldownstairsmod.sounds.coolDownstairsSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class gumballTowerBlockEntity extends BlockEntity {
    private static final long RESET_DELAY_MILLIS = 4200L;

    private long resetAtMillis = -1L;

    public gumballTowerBlockEntity(BlockPos pos, BlockState state) {
        super(coolDownstairsBlockEntities.GUMBALL_TOWER_BE.get(), pos, state);
    }

    public InteractionResult use(Player player, InteractionHand hand) {
        if (this.level == null) {
            return InteractionResult.PASS;
        }

        BlockState state = this.getBlockState();

        if (!(state.getBlock() instanceof gumballTowerBlock)) {
            return InteractionResult.PASS;
        }

        int currentState = state.getValue(gumballTowerBlock.ANIMATION_STATE);

        if (currentState >= 2) {
            return InteractionResult.PASS;
        }

        int nextState = currentState + 1;

        if (nextState == 1) {
            this.level.playSound(player, this.worldPosition, coolDownstairsSoundEvents.KNOBTURN1.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
        } else if (nextState == 2) {
            this.level.playSound(player, this.worldPosition, coolDownstairsSoundEvents.KNOBTURN2.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
        }

        if (!this.level.isClientSide()) {
            this.setAnimationStateForBothHalves(nextState);

            if (nextState == 2) {
                this.startResetTimerForBothHalves();
            }
        }

        return InteractionResult.sidedSuccess(this.level.isClientSide());
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, gumballTowerBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        if (!(state.getBlock() instanceof gumballTowerBlock)) {
            return;
        }

        if (state.getValue(gumballTowerBlock.ANIMATION_STATE) != 2) {
            blockEntity.resetAtMillis = -1L;
            return;
        }

        if (blockEntity.resetAtMillis < 0L) {
            blockEntity.resetAtMillis = System.currentTimeMillis() + RESET_DELAY_MILLIS;
            blockEntity.setChanged();
            return;
        }

        if (System.currentTimeMillis() < blockEntity.resetAtMillis) {
            return;
        }

        level.setBlock(pos, state.setValue(gumballTowerBlock.ANIMATION_STATE, 0), 3);
        blockEntity.resetAtMillis = -1L;
        blockEntity.setChanged();

        if (state.getValue(gumballTowerBlock.HALF) == DoubleBlockHalf.LOWER) {
            Block.popResource(level, pos, new ItemStack(coolDownstairsItems.BUBBLEGUM_BALL.get()));
        }
    }

    private void setAnimationStateForBothHalves(int animationState) {
        if (this.level == null) {
            return;
        }

        BlockState state = this.getBlockState();

        if (!(state.getBlock() instanceof gumballTowerBlock)) {
            return;
        }

        this.level.setBlock(this.worldPosition, state.setValue(gumballTowerBlock.ANIMATION_STATE, animationState), 3);

        BlockPos otherPos = this.getOtherHalfPos(state);
        BlockState otherState = this.level.getBlockState(otherPos);

        if (otherState.is(state.getBlock())) {
            this.level.setBlock(otherPos, otherState.setValue(gumballTowerBlock.ANIMATION_STATE, animationState), 3);
        }
    }

    private void startResetTimerForBothHalves() {
        if (this.level == null) {
            return;
        }

        long resetTime = System.currentTimeMillis() + RESET_DELAY_MILLIS;

        this.resetAtMillis = resetTime;
        this.setChanged();

        BlockState state = this.getBlockState();
        BlockPos otherPos = this.getOtherHalfPos(state);

        if (this.level.getBlockEntity(otherPos) instanceof gumballTowerBlockEntity otherBlockEntity) {
            otherBlockEntity.resetAtMillis = resetTime;
            otherBlockEntity.setChanged();
        }
    }

    private BlockPos getOtherHalfPos(BlockState state) {
        return state.getValue(gumballTowerBlock.HALF) == DoubleBlockHalf.LOWER
                ? this.worldPosition.above()
                : this.worldPosition.below();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putLong("ResetAtMillis", this.resetAtMillis);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.resetAtMillis = tag.getLong("ResetAtMillis");
    }
}