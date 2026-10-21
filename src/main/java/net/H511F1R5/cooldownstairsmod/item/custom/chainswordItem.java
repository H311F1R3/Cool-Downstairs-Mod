package net.H511F1R5.cooldownstairsmod.item.custom;

import net.H511F1R5.cooldownstairsmod.effect.coolDownstairsEffectRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class chainswordItem extends SwordItem {

    public chainswordItem(Item.Properties pProperties) {
        // 5 is attack damage modifier, -2.4f is attack speed modifier
        super(Tiers.DIAMOND, 5, -2.4f, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        CompoundTag tag = stack.getOrCreateTag();
        int hits = tag.getInt("chainsword_hits") + 1;

        // Every 5th hit, apply the Chain Bleed effect
        if (hits >= 5) {
            target.addEffect(new MobEffectInstance(coolDownstairsEffectRegistry.CHAIN_BLEED.get(), 60, 0));
            hits = 0;
        }

        tag.putInt("chainsword_hits", hits);

        return super.hurtEnemy(stack, target, attacker);
    }
}
