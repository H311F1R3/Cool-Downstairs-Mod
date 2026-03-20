package net.H511F1R5.cooldownstairsmod.effect;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.H511F1R5.cooldownstairsmod.sounds.coolDownstairsSoundEvents;

@Mod.EventBusSubscriber(modid = coolDownstairsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class effectBigShot extends MobEffect {

    public effectBigShot() {
        super(MobEffectCategory.BENEFICIAL, 0X36BA4C);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }

    public void applyEffectTick(LivingEntity livingEnt, int amplifier) {

    }

    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        Player player = event.getEntity();
        if (player.hasEffect(coolDownstairsEffectRegistry.BIG_SHOT.get()) && !player.level().isClientSide()) {
            if (event.getContainer() instanceof MerchantMenu menu) {
                var effectInstance = player.getEffect(coolDownstairsEffectRegistry.BIG_SHOT.get());
                if (effectInstance != null) {
                    int amp = effectInstance.getAmplifier();
                    MerchantOffers offers = menu.getOffers();

                    boolean modified = false;
                    for (MerchantOffer offer : offers) {
                        if (offer.getBaseCostA().getItem() == Items.EMERALD) {
                            int baseCount = offer.getBaseCostA().getCount();
                            // 30% discount per tier
                            int discount = (int) Math.ceil(baseCount * 0.3f * (amp + 1));

                            // Prevent the price from dropping below 1
                            if (baseCount - discount < 1) {
                                discount = baseCount - 1;
                            }

                            if (discount > 0) {
                                offer.addToSpecialPriceDiff(-discount);
                                modified = true;
                            }
                        }
                    }

                    if (modified && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                        serverPlayer.sendMerchantOffers(menu.containerId, offers, menu.getTraderLevel(),
                                menu.getTraderXp(), menu.showProgressBar(), menu.canRestock());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onVillagerTrade(TradeWithVillagerEvent event) {
        Player player = event.getEntity();
        if (player.hasEffect(coolDownstairsEffectRegistry.BIG_SHOT.get())) {
            player.level().playSound(null, player.blockPosition(), coolDownstairsSoundEvents.CHA_CHING.get(),
                    net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
        }
    }
}
