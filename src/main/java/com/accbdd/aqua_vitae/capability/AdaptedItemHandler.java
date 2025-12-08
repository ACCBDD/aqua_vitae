package com.accbdd.aqua_vitae.capability;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class AdaptedItemHandler implements IItemHandlerModifiable {
    private final IItemHandlerModifiable wrapped;

    public AdaptedItemHandler(IItemHandlerModifiable wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public int getSlots() {
        return wrapped.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return wrapped.getStackInSlot(i);
    }

    @Override
    public ItemStack insertItem(int i, ItemStack itemStack, boolean simulate) {
        return wrapped.insertItem(i, itemStack, simulate);
    }

    @Override
    public ItemStack extractItem(int index, int count, boolean simulate) {
        return wrapped.extractItem(index, count, simulate);
    }

    @Override
    public int getSlotLimit(int i) {
        return wrapped.getSlotLimit(i);
    }

    @Override
    public boolean isItemValid(int i, ItemStack itemStack) {
        return wrapped.isItemValid(i, itemStack);
    }

    @Override
    public void setStackInSlot(int i, ItemStack itemStack) {
        this.wrapped.setStackInSlot(i, itemStack);
    }
}
