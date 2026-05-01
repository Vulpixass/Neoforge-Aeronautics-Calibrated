package net.vulpixass.aerocali.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.vulpixass.aerocali.content.block.GeneratorBlockEntity;
import net.vulpixass.aerocali.content.sound.GeneratorSoundInstance;
import net.vulpixass.aerocali.content.ui.NavInputScreen;

public class ClientAccess {
    public static void openNavInputScreen(ItemStack stack) {
        Minecraft.getInstance().setScreen(new NavInputScreen(stack));
    }

    public static void manageSound(GeneratorBlockEntity be) {
        float speed = Math.abs(be.getSpeed());

        if (speed > 0.01f && be.generatorSound == null) {
            GeneratorSoundInstance sound = new GeneratorSoundInstance(be);
            net.minecraft.client.Minecraft.getInstance().getSoundManager().play(sound);
            be.generatorSound = sound;
        }
        else if (be.generatorSound instanceof GeneratorSoundInstance sound && sound.isStopped()) {
            be.generatorSound = null;
        }
    }
}
