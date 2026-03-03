package net.H511F1R5.cooldownstairsmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class effectBigShot extends MobEffect {

    public effectBigShot() {
        super(MobEffectCategory.BENEFICIAL, 0X36BA4C);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }

    public void applyEffectTick(LivingEntity livingEnt, int amplifier) {

    }

}
