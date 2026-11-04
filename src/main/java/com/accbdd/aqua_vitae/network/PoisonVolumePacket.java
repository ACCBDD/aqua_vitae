package com.accbdd.aqua_vitae.network;

import com.accbdd.aqua_vitae.client.audio.SaxophoneSound;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public record PoisonVolumePacket(float volume) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PoisonVolumePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "poison_volume"));

    public static final StreamCodec<ByteBuf, PoisonVolumePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, PoisonVolumePacket::volume,
            PoisonVolumePacket::new
    );

    public static void handle(PoisonVolumePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (packet.volume <= 0f) {
                if (SaxophoneSound.INSTANCE == null)
                    return;
                SaxophoneSound.INSTANCE.stopSound();
                SaxophoneSound.INSTANCE = null;
                return;
            }
            if (SaxophoneSound.INSTANCE == null || SaxophoneSound.INSTANCE.isStopped()) {
                SaxophoneSound.INSTANCE = new SaxophoneSound();
                Minecraft.getInstance().getSoundManager().play(SaxophoneSound.INSTANCE);
            }
            SaxophoneSound.INSTANCE.setFadeVolume(packet.volume());
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
