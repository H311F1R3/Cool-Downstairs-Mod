package net.H511F1R5.cooldownstairsmod.datagen;

import net.H511F1R5.cooldownstairsmod.blocks.coolDownstairsBlocks;
import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class coolDownstairsBlockStateProvider extends BlockStateProvider {

    public coolDownstairsBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, coolDownstairsMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(coolDownstairsBlocks.BILLIARD_TABLE);
        blockWithItem(coolDownstairsBlocks.DDR_PAD);
        blockWithItem(coolDownstairsBlocks.SPEAKER_BLOCK);
        blockWithItem(coolDownstairsBlocks.RASTANANA);
        blockWithItem(coolDownstairsBlocks.VINYL_WOOD);

        simpleBlockWithItem(coolDownstairsBlocks.BILLIARD_GAME_TABLE.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/billiard_game_table")));
        // simpleBlockWithItem(coolDownstairsBlocks.GUMBALL_TOWER.get(),
    }

    // Thx for letting me copy ur homework Kaupenjoe ur the GOAT
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
