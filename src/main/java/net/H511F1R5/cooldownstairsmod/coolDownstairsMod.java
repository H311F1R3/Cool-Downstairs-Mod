package net.H511F1R5.cooldownstairsmod;

import com.mojang.logging.LogUtils;
import net.H511F1R5.cooldownstairsmod.blocks.coolDownstairsBlocks;
import net.H511F1R5.cooldownstairsmod.item.coolDownstairsCreativeTab;
import net.H511F1R5.cooldownstairsmod.item.coolDownstairsItems;
import net.H511F1R5.cooldownstairsmod.sounds.coolDownstairsSoundEvents;
import net.H511F1R5.cooldownstairsmod.effect.coolDownstairsEffectRegistry;
import net.H511F1R5.cooldownstairsmod.blocks.entity.coolDownstairsBlockEntities;
import net.H511F1R5.cooldownstairsmod.screen.coolDownstairsMenuTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.H511F1R5.cooldownstairsmod.item.custom.bubblegumBallItem;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(coolDownstairsMod.MOD_ID)
public class coolDownstairsMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "cooldownstairsmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public coolDownstairsMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        coolDownstairsCreativeTab.register(modEventBus);
        coolDownstairsItems.register(modEventBus);
        coolDownstairsBlocks.register(modEventBus);
        coolDownstairsEffectRegistry.EFFECT_DEF_REG.register(modEventBus);
        coolDownstairsSoundEvents.register(modEventBus);
        coolDownstairsBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        net.H511F1R5.cooldownstairsmod.entity.coolDownstairsEntities.register(modEventBus);
        coolDownstairsMenuTypes.register(modEventBus);
        
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods
    // in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onRegisterRenderers(net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(net.H511F1R5.cooldownstairsmod.entity.coolDownstairsEntities.BILLIARD_BALL.get(), net.H511F1R5.cooldownstairsmod.entity.client.billiardBallRenderer::new);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemProperties.register(coolDownstairsItems.BUBBLEGUM_BALL.get(), new ResourceLocation(MOD_ID, "color"),
                        (stack, pLevel, entity, seed) -> {
                            if (stack.hasTag() && stack.getTag().contains("bubbleColor")) {
                                return (float) stack.getTag().getInt("bubbleColor");
                            }
                            // Fallback for Creative Tab or uninitialized items: cycle every 10 ticks (0.5s)
                            float time = pLevel != null ? pLevel.getGameTime() : 0;
                            return (time / 10f) % 9f;
                        });



                ItemBlockRenderTypes.setRenderLayer(coolDownstairsBlocks.GUMBALL_TOWER.get(), RenderType.cutout());
            });
        }
    }
}
