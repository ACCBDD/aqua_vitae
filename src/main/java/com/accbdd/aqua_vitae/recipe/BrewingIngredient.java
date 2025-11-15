package com.accbdd.aqua_vitae.recipe;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.util.Codecs;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record BrewingIngredient(@Nullable Ingredient itemIngredient, @Nullable FluidIngredient fluidIngredient, int color, float starch, float sugar, float yeast, float yeastTolerance, float diastaticPower, List<ResourceKey<Flavor>> flavors, MaltProperties maltProperties) {

    public Either<Ingredient, FluidIngredient> input() {
        if (itemIngredient == null) {
            return Either.right(fluidIngredient);
        } else {
            return Either.left(itemIngredient);
        }
    }

    public static class Builder {
        Ingredient itemIngredient;
        FluidIngredient fluidIngredient;
        int color;
        float starch;
        float sugar;
        float yeast;
        float yeastTolerance;
        float diastaticPower;
        List<ResourceKey<Flavor>> flavors;
        MaltProperties maltProperties;

        private Builder(Ingredient itemIngredient, FluidIngredient fluidIngredient) {
            this.itemIngredient = itemIngredient;
            this.fluidIngredient = fluidIngredient;
            this.color = 0x00000000;
            this.starch = 0;
            this.sugar = 0;
            this.yeast = 0;
            this.yeastTolerance = 0;
            this.diastaticPower = 0;
            this.flavors = List.of();
            this.maltProperties = null;
        }

        public Builder(Ingredient itemIngredient) {
            this(itemIngredient, null);
        }

        public Builder(FluidIngredient fluidIngredient) {
            this(null, fluidIngredient);
        }

        private static Builder copy(Ingredient itemIngredient, FluidIngredient fluidIngredient, BrewingIngredient copied) {
            Builder builder = new Builder(itemIngredient, fluidIngredient);
            builder.itemIngredient = itemIngredient;
            builder.fluidIngredient = fluidIngredient;
            builder.color = copied.color;
            builder.starch = copied.starch;
            builder.sugar = copied.sugar;
            builder.yeast = copied.yeast;
            builder.yeastTolerance = copied.yeastTolerance;
            builder.diastaticPower = copied.diastaticPower;
            builder.flavors = copied.flavors;
            builder.maltProperties = copied.maltProperties;
            return builder;
        }

        public static Builder copy(Ingredient itemIngredient, BrewingIngredient copied) {
            return Builder.copy(itemIngredient, null, copied);
        }

        public static Builder copy(FluidIngredient fluidIngredient, BrewingIngredient copied) {
            return Builder.copy(null, fluidIngredient, copied);
        }

        public BrewingIngredient build() {
            return new BrewingIngredient(itemIngredient, fluidIngredient, color, starch, sugar, yeast, yeastTolerance, diastaticPower, flavors, maltProperties);
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder starch(float starch) {
            this.starch = starch;
            return this;
        }

        public Builder sugar(float sugar) {
            this.sugar = sugar;
            return this;
        }

        public Builder yeast(float yeast) {
            this.yeast = yeast;
            return this;
        }

        public Builder yeastTolerance(float yeastTolerance) {
            this.yeastTolerance = yeastTolerance;
            return this;
        }

        public Builder diastaticPower(float diastaticPower) {
            this.diastaticPower = diastaticPower;
            return this;
        }

        public Builder flavors(List<ResourceKey<Flavor>> flavors) {
            this.flavors = flavors;
            return this;
        }

        public Builder maltProperties(int color, float starch, float diastaticPower) {
            this.maltProperties = new MaltProperties(color, starch, diastaticPower);
            return this;
        }
    }

    public static Codec<BrewingIngredient> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.either(Ingredient.CODEC, FluidIngredient.CODEC).fieldOf("input").forGetter(BrewingIngredient::input),
                    Codecs.HEX_STRING.fieldOf("color").forGetter(BrewingIngredient::color),
                    Codec.floatRange(0, 1).fieldOf("starch").forGetter(BrewingIngredient::starch),
                    Codec.floatRange(0, 1).fieldOf("sugar").forGetter(BrewingIngredient::sugar),
                    Codec.floatRange(0, 1).fieldOf("yeast").forGetter(BrewingIngredient::yeast),
                    Codec.floatRange(0, 1).fieldOf("yeast_tolerance").forGetter(BrewingIngredient::yeastTolerance),
                    Codec.floatRange(0, 1).fieldOf("diastatic_power").forGetter(BrewingIngredient::diastaticPower),
                    ResourceKey.codec(AquaVitae.FLAVOR_REGISTRY).listOf().fieldOf("flavors").forGetter(BrewingIngredient::flavors),
                    MaltProperties.CODEC.optionalFieldOf("malt_properties").forGetter(i -> Optional.ofNullable(i.maltProperties()))
            ).apply(instance, (i, color, starch, sugar, yeast, yeast_tol, dia_pow, flavors, malt) ->
                i.map(
                        item -> new BrewingIngredient(item, null, color, starch, sugar, yeast, yeast_tol, dia_pow, flavors, malt.orElse(null)),
                        fluid -> new BrewingIngredient(null, fluid, color, starch, sugar, yeast, yeast_tol, dia_pow, flavors, malt.orElse(null)))
            )
    );

    public record MaltProperties(int color, float starch, float diastaticPower) {
        public static Codec<MaltProperties> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codecs.HEX_STRING.fieldOf("color").forGetter(MaltProperties::color),
                        Codec.FLOAT.fieldOf("starch").forGetter(MaltProperties::starch),
                        Codec.FLOAT.fieldOf("diastatic_power").forGetter(MaltProperties::diastaticPower)
                ).apply(instance, MaltProperties::new)
        );
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
