package net.vulpixass.aerocali.content.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;

public record NavTargetData(int x, int y, int z, String dimension) {

    public static final Codec<NavTargetData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("x").forGetter(NavTargetData::x),
                    Codec.INT.fieldOf("y").forGetter(NavTargetData::y),
                    Codec.INT.fieldOf("z").forGetter(NavTargetData::z),
                    Codec.STRING.fieldOf("dimension").forGetter(NavTargetData::dimension)
            ).apply(instance, NavTargetData::new)
    );

    public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, NavTargetData> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, NavTargetData::x,
                    ByteBufCodecs.INT, NavTargetData::y,
                    ByteBufCodecs.INT, NavTargetData::z,
                    ByteBufCodecs.STRING_UTF8, NavTargetData::dimension,
                    NavTargetData::new
            );
}
