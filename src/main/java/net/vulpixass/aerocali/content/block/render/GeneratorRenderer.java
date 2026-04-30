package net.vulpixass.aerocali.content.block.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.vulpixass.aerocali.content.block.GeneratorBlock;
import net.vulpixass.aerocali.content.block.GeneratorBlockEntity;

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

        // 1. Get the spin angle
        float angle = getAngleForBe(be, be.getBlockPos(), axis);

        shaft.center()
                // 2. Spin it while it's standing up (Y-axis)
                .rotateZ(angle)

                // 3. TIP IT DOWN (The 90-degree flip you mentioned!)
                // This lays it down so it's pointing North/South
                .rotateXDegrees(90)

                // 4. Align with the block's actual facing (handle East/West/Up/Down)
                // Since we tipped it to North/South, this will now rotate it correctly to the sides
                .rotateToFace(facing)

                .uncenter();

        shaft.light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
    }
}
