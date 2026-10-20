package com.accbdd.aqua_vitae.component;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.api.BrewingIngredient;
import com.accbdd.aqua_vitae.api.Flavor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.Set;

public record FermentingPropertiesComponent(int stress,
                                            BrewingIngredient.BrewingProperties properties) {
    public static final FermentingPropertiesComponent EMPTY = new FermentingPropertiesComponent(0, BrewingIngredient.BrewingProperties.DEFAULT);

    public static final Codec<FermentingPropertiesComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("stress").forGetter(FermentingPropertiesComponent::stress),
                    BrewingIngredient.BrewingProperties.CODEC.fieldOf("properties").forGetter(FermentingPropertiesComponent::properties)
            ).apply(instance, FermentingPropertiesComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FermentingPropertiesComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FermentingPropertiesComponent::stress,
            BrewingIngredient.BrewingProperties.STREAM_CODEC, FermentingPropertiesComponent::properties,
            FermentingPropertiesComponent::new
    );
}
