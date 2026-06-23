package net.vulpixass.aerocali.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NavUpdatePayload(double x, double y, double z) implements CustomPacketPayload {
    // Define the Payload to update the Clientside Navigation Compass texture
    public static final Type<NavUpdatePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("aerocali", "nav_update"));

    public static final StreamCodec<ByteBuf, NavUpdatePayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.DOUBLE, NavUpdatePayload::x,
            ByteBufCodecs.DOUBLE, NavUpdatePayload::y, ByteBufCodecs.DOUBLE, NavUpdatePayload::z, NavUpdatePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
