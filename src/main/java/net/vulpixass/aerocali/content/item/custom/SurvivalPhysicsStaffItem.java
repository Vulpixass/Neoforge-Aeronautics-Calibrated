package net.vulpixass.aerocali.content.item.custom;

import dev.simulated_team.simulated.content.physics_staff.PhysicsStaffItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SurvivalPhysicsStaffItem extends PhysicsStaffItem {
    public SurvivalPhysicsStaffItem(Properties properties) {
        super(properties);
        RANGE = 32;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.pressed").withStyle(ChatFormatting.DARK_GRAY));
            tooltipComponents.add(Component.literal(""));

            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.context_1").withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.context_2").withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.literal(""));

            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.l_click").withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.l_click_desc_1").withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.l_click_desc_2").withStyle(ChatFormatting.GOLD));

            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.r_click").withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.r_click_desc_1").withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.r_click_desc_2").withStyle(ChatFormatting.GOLD));

            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.tab").withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.tab_desc_1").withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.tab_desc_2").withStyle(ChatFormatting.GOLD));

            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.scroll").withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.scroll_desc_1").withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.scroll_desc_2").withStyle(ChatFormatting.GOLD));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.aerocali.survival_physics_staff.unpressed").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
