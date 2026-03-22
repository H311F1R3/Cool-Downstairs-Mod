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
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, coolDownstairsMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> COOLDOWNSTAIRS_TAB = CREATIVE_MODE_TAB.register("cooldownstairs_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(modItems.DUMMY_FACE.get()))
                    .title(Component.translatable("creativetab.cooldownstairs_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(modItems.DUMMY_FACE.get());
                        pOutput.accept(modItems.TIGGER_TELL.get());
                        pOutput.accept(modItems.DEALMAKER.get());
                        pOutput.accept(modItems.FOOTBALL_HELMET.get());
                        pOutput.accept(modItems.CHAIN_SWORD.get());
                        pOutput.accept(modItems.POOL_CUE.get());
                        pOutput.accept(coolDownstairsBlocks.BILLIARD_TABLE.get());
                        pOutput.accept(coolDownstairsBlocks.SPEAKER_BLOCK.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
