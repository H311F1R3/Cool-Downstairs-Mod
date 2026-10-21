package net.H511F1R5.cooldownstairsmod.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.Containers;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import net.H511F1R5.cooldownstairsmod.screen.billiardGameTableBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraft.world.item.ItemStack;

public class billiardGameTableBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private static final int INPUT_SLOT = 0;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 1;

    private LazyOptional<ItemStackHandler> lazyItemHandler = LazyOptional.of(() -> new ItemStackHandler(1));

    public billiardGameTableBlockEntity(BlockPos pos, BlockState state) {
        this(coolDownstairsBlockEntities.BILLIARD_GAME_TABLE_BE.get(), pos, state);
    }

    public billiardGameTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> billiardGameTableBlockEntity.this.progress;
                    case 1 -> billiardGameTableBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> billiardGameTableBlockEntity.this.progress = pValue;
                    case 1 -> billiardGameTableBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);

    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.cooldownstairsmod.billiard_game_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new billiardGameTableBlockMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", this.itemHandler.serializeNBT());
        pTag.putInt("billiard_game_block_progress", progress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.progress = pTag.getInt("billiard_game_block_progress");
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(hasPayment()){
            increaseGameReadiness(); // increases progress
            setChanged(level, pos, state);

            if(hasProgressFinished()){
                poolGameStart();
                setChanged(level, pos, state);
            }
        } else {
            resetProgress();
        }
    }

    private boolean hasPayment(){
        boolean hasPaymentItem = this.itemHandler.getStackInSlot(INPUT_SLOT).getItem() == net.minecraft.world.item.Items.EMERALD;
        ItemStack result = new ItemStack(net.minecraft.world.item.Items.EMERALD);

        return hasPaymentItem;
    }
    private void increaseGameReadiness(){
        progress++;
    }    
    private boolean hasProgressFinished(){
        return progress >= maxProgress;
    }

    private void poolGameStart(){
        // Enable Start button, and prep pool game initialization for player(s) involved
    }
    
    private void resetProgress(){
        progress = 0;
    }
}
