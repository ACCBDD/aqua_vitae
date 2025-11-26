package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.capability.WrappedItemHandler;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.util.RegistryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class MaltKilnBlockEntity extends AbstractBEWithData {
    private static final String ITEM_TAG = "items";
    private static final String PROGRESS_TAG = "progress";
    private static final int MAX_PROGRESS = 200;

    private final ItemStackHandler itemHandler; //0 - input, 1 - fuel, 2 - output
    private final WrappedItemHandler wrappedItemHandler;
    private int progress, burnTime;

    private final ContainerData containerData;

    public MaltKilnBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MALT_KILN.get(), pos, blockState);
        this.itemHandler = createItemHandler();

        this.wrappedItemHandler = new WrappedItemHandler(itemHandler) {
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (slot == 2)
                    return stack;
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (slot < 2)
                    return ItemStack.EMPTY;
                return super.extractItem(slot, amount, simulate);
            }
        };

        this.containerData = new ContainerData() {
            @Override
            public int get(int i) {
                if (i == 0)
                    return MaltKilnBlockEntity.this.progress;
                return MaltKilnBlockEntity.this.burnTime;
            }


            @Override
            public void set(int i, int val) {
                if (i == 0)
                    MaltKilnBlockEntity.this.progress = val;
                else
                    MaltKilnBlockEntity.this.burnTime = val;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @NotNull
    public static ItemStackHandler createItemHandler() {
        return new ItemStackHandler(3) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0)
                    return RegistryUtils.getIngredient(stack) != null;
                if (slot == 1)
                    return stack.getBurnTime(RecipeType.SMELTING) > 0;
                return false;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(ITEM_TAG, itemHandler.serializeNBT(registries));
        tag.putInt(PROGRESS_TAG, progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(ITEM_TAG))
            itemHandler.deserializeNBT(registries, tag.getCompound(ITEM_TAG));
        if (tag.contains(PROGRESS_TAG))
            progress = tag.getInt(PROGRESS_TAG);
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public IItemHandler getWrappedItemHandler() {
        return wrappedItemHandler;
    }

    public ContainerData getContainerData() {
        return containerData;
    }
}
