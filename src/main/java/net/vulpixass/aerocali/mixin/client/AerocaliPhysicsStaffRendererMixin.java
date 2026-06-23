package net.vulpixass.aerocali.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.simulated_team.simulated.content.physics_staff.PhysicsStaffItemRenderer;
import dev.simulated_team.simulated.index.SimPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.content.model.PartialModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PhysicsStaffItemRenderer.class)
public abstract class AerocaliPhysicsStaffRendererMixin {

    @WrapOperation(method = "render", at = @At(value = "INVOKE",
            target = "Lcom/simibubi/create/foundation/item/render/PartialItemModelRenderer;render(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/client/renderer/RenderType;I)V"))
    //Swap the Ring and Sigma of the Staff to use the correct textures
    private void aerocali$swapRingAndSigma(PartialItemModelRenderer renderer, BakedModel model, RenderType renderType, int light, Operation<Void> original, ItemStack stack) {
        if (!stack.is(AerocaliItems.SURVIVAL_PHYSICS_STAFF.get())) {
            original.call(renderer, model, renderType, light);
            return;
        }

        // Replace ring
        if (model == SimPartialModels.PHYSICS_STAFF_RING.get()) {
            original.call(renderer, PartialModels.SURVIVAL_STAFF_RING.get(), renderType, light);
            return;
        }

        // Replace sigma
        if (model == SimPartialModels.PHYSICS_STAFF_SIGMA.get()) {
            original.call(renderer, PartialModels.SURVIVAL_STAFF_SIGMA.get(), renderType, light);
            return;
        }

        original.call(renderer, model, renderType, light);
    }
}

