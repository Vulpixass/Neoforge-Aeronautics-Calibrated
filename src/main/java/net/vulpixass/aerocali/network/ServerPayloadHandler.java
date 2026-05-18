package net.vulpixass.aerocali.network;

import dev.simulated_team.simulated.index.SimDataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vulpixass.aerocali.AeronauticsCalibrated;
import net.vulpixass.aerocali.content.item.custom.NavigationElementItem;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

public class ServerPayloadHandler {
    public static void handleNavUpdate(final NavUpdatePayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

            // Set the Items data if it is a Navigation Compass
            if (stack.getItem() instanceof NavigationElementItem) {
                NavTargetData newData = new NavTargetData((int) data.x(), (int) data.y(), (int) data.z(), "minecraft:overworld");
                stack.set(AerocaliDataComponents.NAV_TARGET.get(), newData);
                stack.set(SimDataComponents.TARGET, AeronauticsCalibrated.AEROCALI_NAV_BRIDGE);
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LODESTONE_COMPASS_LOCK,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        });
    }
}
