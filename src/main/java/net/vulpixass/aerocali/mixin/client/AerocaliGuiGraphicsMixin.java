package net.vulpixass.aerocali.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(GuiGraphics.class)
public abstract class AerocaliGuiGraphicsMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private PoseStack pose;

    @Shadow
    public abstract int guiWidth();

    @Shadow
    public abstract void fill(int var1, int var2, int var3, int var4, int var5);
    @WrapMethod(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
                allow=2)

    // Cut the Staff Inv Item to fit the slot
    private void aerocali$extendStaffScissor(LivingEntity entity, Level level, ItemStack stack,
                                             int x, int y, int seed, int guiOffset, Operation<Void> original) {
        boolean isStaff = stack.is(AerocaliItems.SURVIVAL_PHYSICS_STAFF.get());
        if (isStaff) {
            Window window = Minecraft.getInstance().getWindow();
            float scale = (float)window.getGuiScale();
            Matrix4fc pose = this.pose.last().pose();
            Vector3f position = pose.transformPosition(new Vector3f((float)x, (float)y, 0.0F));
            Vector3f corner = pose.transformPosition(new Vector3f((float)(x + 16), (float)(y + 16), 0.0F));
            position.mul(scale);
            corner.mul(scale);
            int slotHeight = (int)(corner.y - position.y);
            RenderSystem.enableScissor((int)position.x, window.getHeight() - (int)position.y - slotHeight, (int)(corner.x - position.x), slotHeight);
        }
        original.call(entity, level, stack, x, y, seed, guiOffset);
        if (isStaff) RenderSystem.disableScissor();
    }
}

