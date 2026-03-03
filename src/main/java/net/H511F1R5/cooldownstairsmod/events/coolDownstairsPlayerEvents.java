package net.H511F1R5.cooldownstairsmod.events;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.H511F1R5.cooldownstairsmod.effect.coolDownstairsEffectRegistry;
import net.H511F1R5.cooldownstairsmod.sounds.coolDownstairsSoundEvents;
import net.H511F1R5.cooldownstairsmod.item.modItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = coolDownstairsMod.MOD_ID)
public class coolDownstairsPlayerEvents {
    @SubscribeEvent
    public static void onBigShotTrade(PlayerEvent.ItemPickupEvent event) {
        Random rand = new Random();
        Player player = event.getEntity();
        ItemStack itemstack = event.getStack();
        float bigShotChance = rand.nextFloat(1);
        // checks for villager emerald trade
        if (player.hasEffect(coolDownstairsEffectRegistry.BIG_SHOT.get()) && itemstack.getOrCreateTag().getBoolean("from_trade")) {
            int amp = player.getEffect((coolDownstairsEffectRegistry.BIG_SHOT.get())).getAmplifier();
            int bonusEmeralds = amp * rand.nextInt(4);
            // 30% chance for extra emeralds
            if (bigShotChance <= .3) {
                player.getInventory().add(new ItemStack(Items.EMERALD, bonusEmeralds));
            }
        }
    }
    public static void dummyFaceWorkDamnit(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().getItem() == modItems.DUMMY_FACE.get()) {
            player.playSound(coolDownstairsSoundEvents.DUMMY_FACE_USE.get(), 1, 1);
        }
    }
}
