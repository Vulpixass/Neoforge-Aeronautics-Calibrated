package net.vulpixass.aerocali.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static net.vulpixass.aerocali.AeronauticsCalibrated.MOD_ID;

public record DamageStaffPayload(int damageAmount) implements CustomPacketPayload {
    public static final Type<DamageStaffPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "damage_staff"));
    public static final StreamCodec<FriendlyByteBuf, DamageStaffPayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, DamageStaffPayload::damageAmount,
            DamageStaffPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {return TYPE;}
}