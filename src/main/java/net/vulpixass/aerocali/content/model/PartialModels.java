package net.vulpixass.aerocali.content.model;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;
import net.vulpixass.aerocali.AeronauticsCalibrated;

import static net.vulpixass.aerocali.AeronauticsCalibrated.MOD_ID;

public class PartialModels {
    // Defines the Tracker Wind Bowls (Partial) Model
    public static final PartialModel TRACKER_BOWLS = PartialModel.of(ResourceLocation.tryBuild(MOD_ID, "block/nav_tracker_bowls"));

    public static void init() {}
}

