package net.vulpixass.aerocali.content.block.render.generator;

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
import net.minecraft.world.level.block.state.BlockState;
import net.vulpixass.aerocali.content.block.custom.generator.GeneratorBlock;
import net.vulpixass.aerocali.content.block.custom.generator.GeneratorBlockEntity;

public class GeneratorRenderer extends KineticBlockEntityRenderer<GeneratorBlockEntity> {
    public GeneratorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(GeneratorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = be.getBlockState();
        Direction facing = state.getValue(GeneratorBlock.FACING);
        Direction.Axis axis = facing.getAxis();

        SuperByteBuffer shaft = CachedBuffers.partial(AllPartialModels.SHAFT, state);

        float speed = be.getSpeed();
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle = (time * speed * 3f / 10f) % 360;
        float offset = getRotationOffsetForPosition(be, be.getBlockPos(), axis);
        float finalAngle = angle + offset;

        shaft.center();

        if (axis == Direction.Axis.X) {
            shaft.rotateZDegrees(-90);
            shaft.rotateYDegrees(finalAngle);
        }
        else if (axis == Direction.Axis.Z) {
            shaft.rotateXDegrees(90);
            shaft.rotateYDegrees(finalAngle);
        }
        else {
            shaft.rotateYDegrees(finalAngle);
        }

        shaft.uncenter();
        shaft.light(light).renderInto(ms, buffer.getBuffer(RenderType.cutout()));
    }

    @Override
    protected SuperByteBuffer getRotatedModel(GeneratorBlockEntity be, BlockState state) {
        Direction.Axis axis = state.getValue(GeneratorBlock.FACING).getAxis();
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT, state,
                Direction.get(Direction.AxisDirection.POSITIVE, axis));
    }
}
