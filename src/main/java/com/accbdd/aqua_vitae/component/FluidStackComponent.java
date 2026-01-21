package com.accbdd.aqua_vitae.component;

import com.accbdd.aqua_vitae.registry.ModComponents;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;

public record FluidStackComponent(FluidStack stack) {
    public static final FluidStackComponent EMPTY = new FluidStackComponent(FluidStack.EMPTY);

    public static final Codec<FluidStackComponent> CODEC = FluidStack.OPTIONAL_CODEC.xmap(FluidStackComponent::new, FluidStackComponent::stack);
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidStackComponent> STREAM_CODEC = FluidStack.OPTIONAL_STREAM_CODEC.map(FluidStackComponent::new, FluidStackComponent::stack);

    public Component getHoverName() {
        if (stack.has(ModComponents.ALCOHOL_NAME))
            return stack.get(ModComponents.ALCOHOL_NAME).component();
        return stack.getHoverName();
    }
}
