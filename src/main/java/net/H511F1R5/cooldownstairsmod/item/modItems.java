package net.H511F1R5.cooldownstairsmod.item;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class modItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, coolDownstairsMod.MOD_ID);

    public static final RegistryObject<Item> DUMMY_FACE = ITEMS.register("dummy_face",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventbus) {
        ITEMS.register(eventbus);
    }
}
