package net.vulpixass.aerocali.mixin.common;

import dev.simulated_team.simulated.content.physics_staff.PhysicsStaffAction;
import dev.simulated_team.simulated.content.physics_staff.PhysicsStaffServerHandler;
import dev.simulated_team.simulated.network.packets.physics_staff.PhysicsStaffActionPacket;
import dev.simulated_team.simulated.network.packets.physics_staff.PhysicsStaffBeamPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.util.JOMLConversion;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import foundry.veil.api.network.handler.PacketContext;

import java.util.UUID;

@Mixin(PhysicsStaffActionPacket.class)
public abstract class AerocaliPhysicsStaffActionPacketMixin {

    @Shadow @Final protected PhysicsStaffAction action;

    @Shadow @Final protected Vector3d location;

    @Shadow @Final protected UUID subLevel;

    @Inject(method = "handle", at = @At("HEAD"), cancellable = true)
    private void aerocali$allowCustomStaff(PacketContext context, CallbackInfo ci) {
        Player player = context.player();

        boolean holdingCustom = player.getMainHandItem().is(AerocaliItems.SURVIVAL_PHYSICS_STAFF.get()) ||
                player.getOffhandItem().is(AerocaliItems.SURVIVAL_PHYSICS_STAFF.get());

        //CREDIT TO AERONAUTICS: SIMULATED FOR ALL THE LOWER CODE!!!! I DID NOT MAKE THIS!
        if (holdingCustom) {
            ServerLevel level = (ServerLevel) context.level();

            if (this.action == PhysicsStaffAction.LOCK) {
                PhysicsStaffServerHandler.get(level).toggleLock(this.subLevel);
            }

            if (this.action == PhysicsStaffAction.STOP_DRAG) {
                PhysicsStaffServerHandler.get(level).stopDragging(player.getUUID());
            }

            if (this.action == PhysicsStaffAction.LOCK) {
                Vector3d beamStart = JOMLConversion.toJOML(player.getEyePosition());
                Vector3d beamEnd = new Vector3d(this.location);
                ChunkPos chunk = new ChunkPos(BlockPos.containing(this.location.x(), this.location.y(), this.location.z()));
                ClientboundCustomPayloadPacket beamPacket =
                        new ClientboundCustomPayloadPacket(new PhysicsStaffBeamPacket(player.getUUID(), beamStart, beamEnd));

                for (ServerPlayer otherPlayer : level.getChunkSource().chunkMap.getPlayers(chunk, false)) {
                    if (otherPlayer != player) {
                        otherPlayer.connection.send(beamPacket);
                    }
                }
            }

            ci.cancel();
        }
    }
}
