package com.accbdd.aqua_vitae.screen;

import com.accbdd.aqua_vitae.block.entity.MashTunBlockEntity;
import com.accbdd.aqua_vitae.registry.ModBlocks;
import com.accbdd.aqua_vitae.registry.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class MashTunMenu extends AbstractBaseInventoryMenu {
    private final ContainerLevelAccess containerLevelAccess;
    private final ContainerData data;

    public MashTunMenu(int windowId, Inventory inventory) {
        this(windowId, inventory, ContainerLevelAccess.NULL, new ItemStackHandler(10), new SimpleContainerData(4));
    }

    public MashTunMenu(int windowId, Inventory inventory, ContainerLevelAccess containerLevelAccess, IItemHandler items, ContainerData data) {
        super(ModMenus.MASH_TUN.get(), windowId, inventory, 10, 8, 84);
        this.containerLevelAccess = containerLevelAccess;
        this.data = data;
        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(items, i, 24 + (i % 3) * 18, 17 + (i / 3) * 18));
        }
        this.addSlot(new SlotItemHandler(items, 9, 87, 47) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        this.addDataSlots(data);
        layoutPlayerInventorySlots(inventory);
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(containerLevelAccess, player, ModBlocks.MASH_TUN.get());
    }

    public int getProgress() {
        return MashTunBlockEntity.MAX_PROGRESS / 2;
    }

    public int getBurnTime() {
        return data.get(1);
    }

    public int getMaxBurnTime() {
        return data.get(2);
    }

    public int getFluidAmount() {
        return data.get(3);
    }
}
