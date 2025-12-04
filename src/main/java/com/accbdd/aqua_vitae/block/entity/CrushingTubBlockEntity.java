package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.recipe.WortInput;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.registry.ModFluids;
import com.accbdd.aqua_vitae.util.BrewingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrushingTubBlockEntity extends BaseSingleFluidTankEntity {
    public static final int CAPACITY = 1000;
    public static final String ITEMS_TAG = "inputs";
    public static final String CRUSH_TAG = "crush";

    private final ItemStackHandler items;
    public int crush;

    public CrushingTubBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CRUSHING_TUB.get(), pos, blockState, CAPACITY);
        this.items = new ItemStackHandler(4) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return BrewingUtils.getIngredient(stack) != null;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                crush = 0;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(ITEMS_TAG, items.serializeNBT(registries));
        tag.putInt(CRUSH_TAG, crush);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(ITEMS_TAG))
            items.deserializeNBT(registries, tag.getCompound(ITEMS_TAG));
        if (tag.contains(CRUSH_TAG))
            crush = tag.getInt(CRUSH_TAG);
    }

    public IItemHandler getItemHandler() {
        return items;
    }

    public void crush() {
        setChanged();
        if (getLevel() == null || getLevel().isClientSide)
            return;

        if (crush++ > 5) {
            getLevel().playSound(null, getBlockPos(), SoundEvents.SLIME_BLOCK_BREAK, SoundSource.BLOCKS);
            FluidStack fluid = new FluidStack(ModFluids.WORT, getFluid().getAmount());
            List<WortInput> inputs = new ArrayList<>();
            Set<ResourceKey<Flavor>> flavors = new HashSet<>();
            BrewingIngredient.BrewingProperties properties = null;
            for (int i = 0; i < items.getSlots(); i++) {
                ItemStack stack = items.extractItem(i, 1, false);
                if (stack.isEmpty())
                    continue;
                ((ServerLevel) getLevel()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), (float) getBlockPos().getX() + 0.5F, (float) getBlockPos().getY() + 0.1F, (float) getBlockPos().getZ() + 0.5F, 20, 0.25, 0.25, 0.25, 0.0);

                BrewingIngredient ing = BrewingUtils.getIngredient(stack);
                if (ing == null)
                    continue;
                flavors.addAll(ing.flavors());
                if (inputs.isEmpty())
                    properties = ing.properties().copy();
                else
                    properties = properties.add(ing.properties(), inputs.size());
                inputs.add(WortInput.of(stack));
            }
            fluid.set(ModComponents.PRECURSOR_PROPERTIES, new PrecursorPropertiesComponent(inputs, flavors, properties));
            setFluid(fluid);
        } else {
            getLevel().playSound(null, getBlockPos(), SoundEvents.SLIME_BLOCK_FALL, SoundSource.BLOCKS);
            for (int i = 0; i < getItemHandler().getSlots(); i++) {
                if (!getItemHandler().getStackInSlot(i).isEmpty())
                    ((ServerLevel) getLevel()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, getItemHandler().getStackInSlot(i)), (float) getBlockPos().getX() + 0.5F, (float) getBlockPos().getY() + 0.1F, (float) getBlockPos().getZ() + 0.5F, 10, 0.25, 0.25, 0.25, 0.0);
            }
        }
    }
}
