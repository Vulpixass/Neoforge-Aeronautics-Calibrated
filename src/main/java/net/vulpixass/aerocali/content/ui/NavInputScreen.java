package net.vulpixass.aerocali.content.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vulpixass.aerocali.AeronauticsCalibrated;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

public class NavInputScreen extends Screen {

    private static final ResourceLocation BG = ResourceLocation.tryBuild(AeronauticsCalibrated.MOD_ID, "textures/gui/nav_input.png");

    private final ItemStack stack;

    private EditBox xField;
    private EditBox yField;
    private EditBox zField;

    public NavInputScreen(ItemStack stack) {
        super(Component.literal("Navigation Target"));
        this.stack = stack;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        xField = new EditBox(this.font, centerX - 40, centerY - 20, 80, 16, Component.literal("X"));
        yField = new EditBox(this.font, centerX - 40, centerY + 2, 80, 16, Component.literal("Y"));
        zField = new EditBox(this.font, centerX - 40, centerY + 24, 80, 16, Component.literal("Z"));

        this.addRenderableWidget(xField);
        this.addRenderableWidget(yField);
        this.addRenderableWidget(zField);

        this.addRenderableWidget(Button.builder(Component.literal("Confirm"), btn -> {
            try {
                int x = Integer.parseInt(xField.getValue());
                int y = Integer.parseInt(yField.getValue());
                int z = Integer.parseInt(zField.getValue());

                stack.set(AerocaliDataComponents.NAV_TARGET.get(),
                        new NavTargetData(x, y, z, minecraft.level.dimension().location().toString()));

                this.onClose();

            } catch (NumberFormatException ignored) {
                // TODO: show error message
            }
        }).bounds(centerX - 40, centerY + 50, 80, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.blit(BG, (this.width - 176) / 2, (this.height - 166) / 2, 0, 0, 176, 166);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.drawCenteredString(this.font, "Enter Coordinates", this.width / 2, this.height / 2 - 60, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
