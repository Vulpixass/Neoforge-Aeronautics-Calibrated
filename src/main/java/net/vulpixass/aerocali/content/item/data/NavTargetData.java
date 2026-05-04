package net.vulpixass.aerocali.content.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.simulated_team.simulated.content.blocks.nav_table.NavTableBlockEntity;
import dev.simulated_team.simulated.content.blocks.nav_table.navigation_target.NavigationTarget;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record NavTargetData(int x, int y, int z, String dimension) implements NavigationTarget {

    public static final Codec<NavTargetData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.INT.fieldOf("x").forGetter(NavTargetData::x), Codec.INT.fieldOf("y").forGetter(NavTargetData::y),
                    Codec.INT.fieldOf("z").forGetter(NavTargetData::z), Codec.STRING.fieldOf("dimension")
                            .forGetter(NavTargetData::dimension)).apply(instance, NavTargetData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, NavTargetData> STREAM_CODEC;

    @Override
    public @Nullable Vec3 getTarget(NavTableBlockEntity navTableBlockEntity, ItemStack itemStack) {
        return new Vec3(this.x, this.y, this.z);
    }

    @Override
    public float getMaxRange() {
        return 1000000.0F;
    }

    @Override
    public float getDeadzone() {
        return 0.5F;
    }

    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, NavTargetData::x, ByteBufCodecs.INT, NavTargetData::y, ByteBufCodecs.INT, NavTargetData::z, ByteBufCodecs.STRING_UTF8, NavTargetData::dimension, NavTargetData::new);
    }
}

