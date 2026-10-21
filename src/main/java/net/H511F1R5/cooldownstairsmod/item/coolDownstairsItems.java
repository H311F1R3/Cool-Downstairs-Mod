package net.H511F1R5.cooldownstairsmod.item;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.H511F1R5.cooldownstairsmod.item.custom.*;
import net.H511F1R5.cooldownstairsmod.sounds.coolDownstairsSoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class coolDownstairsItems {

        public static final modArmorMaterials DEALMAKER_MATERIAL = new modArmorMaterials("dealmaker", 17,
                        new int[] { 3, 6, 8, 3 }, 3, 17, coolDownstairsSoundEvents.DEALMAKER_EQUIP_SOUND::get, 0);
        public static final modArmorMaterials FOOTBALLHELM_MATERIAL = new modArmorMaterials("football_helmet", 15,
                        new int[] { 5, 5, 5, 5 }, 5, 14, coolDownstairsSoundEvents.FOOTBALLHELM_EQUIP_SOUND::get, 7);
        public static final modArmorMaterials ROLLERSKATE_MATERIAL = new modArmorMaterials("rollerskate", 15,
                        new int[] { 5, 5, 5, 5 }, 5, 14, () -> SoundEvents.ARMOR_EQUIP_GENERIC, 7);

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        coolDownstairsMod.MOD_ID);

        public static final RegistryObject<Item> DUMMY_FACE = ITEMS.register("dummy_face",
                        () -> new dummyFaceItem(new Item.Properties()));
        public static final RegistryObject<Item> DEALMAKER = ITEMS.register("dealmaker",
                        () -> new dealmakerItem(DEALMAKER_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties()));
        public static final RegistryObject<Item> CHAIN_SWORD = ITEMS.register("chainsword",
                        () -> new chainswordItem(new Item.Properties()));
        public static final RegistryObject<Item> POOL_CUE = ITEMS.register("pool_cue",
                        () -> new poolCueItem(new Item.Properties()));
        public static final RegistryObject<Item> TIGGER_TELL = ITEMS.register("tigger_tell",
                        () -> new tiggerTellItem(new Item.Properties()));
        public static final RegistryObject<Item> FOOTBALL_HELMET = ITEMS.register("football_helmet",
                        () -> new ArmorItem(FOOTBALLHELM_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties()));
        public static final RegistryObject<Item> BUBBLEGUM_BALL = ITEMS.register("bubblegum_ball",
                        () -> new bubblegumBallItem(new Item.Properties().food(coolDownstairsFoods.BUBBLEGUM_BALL)));
        public static final RegistryObject<Item> ROLLER_SKATES = ITEMS.register("roller_skates",
                        () -> new ArmorItem(ROLLERSKATE_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties()));
        public static final RegistryObject<Item> HEADPHONES = ITEMS.register("headphones",
                        () -> new ArmorItem(DEALMAKER_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties()));
        public static final RegistryObject<Item> GAME_COMPONENT = ITEMS.register("game_component",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> CHICKEN_SHANK = ITEMS.register("chicken_shank",
                        () -> new Item(new Item.Properties().food(coolDownstairsFoods.CHICKEN_SHANK)));
        public static void register(IEventBus eventbus) {
                ITEMS.register(eventbus);
        }

        public static void init() {
                DEALMAKER_MATERIAL.setRepairMaterial(Ingredient.of(Items.IRON_INGOT));
        }
}
