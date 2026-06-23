package net.vulpixass.aerocali.content.block.render.mechanical_anvil;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.vulpixass.aerocali.content.block.custom.mechanical_anvil.MechanicalAnvilBlock;
import net.vulpixass.aerocali.content.block.custom.mechanical_anvil.MechanicalAnvilBlockEntity;
import net.vulpixass.aerocali.content.model.PartialModels;

public class MechanicalAnvilRenderer extends KineticBlockEntityRenderer<MechanicalAnvilBlockEntity> {
    private static final float smoothing = 0.2f;

    public MechanicalAnvilRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(MechanicalAnvilBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = be.getBlockState();

        // Render horizontal shafts
        for (Direction dir : Direction.Plane.HORIZONTAL) {

            SuperByteBuffer shaft = CachedBuffers.partial(AllPartialModels.SHAFT, state);

            float speed = be.getSpeed();
            float time = AnimationTickHolder.getRenderTime(be.getLevel());
            float angle = (time * speed * 3f / 10f) % 360;
            float offset = getRotationOffsetForPosition(be, be.getBlockPos(), dir.getAxis());
            float finalAngle = angle + offset;

            shaft.center();

            if (dir.getAxis() == Direction.Axis.X) {
                shaft.rotateZDegrees(-90);
                shaft.rotateYDegrees(finalAngle);
            }
            else if (dir.getAxis() == Direction.Axis.Z) {
                shaft.rotateXDegrees(90);
                shaft.rotateYDegrees(finalAngle);
            }
            else {
                shaft.rotateYDegrees(finalAngle);
            }

            shaft.uncenter().light(light).renderInto(ms, buffer.getBuffer(RenderType.cutout()));
        }

        // Render the anvil head
        float rpm = Math.abs(be.getSpeed());
        float target = Mth.clamp(rpm / 128f, 0f, 1f) * 0.35f;

        be.clientHeight = be.clientHeight + (target - be.clientHeight) * smoothing;
        float y = be.clientHeight;

        CachedBuffers.partial(PartialModels.MECHANICAL_ANVIL_HEAD, state).translate(0, y, 0).light(light).renderInto(ms, buffer.getBuffer(RenderType.cutout()));
    }

}
