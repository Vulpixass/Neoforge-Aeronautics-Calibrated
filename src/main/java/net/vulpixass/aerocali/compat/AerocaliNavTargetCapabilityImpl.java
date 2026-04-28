package net.vulpixass.aerocali.compat;

import dev.simulated_team.simulated.content.blocks.nav_table.navigation_target.NavigationTarget;
import net.minecraft.world.item.ItemStack;
import net.vulpixass.aerocali.content.item.data.target.AerocaliNavTarget;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

public class AerocaliNavTargetCapabilityImpl implements AerocaliNavTargetCapability {

    @Override
    public NavigationTarget getNavigationTarget(ItemStack stack) {

        // 1. Check YOUR component first
        AerocaliNavTarget ours = stack.get(AerocaliDataComponents.NAV_TABLE_TARGET.get());
        if (ours != null)
            return ours;

        // 2. Fallback to Simulated’s logic
        return NavigationTarget.ofStack(stack);
    }
}
