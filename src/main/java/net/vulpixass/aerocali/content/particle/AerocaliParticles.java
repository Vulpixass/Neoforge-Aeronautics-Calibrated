package net.vulpixass.aerocali.content.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vulpixass.aerocali.AeronauticsCalibrated;

import java.util.function.Supplier;

public class AerocaliParticles {
    // Registers/Defines the unused custom particles
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, AeronauticsCalibrated.MOD_ID);

    public static final Supplier<SimpleParticleType> THRUST_PARTICLES =
            PARTICLE_TYPES.register("thrust_particles", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
