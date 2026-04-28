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

public class AerocaliItemProperties {

    public static void register() {
        ItemProperties.register(
                AerocaliItems.NAVIGATION_ELEMENT.get(),
                ResourceLocation.tryBuild(AeronauticsCalibrated.MOD_ID, "nav_angle"),
                AerocaliItemProperties::computeAngle
        );
    }

    private static float computeAngle(ItemStack stack, Level level, LivingEntity entity, int seed) {
        if (entity == null || level == null) return 0f;

        NavTargetData data = stack.get(AerocaliDataComponents.NAV_TARGET.get());
        if (data == null) return 0f;

        // Wrong dimension? Return random wobble like vanilla compass
        if (!level.dimension().location().toString().equals(data.dimension())) {
            return (entity.tickCount + seed) % 32;
        }

        double dx = data.x() + 0.5 - entity.getX();
        double dz = data.z() + 0.5 - entity.getZ();

        double angle = Math.atan2(dz, dx); // radians
        angle = angle < 0 ? angle + (Math.PI * 2) : angle;

        // Convert radians to 0–32 stepped frame index
        float frame = (float)(angle / (Math.PI * 2) * 32.0);

        return frame;
    }
}
