package net.vulpixass.aerocali.content.item.data;

import dev.simulated_team.simulated.content.blocks.nav_table.NavTableBlockEntity;
import dev.simulated_team.simulated.content.blocks.nav_table.navigation_target.NavigationTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

public class NavTarget implements NavigationTarget {
    public NavTarget() {}

    //get the target for the Navigation Table
    @Override
    public Vec3 getTarget(NavTableBlockEntity navBE, ItemStack stack) {
        NavTargetData data = AerocaliDataComponents.NAV_TARGET_DATA.get(stack);
        if (data == null) return null;

        return new Vec3(data.x() + 0.5, data.y() + 0.5, data.z() + 0.5);
    }

    // returns the maximum range which... idk what it does XD!
    @Override
    public float getMaxRange() {
        return 0;
    }
}
