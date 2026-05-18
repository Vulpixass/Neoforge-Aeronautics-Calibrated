package net.vulpixass.aerocali.content.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vulpixass.aerocali.client.ClientAccess;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

public class NavigationElementItem extends Item {

    public NavigationElementItem(Properties properties) {
        super(properties);
    }

    public boolean isFoil(ItemStack stack) {
        // Makes the Item be shiny and have an enchantment Glint if it has a Target
        return stack.has(AerocaliDataComponents.NAV_TARGET) || super.isFoil(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        // Updates the inventory constantly so the compass always points according to the players rotation
        if (level.isClientSide) return;

        NavTargetData data = stack.get(AerocaliDataComponents.NAV_TARGET.get());
        if (data != null) stack.set(AerocaliDataComponents.NAV_TARGET.get(), data);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        NavTargetData data = AerocaliDataComponents.NAV_TARGET_DATA.get(stack);

        // Opens the Coordinate input menu
        if (level.isClientSide) {
            if (player.isShiftKeyDown()) {
                ClientAccess.openNavInputScreen(stack);
            }
            return InteractionResultHolder.sidedSuccess(stack, true);
        }

        // sets the Target
        if (player.isShiftKeyDown() && data != null) {
            stack.set(AerocaliDataComponents.NAV_TARGET.get(), new NavTargetData(data.x(), data.y(), data.z(), "overworld"));
        }
        return InteractionResultHolder.sidedSuccess(stack, false);
    }

}
