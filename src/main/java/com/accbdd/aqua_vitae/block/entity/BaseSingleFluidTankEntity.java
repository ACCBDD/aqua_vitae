package com.accbdd.aqua_vitae.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public abstract class BaseSingleFluidTankEntity extends BlockEntity {
    private static final String FLUID_TAG = "fluid";

    private final int capacity;
    private final IFluidHandler fluidHandler;

    public BaseSingleFluidTankEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity) {
        super(type, pos, blockState);
        this.capacity = capacity;
        this.fluidHandler = createFluidHandler(capacity);
    }

    protected IFluidHandler createFluidHandler(int capacity) {
        return new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                BaseSingleFluidTankEntity.this.setChanged();
            }
        };
    }

    public int getCapacity() {
        return capacity;
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    public FluidStack getFluid() {
        return fluidHandler.getFluidInTank(0);
    }

    public int fill(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
        return fluidHandler.fill(fluidStack, fluidAction);
    }

    public FluidStack drain(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
        return fluidHandler.drain(fluidStack, fluidAction);
    }

    public FluidStack drain(int amount, IFluidHandler.FluidAction fluidAction) {
        return fluidHandler.drain(amount, fluidAction);
    }

    public void setFluid(FluidStack fluid) {
        fluidHandler.drain(getFluid(), IFluidHandler.FluidAction.EXECUTE);
        fluidHandler.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!getFluid().isEmpty()) {
            tag.put(FLUID_TAG, getFluid().save(registries));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(FLUID_TAG)) {
            setFluid(FluidStack.parseOptional(registries, tag.getCompound(FLUID_TAG)));
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }
}
