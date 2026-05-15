package net.vulpixass.aerocali.network;

import dev.simulated_team.simulated.index.SimNavigationTargets;
import dev.simulated_team.simulated.index.SimRegistries;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NavUpdatePayload(double x, double y, double z) implements CustomPacketPayload {
    public static final Type<NavUpdatePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("aerocali", "nav_update"));

    public static final StreamCodec<ByteBuf, NavUpdatePayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.DOUBLE, NavUpdatePayload::x,
            ByteBufCodecs.DOUBLE, NavUpdatePayload::y, ByteBufCodecs.DOUBLE, NavUpdatePayload::z, NavUpdatePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
