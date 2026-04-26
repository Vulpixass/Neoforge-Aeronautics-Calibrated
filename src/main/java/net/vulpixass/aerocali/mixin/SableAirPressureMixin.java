package net.vulpixass.aerocali.mixin;

import dev.ryanhcode.sable.physics.config.dimension_physics.DimensionPhysicsData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vulpixass.aerocali.content.block.ThrusterBlockEntity;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionPhysicsData.class)
public class SableAirPressureMixin {

    @Inject(method = "getAirPressure", at = @At("TAIL"), cancellable = true)
    private static void aerocali$overrideAirPressure(Level level, Vector3dc pos, CallbackInfoReturnable<Double> cir) {
        BlockPos bp = BlockPos.containing(pos.x(), pos.y(), pos.z());
        BlockEntity be = level.getBlockEntity(bp);

        if (!(be instanceof ThrusterBlockEntity)) return;

        double y = pos.y();
        double max = 700.0;

        double p = 1.0 - (y / max);
        p = Math.max(p, 0.05);

        // TODO: Fix this so that the Physics Object stabilizes at Y: 700 or 1000
        // BUT FOR NOW DON'T DO ANYTHING! IT WORKS OKAY?! DO NOT TOUCH!

        cir.setReturnValue(p);
    }
}
