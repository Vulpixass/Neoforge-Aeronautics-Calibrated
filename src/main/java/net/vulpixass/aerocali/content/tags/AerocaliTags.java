package net.vulpixass.aerocali.content.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import static net.vulpixass.aerocali.AeronauticsCalibrated.MOD_ID;

public class AerocaliTags {

    public static class Fluids {
        public static final TagKey<Fluid> THRUSTER_FUEL = TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(MOD_ID, "thruster_fuel"));
        public static void register() {}
    }

}