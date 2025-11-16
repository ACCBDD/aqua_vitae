package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class FermentingBlockEntity extends BlockEntity {
    FluidTank tank;

    public FermentingBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FERMENTER.get(), pos, blockState);
    }

    public void tickServer() {

    }
}
