package net.vulpixass.aerocali.content.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vulpixass.aerocali.AeronauticsCalibrated;

import java.util.Objects;

public class AerocaliSounds {
    // Registers all the Sounds
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, AeronauticsCalibrated.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> THRUSTER =
            register("thruster");
    public static final DeferredHolder<SoundEvent, SoundEvent> THRUSTER_ION =
            register("thruster_ion");
    public static final DeferredHolder<SoundEvent, SoundEvent> THRUSTER_START =
            register("thruster_start");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(
                Objects.requireNonNull(ResourceLocation.tryBuild(AeronauticsCalibrated.MOD_ID, name))
        ));
    }
}
