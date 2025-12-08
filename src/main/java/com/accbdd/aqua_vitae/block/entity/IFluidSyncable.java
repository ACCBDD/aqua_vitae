package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.network.FluidSyncPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;

public interface IFluidSyncable {
    default void sendFluidUpdate(BlockEntity entity, FluidStack stack, int tankId) {
        if (entity.getLevel() != null && !entity.getLevel().isClientSide) {
            PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) entity.getLevel(),
                    new ChunkPos(entity.getBlockPos()),
                    new FluidSyncPacket(entity.getBlockPos(), stack, tankId)
            );
        }
    }

    void setFluid(FluidStack stack, int tankId);
}
