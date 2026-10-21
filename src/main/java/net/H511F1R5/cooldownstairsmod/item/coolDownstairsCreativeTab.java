package net.H511F1R5.cooldownstairsmod.item;

import net.H511F1R5.cooldownstairsmod.blocks.coolDownstairsBlocks;
import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class coolDownstairsCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, coolDownstairsMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> COOLDOWNSTAIRS_TAB = CREATIVE_MODE_TAB.register(
            "cooldownstairs_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(coolDownstairsItems.DUMMY_FACE.get()))
                    .title(Component.translatable("creativetab.cooldownstairs_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(coolDownstairsItems.DUMMY_FACE.get());
                        pOutput.accept(coolDownstairsItems.GAME_COMPONENT.get());
                        pOutput.accept(coolDownstairsItems.TIGGER_TELL.get());
                        pOutput.accept(coolDownstairsItems.DEALMAKER.get());
                        pOutput.accept(coolDownstairsItems.FOOTBALL_HELMET.get());
                        pOutput.accept(coolDownstairsItems.CHAIN_SWORD.get());
                        pOutput.accept(coolDownstairsItems.POOL_CUE.get());
                        pOutput.accept(coolDownstairsItems.BUBBLEGUM_BALL.get());
                        pOutput.accept(coolDownstairsItems.CHICKEN_SHANK.get());

                        pOutput.accept(coolDownstairsBlocks.BILLIARD_TABLE.get());
                        pOutput.accept(coolDownstairsBlocks.BILLIARD_GAME_BLOCK.get());
                        pOutput.accept(coolDownstairsBlocks.SPEAKER_BLOCK.get());
                        pOutput.accept(coolDownstairsBlocks.GUMBALL_TOWER.get());
                        pOutput.accept(coolDownstairsBlocks.VINYL_WOOD.get());
                        pOutput.accept(coolDownstairsBlocks.DDR_PAD.get());
                        pOutput.accept(coolDownstairsBlocks.RASTANANA.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
