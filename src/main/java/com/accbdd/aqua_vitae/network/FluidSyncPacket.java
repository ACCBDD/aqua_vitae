package com.accbdd.aqua_vitae.network;

import com.accbdd.aqua_vitae.block.entity.IFluidSyncable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public record FluidSyncPacket(BlockPos pos, FluidStack fluidStack, int tankId) implements CustomPacketPayload {
    public static final Type<FluidSyncPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "fluid_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidSyncPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, FluidSyncPacket::pos,
            FluidStack.OPTIONAL_STREAM_CODEC, FluidSyncPacket::fluidStack,
            ByteBufCodecs.VAR_INT, FluidSyncPacket::tankId,
            FluidSyncPacket::new
    );

    @Override
    public Type<FluidSyncPacket> type() {
        return TYPE;
    }

    public static void handle(FluidSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
           if (context.player().level() instanceof ClientLevel level) {
               if (level.getBlockEntity(packet.pos()) instanceof IFluidSyncable be) {
                   be.setFluid(packet.fluidStack(), packet.tankId());
               }

               if (context.player().containerMenu instanceof IFluidSyncable menu) {
                   menu.setFluid(packet.fluidStack(), packet.tankId());
               }
           }
        });
    }
}
