package net.H511F1R5.cooldownstairsmod.blocks;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.H511F1R5.cooldownstairsmod.blocks.custom.*;
import net.H511F1R5.cooldownstairsmod.item.coolDownstairsItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class coolDownstairsBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
                        coolDownstairsMod.MOD_ID);

        public static final RegistryObject<Block> BILLIARD_TABLE = registerBlock("billiard_table",
                        () -> new billiardTableBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
        public static final RegistryObject<Block> SPEAKER_BLOCK = registerBlock("speaker_block",
                        () -> new SpeakerBlock(BlockBehaviour.Properties.copy(Blocks.JUKEBOX)));
        public static final RegistryObject<Block> DDR_PAD = registerBlock("ddr_pad",
                        () -> new ddrPad(BlockBehaviour.Properties.copy(Blocks.WHITE_CARPET).noOcclusion()));
        public static final RegistryObject<Block> RASTANANA = registerBlock("rastanana",
                        () -> new rastananaBlock(BlockBehaviour.Properties.copy(Blocks.YELLOW_WOOL).noOcclusion()
                                        .noCollission()));
        public static final RegistryObject<Block> GUMBALL_TOWER = registerBlock("gumball_tower",
                        () -> new gumballTowerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
        public static final RegistryObject<Block> BILLIARD_GAME_BLOCK = registerBlock("billiard_game_block",
                        () -> new billiardGameBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS).noOcclusion()));
        public static final RegistryObject<Block> VINYL_WOOD = registerBlock("vinyl_wood",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));

        private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
                RegistryObject<T> toReturn = BLOCKS.register(name, block);
                registerBlockItem(name, toReturn);
                return toReturn;
        }

        private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
                return coolDownstairsItems.ITEMS.register(name,
                                () -> new BlockItem(block.get(), new Item.Properties()));
        }

        public static void register(IEventBus eventBus) {
                BLOCKS.register(eventBus);
        }
}
