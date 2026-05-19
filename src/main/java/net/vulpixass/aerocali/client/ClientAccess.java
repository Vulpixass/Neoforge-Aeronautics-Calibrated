package net.vulpixass.aerocali.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.vulpixass.aerocali.content.block.custom.generator.BaseGeneratorBlockEntity;
import net.vulpixass.aerocali.content.sound.GeneratorSoundInstance;
import net.vulpixass.aerocali.content.ui.NavInputScreen;

public class ClientAccess {
    // just opens the Navigation Compass's Menu. Like... that should be obvious right?
    public static void openNavInputScreen(ItemStack stack) {
        Minecraft.getInstance().setScreen(new NavInputScreen(stack));
    }

    // Have the generator sound be played by the block whenever active
    public static void manageSound(BaseGeneratorBlockEntity be) {
        float speed = Math.abs(be.getSpeed());

        if (speed > 0.01f && be.generatorSound == null) {
            GeneratorSoundInstance sound = new GeneratorSoundInstance(be);
            Minecraft.getInstance().getSoundManager().play(sound);
            be.generatorSound = sound;
        }
        else if (be.generatorSound instanceof GeneratorSoundInstance sound && sound.isStopped()) {
            be.generatorSound = null;
        }
    }
}
