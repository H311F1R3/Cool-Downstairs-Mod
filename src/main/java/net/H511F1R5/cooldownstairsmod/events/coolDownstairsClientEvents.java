package net.H511F1R5.cooldownstairsmod.events;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.H511F1R5.cooldownstairsmod.item.custom.poolCueItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = coolDownstairsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class coolDownstairsClientEvents {

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    @SubscribeEvent
    public static void onRenderGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay().id().equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            if (player != null && player.isUsingItem() && player.getUseItem().getItem() instanceof poolCueItem) {
                // Cancel the vanilla experience bar
                event.setCanceled(true);

                // Render custom horse jump bar
                int x = mc.getWindow().getGuiScaledWidth() / 2 - 91;
                int y = mc.getWindow().getGuiScaledHeight() - 32 + 3;
                
                int useTicks = player.getTicksUsingItem();
                float charge = poolCueItem.getChargePower(useTicks);

                // Background (empty)
                event.getGuiGraphics().blit(GUI_ICONS_LOCATION, x, y, 0, 84, 182, 5);

                // Filled portion
                int fill = (int) (charge * 183.0F);
                if (fill > 0) {
                    event.getGuiGraphics().blit(GUI_ICONS_LOCATION, x, y, 0, 89, fill, 5);
                }
            }
        }
    }
}
