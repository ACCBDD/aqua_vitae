package com.accbdd.aqua_vitae.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class CombinedFluidHandler implements IFluidHandler {
    private final IFluidHandler[] handlers;
    private final int[] baseIndex;
    private final int tankCount;

    public CombinedFluidHandler(IFluidHandler... handlers) {
        this.handlers = handlers;
        this.baseIndex = new int[handlers.length];
        int index = 0;

        for (int i = 0; i < handlers.length; ++i) {
            index += handlers[i].getTanks();
            this.baseIndex[i] = index;
        }

        this.tankCount = index;
    }

    protected int getIndexForTank(int tank) { //get handler index from overall tank
        if (tank < 0) {
            return -1;
        } else {
            for (int i = 0; i < this.baseIndex.length; ++i) {
                if (tank - this.baseIndex[i] < 0) {
                    return i;
                }
            }

            return -1;
        }
    }

    protected IFluidHandler getHandlerFromIndex(int index) { //get handler from overall tank
        return index >= 0 && index < this.tankCount ? this.handlers[index] : null;
    }

    protected int getTankFromIndex(int tank, int index) { //get wrapped handler tank from overall tank
        return index > 0 && index < this.baseIndex.length ? tank - this.baseIndex[index - 1] : tank;
    }

    @Override
    public int getTanks() {
        return tankCount;
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        int index = this.getIndexForTank(i);
        IFluidHandler handler = getHandlerFromIndex(index);
        int tank = this.getTankFromIndex(i, index);
        return handler.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int i) {
        int index = this.getIndexForTank(i);
        IFluidHandler handler = getHandlerFromIndex(index);
        int tank = this.getTankFromIndex(i, index);
        return handler.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        int index = this.getIndexForTank(i);
        IFluidHandler handler = getHandlerFromIndex(index);
        int tank = this.getTankFromIndex(i, index);
        return handler.isFluidValid(tank, fluidStack);
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        int filled = 0;
        for (int i = 0; i < this.getTanks(); i++) {
            filled += handlers[i].fill(fluidStack, fluidAction);
        }

        return filled;
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        FluidStack drained = FluidStack.EMPTY;
        for (int i = 0; i < this.getTanks(); i++) {
            drained = handlers[i].drain(fluidStack, fluidAction);
            if (!drained.isEmpty()) {
                return drained;
            }
        }
        return drained;
    }

    @Override
    public FluidStack drain(int tank, FluidAction fluidAction) {
        FluidStack drained = FluidStack.EMPTY;
        for (int i = 0; i < this.getTanks(); i++) {
            drained = handlers[i].drain(tank, fluidAction);
            if (!drained.isEmpty()) {
                return drained;
            }
        }
        return drained;
    }
}
