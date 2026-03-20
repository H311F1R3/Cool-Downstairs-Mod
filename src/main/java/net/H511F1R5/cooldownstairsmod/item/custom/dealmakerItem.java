package net.H511F1R5.cooldownstairsmod.item.custom;

import net.H511F1R5.cooldownstairsmod.item.modItems;
import net.H511F1R5.cooldownstairsmod.effect.coolDownstairsEffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

public class dealmakerItem extends ArmorItem {
    public dealmakerItem(ArmorMaterial material, ArmorItem.Type type, Properties pProperties) {
        super(material, type, pProperties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!world.isClientSide) {
            if (player.getInventory().armor.get(3).getItem() == modItems.DEALMAKER.get()) {
                player.addEffect(new MobEffectInstance(coolDownstairsEffectRegistry.BIG_SHOT.get(), 200, 0, false, false, true));
            }
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack with) {
        return with.is(Tags.Items.INGOTS_IRON) || stack.getItem() == with.getItem();
    }

}
