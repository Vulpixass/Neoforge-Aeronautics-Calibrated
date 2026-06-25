package net.vulpixass.aerocali.mixin.client;

import dev.simulated_team.simulated.content.physics_staff.PhysicsStaffClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.network.packet.DamageStaffPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhysicsStaffClientHandler.class)
public class AerocaliLeftClickMixin {

    @Inject(method = "onItemPunched", at = @At("HEAD"))

    private void aerocali$damageOnLeftClick(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();

        if (stack.is(AerocaliItems.SURVIVAL_PHYSICS_STAFF.get())) {
            mc.getConnection().send(new DamageStaffPayload(25));
        }
    }
}
