package net.vulpixass.aerocali.content.item.custom;

import dev.simulated_team.simulated.index.SimDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vulpixass.aerocali.AeronauticsCalibrated;
import net.vulpixass.aerocali.client.ClientAccess;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.compat.NavTarget;
import net.vulpixass.aerocali.content.ui.NavInputScreen;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

public class NavigationElementItem extends Item {

    public NavigationElementItem(Properties properties) {
        super(properties);
    }

    public boolean isFoil(ItemStack stack) {
        return stack.has(AerocaliDataComponents.NAV_TARGET) || super.isFoil(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide) return;

        NavTargetData data = stack.get(AerocaliDataComponents.NAV_TARGET.get());
        if (data != null) stack.set(AerocaliDataComponents.NAV_TARGET.get(), data);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        NavTargetData data = AerocaliDataComponents.NAV_TARGET_DATA.get(stack);
        if (level.isClientSide) {
            if (player.isShiftKeyDown()) {
                ClientAccess.openNavInputScreen(stack);
            }

            return InteractionResultHolder.sidedSuccess(stack, true);
        } else {
            if (player.isShiftKeyDown() && data != null) {
                NavTargetData newData = new NavTargetData(data.x(), data.y(), data.z(), "overworld");
                stack.set(AerocaliDataComponents.NAV_TARGET.get(), newData);
                stack.set(SimDataComponents.TARGET, AeronauticsCalibrated.AEROCALI_NAV_BRIDGE);
            }

            return InteractionResultHolder.sidedSuccess(stack, false);
        }
    }
}
