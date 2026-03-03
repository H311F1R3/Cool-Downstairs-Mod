package net.H511F1R5.cooldownstairsmod.item.custom;

import net.H511F1R5.cooldownstairsmod.item.modItems;
import net.H511F1R5.cooldownstairsmod.sounds.coolDownstairsSoundEvents;
import net.H511F1R5.cooldownstairsmod.effect.coolDownstairsEffectRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

public class dealmakerItem extends Item {
    public dealmakerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!world.isClientSide) {
            if (player.getInventory().armor.get(3).getItem() == modItems.DEALMAKER.get()) {
                player.addEffect(new MobEffectInstance(coolDownstairsEffectRegistry.BIG_SHOT.get(), 210, 0, true, true ));
            }
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack with) {
        return with.is(Tags.Items.INGOTS_IRON) || stack.getItem() == with.getItem();
    }

    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    public SoundEvent getEquipSound() {
        return coolDownstairsSoundEvents.DEALMAKER_EQUIP_SOUND.get();
    }

}
