package net.vulpixass.aerocali.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class AerocaliCapabilities {
    public static final BlockCapability<IEnergyStorage, Void> ENERGY = BlockCapability.create(ResourceLocation
                    .fromNamespaceAndPath("aerocali", "energy"), IEnergyStorage.class, Void.class);
}
