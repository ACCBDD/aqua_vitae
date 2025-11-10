package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class KegBlockEntity extends BlockEntity {
    public static final String FLUID_TAG = "fluid";
    public static final int CAPACITY = 4000;

    private final FluidTank tank;

    public KegBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.KEG.get(), pos, blockState);
        tank = new FluidTank(CAPACITY) {
            @Override
            protected void onContentsChanged() {
                KegBlockEntity.this.setChanged();
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tank.writeToNBT(registries, tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tank.readFromNBT(registries, tag);
        setChanged();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    public FluidTank getTank() {
        return tank;
    }
}
