package net.vulpixass.aerocali.content.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

import java.util.List;

public class TraverseBoardItem extends Item {
    public TraverseBoardItem(Properties properties) {super(properties);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        NavTargetData data = AerocaliDataComponents.NAV_TARGET_DATA.get(stack);
        if (level.isClientSide()) return InteractionResultHolder.sidedSuccess(stack, true);

        if (player.isShiftKeyDown()) {
            stack.remove(AerocaliDataComponents.NAV_TARGET.get());
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOOK_PUT, SoundSource.PLAYERS, 1.0f, 1.0f);
            return InteractionResultHolder.success(stack);
        } else {
            NavTargetData newData = new NavTargetData((int) player.getX(), (int) player.getY(), (int) player.getZ(), level.dimension().location().toString());
            stack.set(AerocaliDataComponents.NAV_TARGET.get(), newData);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_LIBRARIAN, SoundSource.PLAYERS, 1.0f, 1.0f);
            return InteractionResultHolder.success(stack);
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.aerocali.traverse_board.usage")
                .withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
