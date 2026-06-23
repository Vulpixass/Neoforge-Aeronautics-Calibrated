package net.vulpixass.aerocali.content.model;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;
import net.vulpixass.aerocali.AeronauticsCalibrated;

import static net.vulpixass.aerocali.AeronauticsCalibrated.MOD_ID;

public class PartialModels {
    // Defines the Animated Models (Partial Models)
    public static final PartialModel TRACKER_BOWLS = PartialModel.of(ResourceLocation.tryBuild(MOD_ID, "block/nav_tracker_bowls"));
    public static final PartialModel MECHANICAL_ANVIL_HEAD = PartialModel.of(ResourceLocation.tryBuild(MOD_ID, "block/mechanical_anvil_head"));

    public static final PartialModel SURVIVAL_STAFF_RING = PartialModel.of(ResourceLocation.tryBuild(MOD_ID, "item/survival_physics_staff/ring"));
    public static final PartialModel SURVIVAL_STAFF_SIGMA = PartialModel.of(ResourceLocation.tryBuild(MOD_ID, "item/survival_physics_staff/sigma"));

    public static void init() {}
}

