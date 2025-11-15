package com.accbdd.aqua_vitae.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class FluidUtils {

    public static boolean handleInteraction(Player player, InteractionHand hand, ItemStack itemStack, IFluidHandler other) {
        if (itemStack.getCapability(Capabilities.FluidHandler.ITEM) == null) {
            return false;
        } else {
            ItemStack copyStack = itemStack.copyWithCount(1);
            IFluidHandlerItem handler = copyStack.getCapability(Capabilities.FluidHandler.ITEM);
            FluidStack fluidInOther = other.getFluidInTank(0);
            if (handler != null) {
                boolean transfer = false;
                FluidStack fluidInItem = handler.getFluidInTank(0);
                if (fluidInItem.isEmpty()) { //handler is empty, try filling it
                    int filled = handler.fill(fluidInOther, IFluidHandler.FluidAction.EXECUTE);
                    if (filled > 0) { //if there is space in the item
                        other.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                        transfer = true;
                    }
                } else { //item has fluid, try emptying it then filling it
                    FluidStack simulatedExtract = handler.drain(handler.getTankCapacity(0), IFluidHandler.FluidAction.SIMULATE);
                    int accepted = other.fill(simulatedExtract, IFluidHandler.FluidAction.EXECUTE);
                    if (accepted > 0) { //other was able to take fluid in item
                        handler.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
                        transfer = true;
                    } else {
                        int filled = handler.fill(fluidInOther, IFluidHandler.FluidAction.EXECUTE);
                        if (filled > 0) { //if there is space in the item
                            other.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                            transfer = true;
                        }
                    }
                }

                ItemStack newContainer = handler.getContainer();
                if (!player.isCreative()) {
                    if (itemStack.getCount() > 1) {
                        itemStack.shrink(1);
                        player.addItem(newContainer);
                    } else {
                        player.setItemInHand(hand, newContainer);
                    }
                    player.getInventory().setChanged();
                }

                return transfer;
            }

            return false;
        }
    }
}
