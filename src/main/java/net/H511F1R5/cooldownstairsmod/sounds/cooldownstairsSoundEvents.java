package net.H511F1R5.cooldownstairsmod.sounds;

import net.H511F1R5.cooldownstairsmod.coolDownstairsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class cooldownstairsSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, coolDownstairsMod.MOD_ID);

    public static final RegistryObject<SoundEvent> DUMMY_FACE_USE = registerSoundEvents("dummy_face_use");

    public static final RegistryObject<SoundEvent> DEALMAKER_EQUIP_SOUND =
            SOUND_EVENTS.register("dealmaker_equip",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(coolDownstairsMod.MOD_ID, "dealmaker_equip")));
    public static final RegistryObject<SoundEvent> FOOTBALLHELM_EQUIP_SOUND = registerSoundEvents("footballhelm_equip");
    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(coolDownstairsMod.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

