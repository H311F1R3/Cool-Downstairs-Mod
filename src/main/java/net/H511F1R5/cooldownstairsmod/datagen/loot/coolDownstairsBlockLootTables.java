package net.H511F1R5.cooldownstairsmod.datagen.loot;

import net.H511F1R5.cooldownstairsmod.blocks.coolDownstairsBlocks;
import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class coolDownstairsBlockLootTables extends BlockLootSubProvider {

    protected coolDownstairsBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(coolDownstairsBlocks.BILLIARD_TABLE.get());
         this.dropSelf(coolDownstairsBlocks.DDR_PAD.get());
         this.dropSelf(coolDownstairsBlocks.SPEAKER_BLOCK.get());
         this.dropSelf(coolDownstairsBlocks.RASTANANA.get());
         this.dropSelf(coolDownstairsBlocks.VINYL_WOOD.get());
         this.dropSelf(coolDownstairsBlocks.BILLIARD_GAME_TABLE.get());
         this.dropSelf(coolDownstairsBlocks.GUMBALL_TOWER.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return coolDownstairsBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    } // all blocks that will have loot tables will be from the cdBlocks Registry
}
