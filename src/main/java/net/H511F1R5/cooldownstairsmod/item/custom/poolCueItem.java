package net.H511F1R5.cooldownstairsmod.item.custom;

import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.constant.DataTickets;

public class poolCueItem extends Item implements GeoItem {

    // To the veiwers at home: Below I tried making geckolib anims work. Then I figured out it gecko dont have armBone
    // support for 1.20.1. Never trust MCreator tutorials.
    public static final String CONTROLLER = "controller";
    public static final String CONTROLLER_IDLE = "controlleridle";
    
    public final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation TAKE_POSITION_ANIM = RawAnimation.begin().thenPlay("take_position");
    private static final RawAnimation TEST_SWING_ANIM = RawAnimation.begin().thenPlay("test_swing");

    public static final String IDLE_ANIM_NAME = "idle";
    public static final String TAKE_POSITION_ANIM_NAME = "take_position";
    public static final String TEST_SWING_ANIM_NAME = "test_swing";

    public poolCueItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new net.minecraftforge.client.extensions.common.IClientItemExtensions() {
            private software.bernie.geckolib.renderer.GeoItemRenderer<poolCueItem> renderer = null;

            @Override
            public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new net.H511F1R5.cooldownstairsmod.item.client.poolCueItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public net.minecraft.world.InteractionResultHolder<ItemStack> use(net.minecraft.world.level.Level pLevel, Player pPlayer, net.minecraft.world.InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        
        if (pPlayer.isShiftKeyDown()) {
            pPlayer.startUsingItem(pHand);
            return net.minecraft.world.InteractionResultHolder.consume(itemStack);
        }
        
        return net.minecraft.world.InteractionResultHolder.pass(itemStack);
    }
    
    @Override
    public net.minecraft.world.item.UseAnim getUseAnimation(ItemStack pStack) {
        return net.minecraft.world.item.UseAnim.BOW;
    }
    


    @Override
    public void releaseUsing(ItemStack pStack, net.minecraft.world.level.Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            player.swing(player.getUsedItemHand(), true);
            
            int useTicks = this.getUseDuration(pStack) - pTimeLeft;
            
            if (!pLevel.isClientSide) {
                net.minecraft.world.phys.Vec3 eyePosition = player.getEyePosition();
                net.minecraft.world.phys.Vec3 lookVector = player.getViewVector(1.0f);
                double reach = 5.0D;
                net.minecraft.world.phys.Vec3 endPosition = eyePosition.add(lookVector.x * reach, lookVector.y * reach, lookVector.z * reach);
                
                net.minecraft.world.phys.AABB searchBox = player.getBoundingBox().expandTowards(lookVector.scale(reach)).inflate(1.0D, 1.0D, 1.0D);
                
                net.minecraft.world.phys.EntityHitResult hitResult = null;
                double closestDistance = reach * reach;
                
                for (net.minecraft.world.entity.Entity entity : pLevel.getEntities(player, searchBox, (ent) -> !ent.isSpectator() && ent.isPickable())) {
                    net.minecraft.world.phys.AABB entityBoundingBox = entity.getBoundingBox().inflate(entity.getPickRadius());
                    java.util.Optional<net.minecraft.world.phys.Vec3> optionalHit = entityBoundingBox.clip(eyePosition, endPosition);
                    
                    if (entityBoundingBox.contains(eyePosition)) {
                        if (closestDistance >= 0.0D) {
                            hitResult = new net.minecraft.world.phys.EntityHitResult(entity, optionalHit.orElse(eyePosition));
                            closestDistance = 0.0D;
                        }
                    } else if (optionalHit.isPresent()) {
                        net.minecraft.world.phys.Vec3 hitVec = optionalHit.get();
                        double distanceToHit = eyePosition.distanceToSqr(hitVec);
                        
                        if (distanceToHit < closestDistance || closestDistance == 0.0D) {
                            if (entity.getRootVehicle() == player.getRootVehicle() && !entity.canRiderInteract()) {
                                if (closestDistance == 0.0D) {
                                    hitResult = new net.minecraft.world.phys.EntityHitResult(entity, hitVec);
                                }
                            } else {
                                hitResult = new net.minecraft.world.phys.EntityHitResult(entity, hitVec);
                                closestDistance = distanceToHit;
                            }
                        }
                    }
                }
                
                if (hitResult != null && hitResult.getEntity() instanceof net.H511F1R5.cooldownstairsmod.entity.billiards.billiardBallEntity ball) {
                    ball.onHitEntity(hitResult, true, useTicks);
                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Billiard Ball hit! Grid Pos.: " + ball.lastHitGridSquare));
                }
            }
        }
    }

    public static float getChargePower(int useTicks) {
        int maxChargeTicks = 30;
        int cycle = useTicks % (maxChargeTicks * 2);
        if (cycle < maxChargeTicks) {
            return (float) cycle / maxChargeTicks;
        } else {
            return 1.0f - (float) (cycle - maxChargeTicks) / maxChargeTicks;
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltips.cooldownstairsmod.pool_cue.tooltip").withStyle(ChatFormatting.GREEN));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    // Below, alotta geckolib boilerplate code
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, CONTROLLER_IDLE, 20, this::idleAnimationPredicate));
        // Changed transition length from 20 ticks (1 sec delay) to 1 tick so the swing plays instantly!
        controllers.add(new AnimationController<>(this, CONTROLLER, 1, state -> PlayState.STOP)
            .triggerableAnim(IDLE_ANIM_NAME, IDLE_ANIM)
            .triggerableAnim(TAKE_POSITION_ANIM_NAME, TAKE_POSITION_ANIM)
            .triggerableAnim(TEST_SWING_ANIM_NAME, TEST_SWING_ANIM));
    }

    public <C extends Item & GeoItem> PlayState idleAnimationPredicate(AnimationState<C> event) {
        ItemDisplayContext perspective = event.getData(DataTickets.ITEM_RENDER_PERSPECTIVE);
        
        if (perspective == ItemDisplayContext.GROUND) {
            event.getController().setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public <C extends Item & GeoItem> PlayState animationPredicate(AnimationState<C> event) {
        event.getController().setAnimation(TAKE_POSITION_ANIM);
        return PlayState.CONTINUE;
    }

    public <C extends Item & GeoItem> PlayState testAnimationPredicate(AnimationState<C> event) {
        event.getController().setAnimation(TEST_SWING_ANIM);
        return PlayState.CONTINUE;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            ItemStack offhandItem = player.getOffhandItem();

            if (offhandItem.is(Items.LAPIS_LAZULI)) {
                if (player.getRandom().nextFloat() < 0.10f) {
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0));
                    target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0));

                    if (!player.getAbilities().instabuild) {
                        offhandItem.shrink(1);
                    }
                }
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        // Prevents the vanilla "bobbing" equip animation when the items state is different
        return oldStack.getItem() != newStack.getItem();
    }
}
