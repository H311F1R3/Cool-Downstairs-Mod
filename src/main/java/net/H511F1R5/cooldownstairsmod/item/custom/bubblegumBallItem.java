package net.H511F1R5.cooldownstairsmod.item.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class bubblegumBallItem extends Item {
    public bubblegumBallItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide()) {
            CompoundTag nbt = pStack.getOrCreateTag();
            if (!nbt.contains("bubbleColor")) {
                nbt.putInt("bubbleColor", pLevel.random.nextInt(9));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        CompoundTag nbt = pStack.getTag();
        if (nbt != null && nbt.contains("bubbleColor")) {
            int color = nbt.getInt("bubbleColor");
            switch (color) {
                case 1:
                    pTooltipComponents.add(Component.translatable("tooltip.cooldownstairsmod.bubblegum_blu"));
                    break;
                case 2:
                    pTooltipComponents.add(Component.translatable("tooltip.cooldownstairsmod.bubblegum_grn"));
                    break;
                case 3:
                    pTooltipComponents.add(Component.translatable("tooltip.cooldownstairsmod.bubblegum_ylw"));
                    break;
                case 4:
                    pTooltipComponents.add(Component.translatable("tooltip.cooldownstairsmod.bubblegum_pur"));
                    break;
                case 5:
                    pTooltipComponents.add(Component.translatable("tooltip.cooldownstairsmod.bubblegum_orn"));
                    break;
                case 6:
                    pTooltipComponents.add(Component.translatable("tooltip.cooldownstairsmod.bubblegum_red"));
                    break;
                case 7:
                    pTooltipComponents.add(Component.translatable("tooltip.cooldownstairsmod.bubblegum_wht"));
                    break;
                case 8:
                    pTooltipComponents.add(Component.translatable("tooltip.cooldownstairsmod.bubblegum_trq"));
                    break;
                case 0:
                default:
                    pTooltipComponents.add(Component.translatable("tooltip.cooldownstairsmod.bubblegum_pnk"));
                    break;
            }
        }
    }
}
