package net.vulpixass.aerocali.content.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.data.AerocaliDataComponents;
import net.vulpixass.aerocali.network.packet.NavUpdatePayload;

public class NavInputScreen extends Screen {

    private final ItemStack stack;
    private boolean invalidCoords = false;
    private EditBox xField;
    private EditBox zField;

    public NavInputScreen(ItemStack stack) {
        super(Component.literal("Navigation Screen"));
        this.stack = stack;
    }

    @Override
    protected void init() {
        // Define the Editable Boxes
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        this.xField = new EditBox(this.font, centerX - 40, centerY - 20, 80, 16, Component.literal("X"));
        this.zField = new EditBox(this.font, centerX - 40, centerY + 2, 80, 16, Component.literal("Z"));

        // Add the required Boxes and Button
        this.addRenderableWidget(this.xField);
        this.addRenderableWidget(this.zField);
        this.addRenderableWidget(Button.builder(Component.literal("Confirm"), (btn) -> {
            try {
                // Get the X and Z Coordinates and send them to the server to update the item
                int x = Integer.parseInt(this.xField.getValue());
                int y = 0;
                int z = Integer.parseInt(this.zField.getValue());
                this.stack.set(AerocaliDataComponents.NAV_TARGET.get(), new NavTargetData(x, y, z, this.minecraft.level.dimension().location().toString()));
                PacketDistributor.sendToServer(new NavUpdatePayload(x, y, z));
                this.onClose();
            } catch (NumberFormatException var5) {
                this.invalidCoords = true;
            }

        }).bounds(centerX - 40, centerY + 50, 80, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Renders a background which... doesn't exist with a texture... don't ask me...
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        // Show What and where Info is supposed to go into the Boxes
        guiGraphics.drawCenteredString(this.font, "Enter Coordinates", this.width / 2, this.height / 2 - 60, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, "X: ", this.width / 2 - 44, this.height / 2 - 16, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, "Z: ", this.width / 2 - 44, this.height / 2 + 6, 0xFFFFFF);
        if (this.invalidCoords) {
            guiGraphics.drawCenteredString(this.font, "Invalid Coordinates", this.width / 2, this.height / 2 + 30, 16711680);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}