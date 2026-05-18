package net.vulpixass.aerocali.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class AerocaliCapabilities {
    // Define/Register the Capability so the BlockEntities requiring FE connect at the correct places
    public static final BlockCapability<IEnergyStorage, Void> ENERGY = BlockCapability.create(ResourceLocation
                    .fromNamespaceAndPath("aerocali", "energy"), IEnergyStorage.class, Void.class);
}
