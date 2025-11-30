package com.accbdd.aqua_vitae.component;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public record BrewingIngredientComponent(BrewingIngredient.BrewingProperties properties, @Nullable BrewingIngredient.BrewingProperties maltProperties, Set<ResourceKey<BrewingIngredient.Flavor>> flavors) {
    public static BrewingIngredientComponent DEFAULT = new BrewingIngredientComponent(BrewingIngredient.BrewingProperties.DEFAULT, null, Set.of());

    public static final Codec<BrewingIngredientComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BrewingIngredient.BrewingProperties.CODEC.fieldOf("properties").forGetter(BrewingIngredientComponent::properties),
                    BrewingIngredient.BrewingProperties.CODEC.optionalFieldOf("malt_properties").forGetter(i -> Optional.ofNullable(i.maltProperties())),
                    ResourceKey.codec(AquaVitae.FLAVOR_REGISTRY).listOf().xmap(Set::copyOf, List::copyOf).fieldOf("flavors").forGetter(BrewingIngredientComponent::flavors)
            ).apply(instance, (prop, malt, flavors) -> new BrewingIngredientComponent(prop, malt.orElse(null), flavors)));

    public static final StreamCodec<RegistryFriendlyByteBuf, BrewingIngredientComponent> STREAM_CODEC = StreamCodec.composite(
            BrewingIngredient.BrewingProperties.STREAM_CODEC, BrewingIngredientComponent::properties,
            ByteBufCodecs.optional(BrewingIngredient.BrewingProperties.STREAM_CODEC).map(opt -> opt.orElse(null), Optional::ofNullable), BrewingIngredientComponent::maltProperties,
            ResourceKey.streamCodec(AquaVitae.FLAVOR_REGISTRY).apply(ByteBufCodecs.collection(NonNullList::createWithCapacity)).map(Set::copyOf, NonNullList::copyOf), BrewingIngredientComponent::flavors,
            BrewingIngredientComponent::new
    );
}
