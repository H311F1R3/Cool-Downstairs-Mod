package net.H511F1R5.cooldownstairsmod.blocks.custom;

import javax.annotation.Nullable;

import net.H511F1R5.cooldownstairsmod.blocks.blockLogic.ConnectedTextureBlock;
import net.H511F1R5.cooldownstairsmod.blocks.blockLogic.blockPixelDetection;
import net.H511F1R5.cooldownstairsmod.blocks.entity.billiardGameTableBlockEntity;
import net.H511F1R5.cooldownstairsmod.blocks.entity.coolDownstairsBlockEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class billiardGameBlock extends ConnectedTextureBlock implements EntityBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public billiardGameBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state){
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos,
            CollisionContext pContext) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof billiardGameTableBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)player), (billiardGameTableBlockEntity)entity, pos);
                // ^^^ Will not work later when porting, blame kaupen
            } else {
                throw new IllegalStateException("Container has no Provider!");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new billiardGameTableBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        
        return createTickerHelper(pBlockEntityType, coolDownstairsBlockEntities.BILLIARD_GAME_TABLE_BE.get(), (pWorld, pPos, pBlockState, pBlockEntity) -> pBlockEntity.tick(pWorld, pPos, pBlockState ));
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> pServerType, BlockEntityType<E> pTargetType, BlockEntityTicker<? super E> pTicker) {
        return pTargetType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
    }
}
