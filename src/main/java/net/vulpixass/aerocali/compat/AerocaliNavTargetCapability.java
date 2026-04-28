package net.vulpixass.aerocali.compat;

import dev.simulated_team.simulated.content.blocks.nav_table.navigation_target.NavigationTarget;
import net.minecraft.world.item.ItemStack;

public interface AerocaliNavTargetCapability {
    NavigationTarget getNavigationTarget(ItemStack stack);
}
