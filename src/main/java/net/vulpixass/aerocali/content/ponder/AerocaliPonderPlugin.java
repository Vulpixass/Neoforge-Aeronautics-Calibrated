package net.vulpixass.aerocali.content.ponder;

import com.simibubi.create.foundation.ponder.CreatePonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class AerocaliPonderPlugin extends CreatePonderPlugin {
    @Override
    public String getModId() {
        return "aerocali"; // Must exactly match your mod's lowercase ID
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> registry) {
        // Hand the system-provided helper off to your scene class
        PonderScenes.register(registry);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        // Keep empty for now if you aren't grouping by tabs yet
    }
}
