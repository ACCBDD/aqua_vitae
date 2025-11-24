package com.accbdd.aqua_vitae.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Comparator;
import java.util.Objects;

public record WortInput(ItemStack item, FluidStack fluid) {
    public static final Codec<WortInput> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.CODEC.optionalFieldOf("item", ItemStack.EMPTY).forGetter(WortInput::item),
                    FluidStack.CODEC.optionalFieldOf("fluid", FluidStack.EMPTY).forGetter(WortInput::fluid)
            ).apply(instance, WortInput::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, WortInput> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, WortInput::item,
            FluidStack.OPTIONAL_STREAM_CODEC, WortInput::fluid,
            WortInput::new
    );

    public static final Comparator<WortInput> COMPARATOR = Comparator
            .<WortInput, Boolean>comparing(entry -> !entry.item().isEmpty())
            .thenComparing(
                    entry -> entry.item().isEmpty()
                            ? ResourceLocation.withDefaultNamespace("")
                            : entry.item().getItem().builtInRegistryHolder().key().location()
            )
            .thenComparing(
                    entry -> entry.fluid().isEmpty()
                            ? ResourceLocation.withDefaultNamespace("")
                            : entry.fluid().getFluid().builtInRegistryHolder().key().location()
            )
            .thenComparingInt(entry -> entry.item().getCount() + entry.fluid().getAmount());

    public static WortInput of(ItemStack item) {
        return new WortInput(item, FluidStack.EMPTY);
    }

    public static WortInput of(FluidStack fluid) {
        return new WortInput(ItemStack.EMPTY, fluid);
    }

    public boolean isValid() {
        return !item.isEmpty() ^ !fluid.isEmpty(); // XOR check
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WortInput input = (WortInput) o;
        return ItemStack.isSameItemSameComponents(item, input.item) && FluidStack.isSameFluidSameComponents(fluid, input.fluid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, fluid);
    }
}
