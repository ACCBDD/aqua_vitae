package com.accbdd.aqua_vitae.capability;

import com.accbdd.aqua_vitae.component.FluidStackComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

/**
 * Capability for an item that stores one portion of a liquid, only able to fill up to full and empty out any amount
 */
public class CupHandler extends FluidTank implements IFluidHandlerItem {
    private final ItemStack stack;

    public CupHandler(ItemStack stack, int capacity) {
        super(capacity);
        this.stack = stack;
        this.fluid = stack.getOrDefault(ModComponents.FLUIDSTACK.get(), FluidStackComponent.EMPTY).stack();
    }

    @Override
    public ItemStack getContainer() {
        return stack;
    }

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
        stack.set(ModComponents.FLUIDSTACK.get(), new FluidStackComponent(this.fluid));
    }

    public void interactWith(IFluidHandler other) {
        if (this.fluid.isEmpty()) { //fill this cup
           if (other.drain(capacity, FluidAction.SIMULATE).getAmount() == capacity) {  //if we can fully fill this cup up
               fill(other.drain(capacity, FluidAction.EXECUTE), FluidAction.EXECUTE);
           }
        } else { //try to empty as much as possible
            drain(other.fill(drain(capacity, FluidAction.SIMULATE), FluidAction.EXECUTE), FluidAction.EXECUTE);
        }
    }
}
