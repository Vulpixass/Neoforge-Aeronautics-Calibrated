package net.vulpixass.aerocali.content.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.vulpixass.aerocali.content.block.custom.generator.BaseGeneratorBlockEntity;
import net.vulpixass.aerocali.content.block.custom.generator.regular.GeneratorBlockEntity;

public class GeneratorSoundInstance extends AbstractTickableSoundInstance {
    private final BaseGeneratorBlockEntity blockEntity;

    // Define the Generator Sound (Yes it really is just bees)
    public GeneratorSoundInstance(BaseGeneratorBlockEntity blockEntity) {
        super(SoundEvents.BEE_LOOP, SoundSource.BLOCKS, blockEntity.getLevel().random);
        this.blockEntity = blockEntity;
        this.x = blockEntity.getBlockPos().getX() + 0.5f;
        this.y = blockEntity.getBlockPos().getY() + 0.5f;
        this.z = blockEntity.getBlockPos().getZ() + 0.5f;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.1f;
        this.pitch = 0.5f;
    }

    // Manage the Generators Sound so that it stops as soon as the block is broken
    @Override
    public void tick() {
        if (blockEntity.isRemoved() || Math.abs(blockEntity.getSpeed()) < 0.01f) {
            this.stop();
            return;
        }
        this.pitch = 0.4f + (Math.abs(blockEntity.getSpeed()) / 512f);
    }
}
