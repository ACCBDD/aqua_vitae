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
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

/**
 * Stores the ingredients and properties of a precursor fluid for fermentation
 * @param ingredients the items used to make this precursor
 * @param properties the overall properties of the precursor
 * @see com.accbdd.aqua_vitae.recipe.BrewingIngredient.BrewingProperties
 */
public record PrecursorPropertiesComponent(List<ItemStack> ingredients, Set<ResourceKey<BrewingIngredient.Flavor>> flavors, BrewingIngredient.BrewingProperties properties) {
    public static final PrecursorPropertiesComponent EMPTY = new PrecursorPropertiesComponent(List.of(), Set.of(), new BrewingIngredient.BrewingProperties.Builder().build());

    public static final Codec<PrecursorPropertiesComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.CODEC.listOf().fieldOf("items").forGetter(PrecursorPropertiesComponent::ingredients),
                    ResourceKey.codec(AquaVitae.FLAVOR_REGISTRY).listOf().xmap(Set::copyOf, List::copyOf).fieldOf("flavors").forGetter(PrecursorPropertiesComponent::flavors),
                    BrewingIngredient.BrewingProperties.CODEC.fieldOf("properties").forGetter(PrecursorPropertiesComponent::properties)
            ).apply(instance, PrecursorPropertiesComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PrecursorPropertiesComponent> STREAM_CODEC = StreamCodec.composite(
            ItemStack.LIST_STREAM_CODEC, PrecursorPropertiesComponent::ingredients,
            ResourceKey.streamCodec(AquaVitae.FLAVOR_REGISTRY).apply(ByteBufCodecs.collection(NonNullList::createWithCapacity)).map(Set::copyOf, NonNullList::copyOf), PrecursorPropertiesComponent::flavors,
            BrewingIngredient.BrewingProperties.STREAM_CODEC, PrecursorPropertiesComponent::properties,
            PrecursorPropertiesComponent::new
    );
}
