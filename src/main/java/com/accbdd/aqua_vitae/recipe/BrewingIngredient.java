package com.accbdd.aqua_vitae.recipe;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.util.Codecs;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record BrewingIngredient(@Nullable Ingredient itemIngredient, @Nullable FluidIngredient fluidIngredient,
                                BrewingProperties properties, BrewingProperties maltProperties,
                                List<ResourceKey<Flavor>> flavors) {

    public BrewingIngredient(Ingredient itemIngredient, BrewingProperties properties, List<ResourceKey<Flavor>> flavors) {
        this(itemIngredient, null, properties, null, flavors);
    }

    public BrewingIngredient(Ingredient itemIngredient, BrewingProperties properties, BrewingProperties maltProperties, List<ResourceKey<Flavor>> flavors) {
        this(itemIngredient, null, properties, maltProperties, flavors);
    }

    public BrewingIngredient(FluidIngredient fluidIngredient, BrewingProperties properties, List<ResourceKey<Flavor>> flavors) {
        this(null, fluidIngredient, properties, null, flavors);
    }


    public Either<Ingredient, FluidIngredient> input() {
        if (itemIngredient == null) {
            return Either.right(fluidIngredient);
        } else {
            return Either.left(itemIngredient);
        }
    }

    public static Codec<BrewingIngredient> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.either(Ingredient.CODEC, FluidIngredient.CODEC).fieldOf("input").forGetter(BrewingIngredient::input),
                    BrewingProperties.CODEC.fieldOf("properties").forGetter(BrewingIngredient::properties),
                    BrewingProperties.CODEC.optionalFieldOf("malt_properties").forGetter(i -> Optional.ofNullable(i.maltProperties())),
                    ResourceKey.codec(AquaVitae.FLAVOR_REGISTRY).listOf().fieldOf("flavors").forGetter(BrewingIngredient::flavors)
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
    public record BrewingProperties(int color, int starch, int sugar, int yeast, int yeastTolerance,
                                    int diastaticPower) {
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
                Codecs.HEX_STREAM_CODEC, BrewingProperties::color,
                ByteBufCodecs.INT, BrewingProperties::starch,
                ByteBufCodecs.INT, BrewingProperties::sugar,
                ByteBufCodecs.INT, BrewingProperties::yeast,
                ByteBufCodecs.INT, BrewingProperties::yeastTolerance,
                ByteBufCodecs.INT, BrewingProperties::diastaticPower,
                BrewingProperties::new
        );

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

    public record Flavor(List<MobEffectInstance> effects) {

        public static class Builder {
            List<MobEffectInstance> effects;

            public Builder() {
                this.effects = new ArrayList<>();
            }

            public Flavor build() {
                return new Flavor(effects);
            }

            public Builder effect(MobEffectInstance effect) {
                this.effects.add(effect);
                return this;
            }
        }

        public static Codec<Flavor> CODEC = Codecs.EFFECT.listOf().xmap(Flavor::new, Flavor::effects);

    }
}
