package net.H511F1R5.cooldownstairsmod.sounds;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.sounds.SoundSource;
import net.minecraft.client.sounds.WeighedSoundEvents;

public class AmplifiedSoundInstance implements SoundInstance {
    private final SoundInstance original;
    private final float volumeBonus;

    public AmplifiedSoundInstance(SoundInstance original, float volumeBonus) {
        this.original = original;
        this.volumeBonus = volumeBonus;
    }

    @Override
    public ResourceLocation getLocation() {
        return original.getLocation();
    }

    @Override
    public WeighedSoundEvents resolve(SoundManager manager) {
        return original.resolve(manager);
    }

    @Override
    public Sound getSound() {
        return original.getSound();
    }

    @Override
    public SoundSource getSource() {
        return original.getSource();
    }

    @Override
    public boolean isLooping() {
        return original.isLooping();
    }

    @Override
    public boolean isRelative() {
        return original.isRelative();
    }

    @Override
    public int getDelay() {
        return original.getDelay();
    }

    @Override
    public float getVolume() {
        return original.getVolume() + volumeBonus;
    }

    @Override
    public float getPitch() {
        return original.getPitch();
    }

    @Override
    public double getX() {
        return original.getX();
    }

    @Override
    public double getY() {
        return original.getY();
    }

    @Override
    public double getZ() {
        return original.getZ();
    }

    @Override
    public SoundInstance.Attenuation getAttenuation() {
        return original.getAttenuation();
    }
}
