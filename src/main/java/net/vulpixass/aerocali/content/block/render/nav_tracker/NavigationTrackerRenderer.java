package net.vulpixass.aerocali.content.block.render.nav_tracker;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.vulpixass.aerocali.content.block.custom.nav_tracker.NavigationTrackerBlockEntity;
import net.vulpixass.aerocali.content.model.PartialModels;

public class NavigationTrackerRenderer extends KineticBlockEntityRenderer<NavigationTrackerBlockEntity> {
    public NavigationTrackerRenderer(BlockEntityRendererProvider.Context context) {super(context);}

    @Override
    protected void renderSafe(NavigationTrackerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = be.getBlockState();
        SuperByteBuffer shaft = CachedBuffers.partial(AllPartialModels.SHAFT, state);

        float speed = be.getSpeed();
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle = (time * speed * 3f / 10f) % 360;

        float offset = getRotationOffsetForPosition(be, be.getBlockPos(), Direction.Axis.Y);
        float finalAngle = angle + offset;

        float yOffset = be.isShaftAbove() ? 0.0f : 0.55f;

        shaft.center().rotateYDegrees(finalAngle).uncenter().translate(0, yOffset, 0).light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.cutout()));

        renderRotatingPart(be, AllPartialModels.COGWHEEL, finalAngle, 0.55f, 0.9f, ms, buffer, light);
        renderRotatingPart(be, PartialModels.TRACKER_BOWLS, finalAngle, 0, 1f, ms, buffer, light);

    }

    private void renderRotatingPart(NavigationTrackerBlockEntity be, PartialModel model, float angle, float yOffset, float scale, PoseStack ms, MultiBufferSource buffer, int light) {
        CachedBuffers.partial(model, be.getBlockState()).center().rotateYDegrees(angle).scale(scale, scale, scale).uncenter()
                .translate(0, yOffset, 0).light(light).renderInto(ms, buffer.getBuffer(RenderType.cutout()));
    }



    @Override
    protected SuperByteBuffer getRotatedModel(NavigationTrackerBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT, state,
                Direction.get(Direction.AxisDirection.POSITIVE, Direction.Axis.Y));
    }
}
