package com.accbdd.aqua_vitae.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;

/**
 * component for the name of an alcohol, usually recalculated on aging, etc
 *
 * @param component the name of the alcohol
 */
public record AlcoholNameComponent(Component component) {
    public static final AlcoholNameComponent GENERIC = new AlcoholNameComponent(Component.translatable("alcohol.aqua_vitae.generic"));

    public static final Codec<AlcoholNameComponent> CODEC = ComponentSerialization.FLAT_CODEC.xmap(AlcoholNameComponent::new, name -> name.component);
    public static final StreamCodec<RegistryFriendlyByteBuf, AlcoholNameComponent> STREAM_CODEC = ComponentSerialization.STREAM_CODEC.map(AlcoholNameComponent::new, name -> name.component);
}
