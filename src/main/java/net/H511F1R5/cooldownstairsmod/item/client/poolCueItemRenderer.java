package net.H511F1R5.cooldownstairsmod.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.H511F1R5.cooldownstairsmod.item.custom.poolCueItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class poolCueItemRenderer extends GeoItemRenderer<poolCueItem> {
    public poolCueItemRenderer() {
        super(new poolCueItemModel());
    }

    @Override
    public void renderByItem(net.minecraft.world.item.ItemStack stack, net.minecraft.world.item.ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        
        Minecraft mc = Minecraft.getInstance();
        boolean isUsing = false;
        
        // Find if this specific stack is currently being used by any player
        if (mc.level != null) {
            for (net.minecraft.world.entity.player.Player player : mc.level.players()) {
                if (player.isUsingItem() && player.getUseItem() == stack) {
                    isUsing = true;
                    break;
                }
            }
        }
        
        // Change values around tmrw. Need to move pool cue up +Y in 1st per. right & left, plus 3rd per. left
        if (isUsing && (transformType == net.minecraft.world.item.ItemDisplayContext.FIRST_PERSON_RIGHT_HAND ||
            transformType == net.minecraft.world.item.ItemDisplayContext.FIRST_PERSON_LEFT_HAND || 
                        transformType == net.minecraft.world.item.ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
            poseStack.mulPose(com.mojang.math.Axis.XN.rotationDegrees(55f));
            poseStack.translate(0.0f, -0.75f, 0.2f);
        } else if (isUsing && (transformType == net.minecraft.world.item.ItemDisplayContext.THIRD_PERSON_LEFT_HAND)) {
            poseStack.mulPose(com.mojang.math.Axis.XN.rotationDegrees(45f));
            poseStack.translate(0.0f, -0.75f, 0.4f); // So I guess the Z axis makes it go up???
        }
        
        super.renderByItem(stack, transformType, poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    
}
