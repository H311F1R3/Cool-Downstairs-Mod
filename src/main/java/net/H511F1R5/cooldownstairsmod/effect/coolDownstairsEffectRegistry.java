package net.H511F1R5.cooldownstairsmod.effect;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class coolDownstairsEffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECT_DEF_REG = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, coolDownstairsMod.MOD_ID);
    public static final DeferredRegister<Potion> POTION_DEF_REG = DeferredRegister.create(ForgeRegistries.POTIONS, coolDownstairsMod.MOD_ID);

    public static final RegistryObject<MobEffect> BIG_SHOT = EFFECT_DEF_REG.register("big_shot", effectBigShot::new);
}
