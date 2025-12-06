package com.accbdd.aqua_vitae.component;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.util.BrewingUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Component for brewing ingredients, used as an override for custom ingredients or for malts which don't have a defined ingredient
 * @param properties
 * @param maltProperties properties when malted, optional
 * @param flavors
 * @param origin the originating brewing ingredient, if this is a malt
 */
public record BrewingIngredientComponent(BrewingIngredient.BrewingProperties properties, @Nullable BrewingIngredient.BrewingProperties maltProperties, Set<ResourceKey<Flavor>> flavors, @Nullable ResourceLocation origin) implements TooltipProvider {
    public static BrewingIngredientComponent DEFAULT = new BrewingIngredientComponent(BrewingIngredient.BrewingProperties.DEFAULT, null, Set.of(), null);

    public static final Codec<BrewingIngredientComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BrewingIngredient.BrewingProperties.CODEC.fieldOf("properties").forGetter(BrewingIngredientComponent::properties),
                    BrewingIngredient.BrewingProperties.CODEC.optionalFieldOf("malt_properties").forGetter(i -> Optional.ofNullable(i.maltProperties())),
                    ResourceKey.codec(AquaVitae.FLAVOR_REGISTRY).listOf().xmap(Set::copyOf, List::copyOf).fieldOf("flavors").forGetter(BrewingIngredientComponent::flavors),
                    ResourceLocation.CODEC.optionalFieldOf("origin").forGetter(i -> Optional.ofNullable(i.origin))
            ).apply(instance, (prop, malt, flavors, origin) -> new BrewingIngredientComponent(prop, malt.orElse(null), flavors, origin.orElse(null))));

    public static final StreamCodec<RegistryFriendlyByteBuf, BrewingIngredientComponent> STREAM_CODEC = StreamCodec.composite(
            BrewingIngredient.BrewingProperties.STREAM_CODEC, BrewingIngredientComponent::properties,
            ByteBufCodecs.optional(BrewingIngredient.BrewingProperties.STREAM_CODEC).map(opt -> opt.orElse(null), Optional::ofNullable), BrewingIngredientComponent::maltProperties,
            ResourceKey.streamCodec(AquaVitae.FLAVOR_REGISTRY).apply(ByteBufCodecs.collection(NonNullList::createWithCapacity)).map(Set::copyOf, NonNullList::copyOf), BrewingIngredientComponent::flavors,
            ResourceLocation.STREAM_CODEC, BrewingIngredientComponent::origin,
            BrewingIngredientComponent::new
    );

    public Component originDescriptionId() {
        return Component.translatable("ingredient.aqua_vitae." + origin.toString());
    }

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        consumer.accept(BrewingUtils.flavorTooltip(this.flavors));
    }
}
