package com.accbdd.aqua_vitae.network;

import com.accbdd.aqua_vitae.registry.ModAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public record AlcoholSyncPacket(int bloodAlcohol, int intoxication, int hangover) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AlcoholSyncPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "alcohol_sync"));

    public static final StreamCodec<ByteBuf, AlcoholSyncPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, AlcoholSyncPacket::bloodAlcohol,
            ByteBufCodecs.INT, AlcoholSyncPacket::intoxication,
            ByteBufCodecs.INT, AlcoholSyncPacket::hangover,
            AlcoholSyncPacket::new
    );

    public static void handle(AlcoholSyncPacket packet, IPayloadContext context) {
        Player player = context.player();
        player.setData(ModAttachments.BLOOD_ALCOHOL, packet.bloodAlcohol());
        player.setData(ModAttachments.INTOXICATION, packet.intoxication());
        player.setData(ModAttachments.HANGOVER, packet.hangover());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
