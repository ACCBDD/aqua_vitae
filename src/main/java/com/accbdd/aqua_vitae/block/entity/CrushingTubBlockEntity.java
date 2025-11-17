package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.util.RegistryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CrushingTubBlockEntity extends BaseSingleFluidTankEntity {
    public static final int CAPACITY = 1000;
    public static final String ITEMS_TAG = "items";

    private final ItemStackHandler items;

    public CrushingTubBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CRUSHING_TUB.get(), pos, blockState, CAPACITY);
        this.items = new ItemStackHandler(4) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return RegistryUtils.getIngredient(stack) != null;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(ITEMS_TAG, items.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(ITEMS_TAG))
            items.deserializeNBT(registries, tag.getCompound(ITEMS_TAG));
    }

    public IItemHandler getItemHandler() {
        return items;
    }
}
