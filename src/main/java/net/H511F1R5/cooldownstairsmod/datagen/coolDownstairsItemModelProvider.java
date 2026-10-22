package net.H511F1R5.cooldownstairsmod.datagen;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.H511F1R5.cooldownstairsmod.item.coolDownstairsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class coolDownstairsItemModelProvider extends ItemModelProvider {
    public coolDownstairsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, coolDownstairsMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(coolDownstairsItems.DEALMAKER);
        simpleItem(coolDownstairsItems.CHICKEN_SHANK);
        simpleItem(coolDownstairsItems.DUMMY_FACE);
        simpleItem(coolDownstairsItems.FOOTBALL_HELMET);
        simpleItem(coolDownstairsItems.GAME_COMPONENT);
        simpleItem(coolDownstairsItems.HEADPHONES);
        simpleItem(coolDownstairsItems.TIGGER_TELL);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(coolDownstairsMod.MOD_ID,"item/" + item.getId().getPath()));
    }
}
