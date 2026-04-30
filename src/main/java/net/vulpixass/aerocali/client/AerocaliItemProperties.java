package net.vulpixass.aerocali.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vulpixass.aerocali.AeronauticsCalibrated;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

import java.util.Objects;

public class AerocaliItemProperties {
    private static float res = 0;

    public static void register() {
        ItemProperties.register(AerocaliItems.NAVIGATION_ELEMENT.get(),
                Objects.requireNonNull(ResourceLocation.tryBuild(AeronauticsCalibrated.MOD_ID, "nav_angle")), AerocaliItemProperties::computeAngle);
    }

    private static float computeAngle(ItemStack stack, Level level, LivingEntity entity, int seed) {
        if (entity == null || level == null) return 0f;

        NavTargetData data = AerocaliDataComponents.NAV_TARGET_DATA.get(stack);
        if (data == null) return 0f;

        if (!level.dimension().location().toString().equals(data.dimension())) {
            return (entity.tickCount + seed) % 32;
        }

        double dx = data.x() + 0.5 - entity.getX();
        double dz = data.z() + 0.5 - entity.getZ();

        double angle = Math.atan2(dz, dx) / (Math.PI * 2);
        double rotation = entity.getViewYRot(1.0F) / 360.0;

        angle = angle + 0.5;

        return (float) Math.floorMod((long) ((angle - (rotation - 0.25)) * 32.0), 32);
    }
}
