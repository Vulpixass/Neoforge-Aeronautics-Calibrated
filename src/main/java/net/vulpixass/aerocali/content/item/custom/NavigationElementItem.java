package net.vulpixass.aerocali.content.item.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vulpixass.aerocali.content.ui.NavInputScreen;

public class NavigationElementItem extends Item {

    public NavigationElementItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide && player.isShiftKeyDown()) {
            Minecraft.getInstance().setScreen(new NavInputScreen(stack));
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        return InteractionResultHolder.pass(stack);
    }
}
