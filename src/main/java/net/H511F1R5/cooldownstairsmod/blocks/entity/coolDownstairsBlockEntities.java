package net.H511F1R5.cooldownstairsmod.blocks.entity;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.H511F1R5.cooldownstairsmod.blocks.coolDownstairsBlocks;
import net.H511F1R5.cooldownstairsmod.blocks.entity.billiardGameTableBlockEntity;
import net.minecraftforge.eventbus.api.IEventBus; 
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class coolDownstairsBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, coolDownstairsMod.MOD_ID);
    
    public static final RegistryObject<BlockEntityType<billiardGameTableBlockEntity>> BILLIARD_GAME_TABLE_BE = 
        BLOCK_ENTITIES.register("billiard_game_table_be", () -> 
            BlockEntityType.Builder.of(billiardGameTableBlockEntity::new, coolDownstairsBlocks.BILLIARD_GAME_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<gumballTowerBlockEntity>> GUMBALL_TOWER_BE = 
        BLOCK_ENTITIES.register("gumball_tower_be", () ->
            BlockEntityType.Builder.of(gumballTowerBlockEntity::new, coolDownstairsBlocks.GUMBALL_TOWER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    
}
