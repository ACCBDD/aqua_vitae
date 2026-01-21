package com.accbdd.aqua_vitae.component;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.recipe.IngredientColor;
import com.accbdd.aqua_vitae.recipe.IngredientMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.Set;

/**
 * @param color
 * @param abv     alcohol per bucket - 1000 should be granular enough?
 * @param age     age in age cycles
 * @param flavors
 * @param inputs
 */
public record AlcoholPropertiesComponent(IngredientColor color, float abv, int age,
                                         Set<ResourceKey<Flavor>> flavors, IngredientMap inputs) {

    public static final AlcoholPropertiesComponent EMPTY = new AlcoholPropertiesComponent(new IngredientColor(0), 0, 0, Set.of(), new IngredientMap());

    public static final Codec<AlcoholPropertiesComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    IngredientColor.CODEC.fieldOf("color").forGetter(AlcoholPropertiesComponent::color),
                    Codec.FLOAT.fieldOf("abv").forGetter(AlcoholPropertiesComponent::abv),
                    Codec.INT.fieldOf("age").forGetter(AlcoholPropertiesComponent::age),
                    ResourceKey.codec(AquaVitae.FLAVOR_REGISTRY).listOf().xmap(Set::copyOf, List::copyOf).fieldOf("flavors").forGetter(AlcoholPropertiesComponent::flavors),
                    IngredientMap.CODEC.fieldOf("inputs").forGetter(AlcoholPropertiesComponent::inputs)
            ).apply(instance, AlcoholPropertiesComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AlcoholPropertiesComponent> STREAM_CODEC = StreamCodec.composite(
            IngredientColor.STREAM_CODEC, AlcoholPropertiesComponent::color,
            ByteBufCodecs.FLOAT, AlcoholPropertiesComponent::abv,
            ByteBufCodecs.INT, AlcoholPropertiesComponent::age,
            ResourceKey.streamCodec(AquaVitae.FLAVOR_REGISTRY).apply(ByteBufCodecs.collection(NonNullList::createWithCapacity)).map(Set::copyOf, NonNullList::copyOf), AlcoholPropertiesComponent::flavors,
            IngredientMap.STREAM_CODEC, AlcoholPropertiesComponent::inputs,
            AlcoholPropertiesComponent::new
    );
}
