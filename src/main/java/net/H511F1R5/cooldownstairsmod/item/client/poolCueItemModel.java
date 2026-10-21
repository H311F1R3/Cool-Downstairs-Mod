package net.H511F1R5.cooldownstairsmod.item.client;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.H511F1R5.cooldownstairsmod.item.custom.poolCueItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class poolCueItemModel extends GeoModel<poolCueItem> {
    @Override
    public ResourceLocation getModelResource(poolCueItem animatable) {
        return new ResourceLocation(coolDownstairsMod.MOD_ID, "geo/poolcue_full.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(poolCueItem animatable) {
        return new ResourceLocation(coolDownstairsMod.MOD_ID, "textures/item/poolcue_full.png");
    }

    @Override
    public ResourceLocation getAnimationResource(poolCueItem animatable) {
        return new ResourceLocation(coolDownstairsMod.MOD_ID, "animations/poolcue.animation.json");
    }

    @Override
    public void setCustomAnimations(poolCueItem animatable, long instanceId, AnimationState<poolCueItem> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        CoreGeoBone poolCueBone = getAnimationProcessor().getBone("poolCue");
        
        if (poolCueBone != null) {
            Entity entity = animationState.getData(DataTickets.ENTITY);
            
            // If the item is being held/used by a LivingEntity
            if (entity instanceof LivingEntity livingEntity && livingEntity.getUseItem().is(animatable)) {
                int useTicks = livingEntity.getTicksUsingItem();
                float charge = poolCueItem.getChargePower(useTicks);
                
                // Bob the item by moving it up to 1.5 units along the X axis based on the charge progress.
                // We add to the existing X position so we don't overwrite any playing animations.
                poolCueBone.setPosX(poolCueBone.getPosX() + (charge * 1.5f));
            }
        }
    }
}
