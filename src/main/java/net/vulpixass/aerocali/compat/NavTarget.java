package net.vulpixass.aerocali.compat;

import dev.simulated_team.simulated.content.blocks.nav_table.NavTableBlockEntity;
import dev.simulated_team.simulated.content.blocks.nav_table.navigation_target.NavigationTarget;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

import javax.annotation.Nullable;

public class NavTarget implements NavigationTarget {
    public NavTarget() {}

    @Nullable
    public Vec3 getTarget(NavTableBlockEntity be, ItemStack stack) {
        NavTargetData data = (NavTargetData)stack.get((DataComponentType)AerocaliDataComponents.NAV_TARGET.get());
        return data != null ? new Vec3(data.x(), data.y(), data.z()) : null;
    }

    public float getMaxRange() {
        return 1000000.0F;
    }
}
