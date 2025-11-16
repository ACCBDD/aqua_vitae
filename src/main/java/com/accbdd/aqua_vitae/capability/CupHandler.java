package com.accbdd.aqua_vitae.capability;

import com.accbdd.aqua_vitae.component.FluidStackComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

/**
 * Capability for an item that stores a liquid
 */
public class CupHandler extends FluidTank implements IFluidHandlerItem {
    private final ItemStack stack;

    public CupHandler(ItemStack stack, int capacity) {
        super(capacity);
        this.stack = stack;
        this.fluid = stack.getOrDefault(ModComponents.FLUIDSTACK, FluidStackComponent.EMPTY).stack().copy();
    }

    @Override
    public ItemStack getContainer() {
        if (fluid.isEmpty())
            stack.remove(ModComponents.FLUIDSTACK);
        else
            stack.set(ModComponents.FLUIDSTACK, new FluidStackComponent(fluid.copy()));
        return stack;
    }

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
        stack.set(ModComponents.FLUIDSTACK, new FluidStackComponent(this.fluid.copy()));
    }
}
