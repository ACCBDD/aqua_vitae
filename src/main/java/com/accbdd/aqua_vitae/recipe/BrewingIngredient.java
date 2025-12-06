package com.accbdd.aqua_vitae.recipe;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.component.BrewingIngredientComponent;
import com.accbdd.aqua_vitae.component.RoastCountComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.registry.ModItems;
import com.accbdd.aqua_vitae.util.BrewingUtils;
import com.accbdd.aqua_vitae.util.Codecs;
import com.accbdd.aqua_vitae.util.NumUtils;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public record BrewingIngredient(@Nullable Ingredient itemIngredient, @Nullable FluidIngredient fluidIngredient,
                                BrewingProperties properties, @Nullable BrewingProperties maltProperties,
                                Set<ResourceKey<Flavor>> flavors) {

    public BrewingIngredient(Ingredient itemIngredient, BrewingProperties properties, BrewingProperties maltProperties, Set<ResourceKey<Flavor>> flavors) {
        this(itemIngredient, null, properties, maltProperties, flavors);
    }

    public BrewingIngredient(FluidIngredient fluidIngredient, BrewingProperties properties, BrewingProperties maltProperties, Set<ResourceKey<Flavor>> flavors) {
        this(null, fluidIngredient, properties, maltProperties, flavors);
    }

    public BrewingIngredient(Ingredient itemIngredient, BrewingProperties properties, Set<ResourceKey<Flavor>> flavors) {
        this(itemIngredient, properties, null, flavors);
    }

    public BrewingIngredient(FluidIngredient fluidIngredient, BrewingProperties properties, Set<ResourceKey<Flavor>> flavors) {
        this(fluidIngredient, properties, null, flavors);
    }

    public Either<Ingredient, FluidIngredient> input() {
        if (itemIngredient == null) {
            return Either.right(fluidIngredient);
        } else {
            return Either.left(itemIngredient);
        }
    }

    /**
     * @return malt output itemstack, or empty if nonexistent
     */
    public ItemStack maltOutput() {
        if (maltProperties() == null)
            return ItemStack.EMPTY;

        ItemStack stack = new ItemStack(ModItems.MALT.get(), 1);
        stack.set(ModComponents.BREWING_INGREDIENT.get(), new BrewingIngredientComponent(maltProperties(), null, this.flavors, BrewingUtils.getIngredientKey(this)));
        stack.set(ModComponents.ROAST_COUNTER.get(), new RoastCountComponent(1));
        return stack;
    }

    public static Codec<BrewingIngredient> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.either(Ingredient.CODEC, FluidIngredient.CODEC).fieldOf("input").forGetter(BrewingIngredient::input),
                    BrewingProperties.CODEC.fieldOf("properties").forGetter(BrewingIngredient::properties),
                    BrewingProperties.CODEC.optionalFieldOf("malt_properties").forGetter(i -> Optional.ofNullable(i.maltProperties())),
                    ResourceKey.codec(AquaVitae.FLAVOR_REGISTRY).listOf().xmap(Set::copyOf, List::copyOf).fieldOf("flavors").forGetter(BrewingIngredient::flavors)
            ).apply(instance, (i, properties, malt, flavors) ->
                    i.map(
                            item -> new BrewingIngredient(item, null, properties, malt.orElse(null), flavors),
                            fluid -> new BrewingIngredient(null, fluid, properties, malt.orElse(null), flavors))
            )
    );

    /**
     * @param color
     * @param starch
     * @param sugar
     * @param yeast
     * @param yeastTolerance in terms of alcohol per bucket
     * @param diastaticPower
     */
    public record BrewingProperties(int color, int starch, int sugar, int yeast, int yeastTolerance, int diastaticPower) {
        public static BrewingProperties DEFAULT = new BrewingProperties(0x00000000, 0, 0, 0, 0, 0);

        public static Codec<BrewingProperties> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codecs.HEX_STRING.fieldOf("color").forGetter(BrewingProperties::color),
                        Codec.intRange(0, Integer.MAX_VALUE).fieldOf("starch").forGetter(BrewingProperties::starch),
                        Codec.intRange(0, Integer.MAX_VALUE).fieldOf("sugar").forGetter(BrewingProperties::sugar),
                        Codec.intRange(0, Integer.MAX_VALUE).fieldOf("yeast").forGetter(BrewingProperties::yeast),
                        Codec.intRange(0, 1000).fieldOf("yeast_tolerance").forGetter(BrewingProperties::yeastTolerance),
                        Codec.intRange(0, Integer.MAX_VALUE).fieldOf("diastatic_power").forGetter(BrewingProperties::diastaticPower)
                ).apply(instance, BrewingProperties::new)
        );

        public static StreamCodec<FriendlyByteBuf, BrewingProperties> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, BrewingProperties::color,
                ByteBufCodecs.INT, BrewingProperties::starch,
                ByteBufCodecs.INT, BrewingProperties::sugar,
                ByteBufCodecs.INT, BrewingProperties::yeast,
                ByteBufCodecs.INT, BrewingProperties::yeastTolerance,
                ByteBufCodecs.INT, BrewingProperties::diastaticPower,
                BrewingProperties::new
        );

        public BrewingProperties copy() {
            return new BrewingProperties(color, starch, sugar, yeast, yeastTolerance, diastaticPower);
        }

        /**
         * Adds two BrewingProperties together with a weight
         *
         * @param other  to add
         * @param weight the weight this object should have in comparison to the other
         * @return a new combined BrewingProperties object
         */
        public BrewingProperties add(BrewingProperties other, int weight) {
            int newColor = blendColor(other);

            return new BrewingProperties(newColor,
                    this.starch + other.starch,
                    this.sugar + other.sugar,
                    this.yeast + other.yeast,
                    Math.max(this.yeastTolerance, other.yeastTolerance), //max for yeast
                    (this.diastaticPower * weight + other.diastaticPower) / (weight + 1)); //average DP
        }

        /**
         * @return a kilned version of the property - darker colors, lower diastatic power, etc.
         */
        public BrewingProperties kiln() {
            int kilnColor = NumUtils.saturateColor(this.color, 0.4f);
            return new BrewingProperties(NumUtils.darkenColor(kilnColor, 0.25f),
                    (int) (this.starch * 0.95),
                    (int) (this.sugar * 0.95),
                    (int) (this.yeast * 0.95),
                    this.yeastTolerance,
                    (int) (this.diastaticPower * 0.75));
        }

        private int blendColor(BrewingProperties other) {

            int c1 = this.color();
            int a1 = (c1 >>> 24) & 0xFF;
            int r1 = (c1 >>> 16) & 0xFF;
            int g1 = (c1 >>> 8) & 0xFF;
            int b1 = (c1 & 0xFF);

            int c2 = other.color();
            int a2 = (c2 >>> 24) & 0xFF;
            int r2 = (c2 >>> 16) & 0xFF;
            int g2 = (c2 >>> 8) & 0xFF;
            int b2 = (c2 & 0xFF);

            float otherWeight = a2 / 255.0f;
            float thisWeight = a1 / 255.0f;
            float totalWeight = otherWeight + thisWeight;
            float weightCurrentRGB = thisWeight / totalWeight;
            float weightAddedRGB = otherWeight / totalWeight;

            int a = (int) (a1 * weightCurrentRGB + a2 * weightAddedRGB);
            int r = (int) (r1 * weightCurrentRGB + r2 * weightAddedRGB);
            int g = (int) (g1 * weightCurrentRGB + g2 * weightAddedRGB);
            int b = (int) (b1 * weightCurrentRGB + b2 * weightAddedRGB);

            return (Math.max(a, 80) << 24) | (r << 16) | (g << 8) | b;
        }

        public static class Builder {
            int color;
            int starch;
            int sugar;
            int yeast;
            int yeastTolerance;
            int diastaticPower;

            public Builder() {
                this.color = 0xDDFFFFFF;
                this.starch = 0;
                this.sugar = 0;
                this.yeast = 0;
                this.yeastTolerance = 0;
                this.diastaticPower = 0;
            }

            public Builder copy() {
                Builder builder = new Builder();
                builder.color = this.color;
                builder.starch = this.starch;
                builder.sugar = this.sugar;
                builder.yeast = this.yeast;
                builder.yeastTolerance = this.yeastTolerance;
                builder.diastaticPower = this.diastaticPower;
                return builder;
            }

            public BrewingProperties build() {
                return new BrewingProperties(color, starch, sugar, yeast, yeastTolerance, diastaticPower);
            }

            public Builder color(int color) {
                this.color = color;
                return this;
            }

            public Builder starch(int starch) {
                this.starch = starch;
                return this;
            }

            public Builder sugar(int sugar) {
                this.sugar = sugar;
                return this;
            }

            public Builder yeast(int yeast) {
                this.yeast = yeast;
                return this;
            }

            public Builder yeastTolerance(int yeastTolerance) {
                this.yeastTolerance = yeastTolerance;
                return this;
            }

            public Builder diastaticPower(int diastaticPower) {
                this.diastaticPower = diastaticPower;
                return this;
            }
        }
    }
}
