package com.accbdd.aqua_vitae.capability;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class AdaptedFluidHandler implements IFluidHandler {
    private final IFluidHandler wrapped;

    public AdaptedFluidHandler(IFluidHandler wrapped) {

        this.wrapped = wrapped;
    }

    @Override
    public int getTanks() {
        return wrapped.getTanks();
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        return wrapped.getFluidInTank(i);
    }

    @Override
    public int getTankCapacity(int i) {
        return wrapped.getTankCapacity(i);
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return wrapped.isFluidValid(i, fluidStack);
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return wrapped.fill(fluidStack, fluidAction);
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return wrapped.drain(fluidStack, fluidAction);
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return wrapped.drain(i, fluidAction);
    }
}
