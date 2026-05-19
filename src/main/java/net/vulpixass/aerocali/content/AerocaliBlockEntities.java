package net.vulpixass.aerocali.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vulpixass.aerocali.content.block.AerocaliBlocks;
import net.vulpixass.aerocali.content.block.custom.generator.BaseGeneratorBlockEntity;
import net.vulpixass.aerocali.content.block.custom.generator.creative.CreativeGeneratorBlockEntity;
import net.vulpixass.aerocali.content.block.custom.generator.industrial.IndustrialGeneratorBlockEntity;
import net.vulpixass.aerocali.content.block.custom.generator.regular.GeneratorBlockEntity;
import net.vulpixass.aerocali.content.block.custom.thruster.ThrusterBlockEntity;
import net.vulpixass.aerocali.content.block.custom.nav_tracker.NavigationTrackerBlockEntity;

public class AerocaliBlockEntities {
    // Register all BlockEntities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries
            .BLOCK_ENTITY_TYPE, "aerocali");

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ThrusterBlockEntity>> THRUSTER = BLOCK_ENTITIES
            .register("thruster", () -> BlockEntityType.Builder.of(ThrusterBlockEntity::new, AerocaliBlocks.THRUSTER
                            .get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneratorBlockEntity>> GENERATOR = BLOCK_ENTITIES
            .register("generator", () -> BlockEntityType.Builder.of(GeneratorBlockEntity::new, AerocaliBlocks.GENERATOR
                    .get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IndustrialGeneratorBlockEntity>> INDUSTRIAL_GENERATOR = BLOCK_ENTITIES
            .register("industrial_generator", () -> BlockEntityType.Builder.of(IndustrialGeneratorBlockEntity::new,
                    AerocaliBlocks.INDUSTRIAL_GENERATOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CreativeGeneratorBlockEntity>> CREATIVE_GENERATOR = BLOCK_ENTITIES
            .register("creative_generator", () -> BlockEntityType.Builder.of(CreativeGeneratorBlockEntity::new, AerocaliBlocks.CREATIVE_GENERATOR
                    .get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NavigationTrackerBlockEntity>> TRACKER = BLOCK_ENTITIES
            .register("tracker", () -> BlockEntityType.Builder.of(NavigationTrackerBlockEntity::new, AerocaliBlocks.TRACKER
                    .get()).build(null));
}
