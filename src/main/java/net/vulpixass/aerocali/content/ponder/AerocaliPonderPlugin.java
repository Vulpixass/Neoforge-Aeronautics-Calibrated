package net.vulpixass.aerocali.content.ponder;

import com.simibubi.create.foundation.ponder.CreatePonderPlugin;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vulpixass.aerocali.content.block.AerocaliBlocks;
import net.vulpixass.aerocali.content.item.AerocaliItems;

import static net.vulpixass.aerocali.AeronauticsCalibrated.MOD_ID;

public class AerocaliPonderPlugin extends CreatePonderPlugin {
    @Override
    public String getModId() {
        return MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> registry) {
        // When ready registers the Ponder Scenes
        PonderScenes.register(registry);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        super.registerTags(helper);

        //Registers the Main Ponder Group for all the POnder Scenes
        ResourceLocation aerocaliGroupId = ResourceLocation.tryBuild(MOD_ID, "aerocali");

        helper.registerTag(aerocaliGroupId).addToIndex().item(new ItemStack((ItemLike) AerocaliBlocks.THRUSTER_ITEM).getItem())
                .title("aerocali.ponder.tag.aerocali").description("aerocali.ponder.tag.aerocali.desc").register();

        PonderTagRegistrationHelper<DeferredHolder<?, ?>> holderHelper = helper.withKeyFunction(DeferredHolder::getId);
        holderHelper.addToTag(aerocaliGroupId).add(AerocaliBlocks.THRUSTER_ITEM).add(AerocaliItems.IONIZED_THERMAL_MECHANISM)
                .add(AerocaliBlocks.GENERATOR_ITEM).add(AerocaliBlocks.MECHANICAL_ANVIL_ITEM).add(AerocaliBlocks.CABLE_ITEM).add(AerocaliBlocks.TRACKER_ITEM);
    }
}
