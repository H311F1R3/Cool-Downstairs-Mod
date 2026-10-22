package net.H511F1R5.cooldownstairsmod.datagen;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = coolDownstairsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class dataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        dataGenerator.addProvider(event.includeServer(), new coolDownstairsRecipeProvider(packOutput));
        dataGenerator.addProvider(event.includeServer(), coolDownstairsLootTableProvider.create(packOutput));

        dataGenerator.addProvider(event.includeClient(), new coolDownstairsBlockStateProvider(packOutput, existingFileHelper));
        dataGenerator.addProvider(event.includeClient(), new coolDownstairsItemModelProvider(packOutput, existingFileHelper));

        coolDownstairsBlockTagGenerator blockTagGenerator = dataGenerator.addProvider(event.includeServer(),
                new coolDownstairsBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
        dataGenerator.addProvider(event.includeServer(), new coolDownstairsItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
    }

}
