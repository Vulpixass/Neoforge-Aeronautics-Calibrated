package net.vulpixass.aerocali.content.item.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.simulated_team.simulated.SimulatedClient;
import dev.simulated_team.simulated.content.physics_staff.PhysicsStaffClientHandler;
import dev.simulated_team.simulated.index.SimPartialModels;
import dev.simulated_team.simulated.util.SimDistUtil;
import dev.simulated_team.simulated.util.SimMathUtils;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.vulpixass.aerocali.content.model.PartialModels;
import net.vulpixass.aerocali.util.JOMLConversion;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class SurvivalPhysicsStaffRenderer extends CustomRenderedItemModelRenderer {
    public SurvivalPhysicsStaffRenderer() {
        System.out.println("SurvivalPhysicsStaffRenderer CONSTRUCTED");
    }

    private static final Vector3d focusPos = new Vector3d();
    private static final Matrix4f itemProjMat = new Matrix4f();

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext context, PoseStack ms,
                          MultiBufferSource buffer, int light, int overlay) {
        System.out.println("RENDERING!");
        float openAmount = 0.0F;
        float cubeScale = 0.0F;

        PhysicsStaffClientHandler clientHandler = SimulatedClient.PHYSICS_STAFF_CLIENT_HANDLER;
        Minecraft minecraft = Minecraft.getInstance();
        float partialTicks = AnimationTickHolder.getPartialTicks();
        Player player = SimDistUtil.getClientPlayer();

        // Only use the local client handler values (no beams access)
        if (player != null && (context.firstPerson() || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {

            openAmount = Mth.lerp(partialTicks, clientHandler.previousExtension, clientHandler.extension);
            cubeScale = Mth.lerp(partialTicks, clientHandler.previousCubeScale, clientHandler.cubeScale);
        }

        float tiltAmount = Mth.lerp(partialTicks, clientHandler.previousTilt, clientHandler.tilt);
        Quaternionf utilQuat = new Quaternionf();

        if (context.firstPerson()) {
            if (clientHandler.getDragSession() != null) {
                PhysicsStaffClientHandler.ClientDragSession dragSession = clientHandler.getDragSession();
                Quaternionf rotation = minecraft.gameRenderer.getMainCamera().rotation();

                // Use your JOMLConversion to get a Vec3, then to JOML
                Vector3d globalAnchor = JOMLConversion.toJOML(((Camera) minecraft.gameRenderer.getMainCamera()).getPosition().add(JOMLConversion
                        .toMojang(dragSession.dragLocalAnchor())));

                Vector3d dirToAnchor = globalAnchor.sub(JOMLConversion.toJOML(player.getEyePosition(partialTicks))).normalize();

                rotation.transformInverse(dirToAnchor);
                Quaternionf quat = SimMathUtils.getQuaternionfFromVectorRotation(new Vector3d(0.0F, 0.0F, -1.0F), dirToAnchor);
                ms.mulPose(utilQuat.identity().rotateY(-(float) Math.PI / 2F));
                ms.mulPose(quat.slerp(utilQuat.identity(), 0.6F));
                ms.mulPose(utilQuat.identity().rotateY((float) Math.PI / 2F));
            }

            float tiltMultiplier = context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND ? -1.0F : 1.0F;
            ms.mulPose(utilQuat.identity().rotateZ(
                    (float) Math.toRadians(((double) tiltAmount * 0.5F + 0.5F) * -61.0F) * tiltMultiplier
            ));
        }

        // Base + core from Simulated
        renderer.render(model.getOriginalModel(), light);
        renderer.renderSolidGlowing(SimPartialModels.PHYSICS_STAFF_CORE.get(), 15728880);
        renderer.renderGlowing(SimPartialModels.PHYSICS_STAFF_CORE_GLOW.get(), 15728880);

        // Ring (your partial)
        ms.pushPose();
        ms.translate(0.0F, 0.40625F, 0.0F);
        renderer.render(PartialModels.SURVIVAL_STAFF_RING.get(), light);
        ms.popPose();

        // Sigma (your partial)
        ms.translate(0.0F, 0.5625F, 0.0F);
        for (int i = 0; i < 2; ++i) {
            ms.pushPose();
            ms.mulPose(Axis.YP.rotationDegrees(i * 180));
            ms.translate(-0.1875F, 0.0F, 0.0F);
            ms.mulPose(Axis.ZP.rotationDegrees(openAmount * 20.0F));
            renderer.render(PartialModels.SURVIVAL_STAFF_SIGMA.get(), light);
            ms.popPose();
        }

        // Cube (Simulated partials, your behavior)
        ms.translate(0.0F, 0.375F, 0.0F);
        if (context.firstPerson()) {
            if (clientHandler.getDragSession() != null) {
                clientHandler.lastCubeOrientation.set(clientHandler.getDragSession().dragOrientation());
            }

            Matrix4f m = new Matrix4f(ms.last().pose());
            m.m30(0.0F).m31(0.0F).m32(0.0F);
            m.invert();
            m.rotate(clientHandler.lastCubeOrientation);
            ms.mulPose(m);
        }

        cubeScale = Mth.lerp(cubeScale, -0.05F, 1.0F);
        cubeScale = Mth.clamp(cubeScale, 0.0F, 1.0F);
        cubeScale *= 0.8F;
        ms.scale(cubeScale, cubeScale, cubeScale);

        renderer.renderSolidGlowing(SimPartialModels.PHYSICS_STAFF_INNER_CUBE.get(), 15728880);

        if (context.firstPerson()) {
            Vector3f focusPoint = new Vector3f();
            ms.last().pose().transformPosition(focusPoint);
            itemProjMat.set(RenderSystem.getProjectionMatrix());
            focusPos.set(focusPoint.x, focusPoint.y, focusPoint.z);
        }

        ms.scale(1.2F, 1.2F, 1.2F);
        renderer.renderGlowing(SimPartialModels.PHYSICS_STAFF_OUTER_CUBE.get(), 15728880);
    }
}
