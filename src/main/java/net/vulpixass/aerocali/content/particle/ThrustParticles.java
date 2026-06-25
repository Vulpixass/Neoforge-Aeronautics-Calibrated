package net.vulpixass.aerocali.content.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class ThrustParticles extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    // Defines the custom (unused) regular Thruster Particles
    protected ThrustParticles(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.spriteSet = spriteSet;
        this.friction = 1.1f;
        this.lifetime = 20;
        this.setSpriteFromAge(spriteSet);
        this.quadSize *= 2.5f;
        this.scale(2.5f);
        this.tick();
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(spriteSet); // Progresses animation frames based on age
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double pX,
                                                 double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new ThrustParticles(clientLevel, pX, pY, pZ,this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
        }

    }
}
