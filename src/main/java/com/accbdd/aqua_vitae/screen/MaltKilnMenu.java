package com.accbdd.aqua_vitae.screen;

import com.accbdd.aqua_vitae.block.entity.MaltKilnBlockEntity;
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
import net.neoforged.neoforge.items.SlotItemHandler;

public class MaltKilnMenu extends AbstractBaseInventoryMenu {
    private final ContainerLevelAccess containerLevelAccess;
    private final ContainerData data;

    public MaltKilnMenu(int windowId, Inventory inventory) {
        this(windowId, inventory, ContainerLevelAccess.NULL, MaltKilnBlockEntity.createClientItemHandler(), new SimpleContainerData(4));
    }

    public MaltKilnMenu(int windowId, Inventory inventory, ContainerLevelAccess containerLevelAccess, IItemHandler items, ContainerData data) {
        super(ModMenus.MALT_KILN.get(), windowId, inventory, 3, 8, 84);
        this.containerLevelAccess = containerLevelAccess;
        this.data = data;
        this.addSlot(new SlotItemHandler(items, 0, 56, 17));
        this.addSlot(new SlotItemHandler(items, 1, 56, 53));
        this.addSlot(new SlotItemHandler(items, 2, 116, 35) {
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
        return AbstractContainerMenu.stillValid(containerLevelAccess, player, ModBlocks.MALT_KILN.get());
    }

    public int getProgress() {
        return data.get(0);
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
