package com.accbdd.aqua_vitae.screen.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.function.Predicate;

public class CustomSlot extends SlotItemHandler {
    private final Predicate<ItemStack> placePredicate;
    private final Predicate<Player> takePredicate;

    public CustomSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> placePredicate, Predicate<Player> takePredicate) {
        super(itemHandler, index, xPosition, yPosition);
        this.placePredicate = placePredicate;
        this.takePredicate = takePredicate;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return placePredicate.test(stack);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return takePredicate.test(playerIn);
    }

    public static class Input extends CustomSlot {
        public Input(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition, null, null);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return true;
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return true;
        }
    }

    public static class Output extends CustomSlot {
        public Output(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition, null, null);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return true;
        }
    }
}
