package net.H511F1R5.cooldownstairsmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class effectChainBleed extends MobEffect {

    protected effectChainBleed() {
        super(MobEffectCategory.HARMFUL, 0XD32C0E);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // Trigger damage exactly every 20 ticks (1 second)
        // If duration is 60, 40, 20, it will tick.
        return duration % 20 == 0 && duration <= 60;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEnt, int amplifier) {
        net.minecraft.world.effect.MobEffectInstance instance = livingEnt.getEffect(this);
        if (instance != null) {
            int duration = instance.getDuration();
            
            // For a 3 second duration (60 ticks):
            // at duration 60 -> elapsed index = 0 -> damage = 1
            // at duration 40 -> elapsed index = 1 -> damage = 2
            // at duration 20 -> elapsed index = 2 -> damage = 4
            int elapsedSeconds = 3 - (duration / 20);
            if (elapsedSeconds < 0) elapsedSeconds = 0;

            float baseDamage = (float) Math.pow(2, elapsedSeconds);
            int armorValue = livingEnt.getArmorValue();
            
            // Scales with the protection amount of equipped armor multiplicatively
            float finalDamage = baseDamage * (1.0f + (armorValue * 0.25f));

            // Apply magic damage
            livingEnt.hurt(livingEnt.damageSources().magic(), finalDamage);
        }
    }
}
