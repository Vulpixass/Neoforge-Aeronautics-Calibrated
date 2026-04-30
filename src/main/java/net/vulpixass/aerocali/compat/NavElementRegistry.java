package net.vulpixass.aerocali.compat;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.simulated_team.simulated.Simulated;
import net.minecraft.world.level.ItemLike;
import net.vulpixass.aerocali.content.item.AerocaliItems;

public class NavElementRegistry {
    public static void init() {
        //Simulated.getRegistrate().navTarget("navigation_element", NavTarget::new, AerocaliItems.NAVIGATION_ELEMENT.get());
    }
}
