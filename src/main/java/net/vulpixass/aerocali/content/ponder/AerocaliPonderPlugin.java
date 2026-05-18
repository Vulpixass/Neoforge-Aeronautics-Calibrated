package net.vulpixass.aerocali.content.ponder;

import com.simibubi.create.foundation.ponder.CreatePonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static net.vulpixass.aerocali.AeronauticsCalibrated.MOD_ID;

public class AerocaliPonderPlugin extends CreatePonderPlugin {
    @Override
    public String getModId() {
        return MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> registry) {
        // When ready registers the Ponder Scenes
        PonderScenes.register(registry);
    }
}
