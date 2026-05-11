package net.vulpixass.aerocali.content.item.render.nav_tracker;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.vulpixass.aerocali.content.block.AerocaliBlocks;
import net.vulpixass.aerocali.content.model.PartialModels;

public class NavigationTrackerItemRenderer extends BlockEntityWithoutLevelRenderer {
    public NavigationTrackerItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }
    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        // 1. Render the Base (The model you actually HAVE)
        BlockState state = AerocaliBlocks.TRACKER.get().defaultBlockState();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, ms, buffer, light, overlay);

        // 2. Render the "Ghost" parts (Create's models)
        // Since we are in code, we CAN access Create's shaft even if we don't have the file!
        float time = AnimationTickHolder.getRenderTime();
        float angle = (time * 0.5f) % 360;

        // Render the Shaft
        ms.pushPose();
        renderPart(AllPartialModels.SHAFT, state, angle, 0.55f, 1.0f, ms, buffer, light);

        // Render the Cog (The Create one you found!)
        renderPart(AllPartialModels.COGWHEEL, state, angle, 0, 0.5f, ms, buffer, light);

        // Render your Bowls (The one you MADE)
        renderPart(PartialModels.TRACKER_BOWLS, state, angle, 1.05f, 1.0f, ms, buffer, light);
        ms.popPose();
    }

    private void renderPart(PartialModel model, BlockState state, float angle, float y, float scale, PoseStack ms, MultiBufferSource buffer, int light) {
        CachedBuffers.partial(model, state)
                .center().rotateYDegrees(angle).scale(scale).uncenter()
                .translate(0, y, 0).light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.cutout()));
    }
}
