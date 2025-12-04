package com.accbdd.aqua_vitae.recipe;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.util.Codecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public record Flavor(List<MobEffectInstance> effects, @Nullable Transition ferment, @Nullable Transition distill, @Nullable Transition age, @Nullable Transition kiln, @Nullable Transition malt) {
    public static class Builder {
        List<MobEffectInstance> effects;
        Transition ferment, distill, age, kiln, malt;

        public Builder() {
            this.effects = new ArrayList<>();
            this.ferment = null;
            this.distill = null;
            this.age = null;
            this.kiln = null;
            this.malt = null;
        }

        private Builder(List<MobEffectInstance> effects, Transition ferment, Transition distill, Transition age, Transition kiln, Transition malt) {
            this.effects = List.copyOf(effects);
            this.ferment = ferment;
            this.distill = distill;
            this.age = age;
            this.kiln = kiln;
            this.malt = malt;
        }

        public static Builder of(Flavor flavor) {
            return new Builder(flavor.effects, flavor.ferment, flavor.distill, flavor.age, flavor.kiln, flavor.malt);
        }

        public Flavor build() {
            return new Flavor(effects, ferment, distill, age, kiln, malt);
        }

        public Builder effect(MobEffectInstance effect) {
            this.effects.add(effect);
            return this;
        }

        public Builder ferment(ResourceKey<Flavor> ferment, int transition) {
            this.ferment = new Transition(ferment, transition);
            return this;
        }

        public Builder distill(ResourceKey<Flavor> distill, int transition) {
            this.distill = new Transition(distill, transition);
            return this;
        }

        public Builder age(ResourceKey<Flavor> age, int transition) {
            this.age = new Transition(age, transition);
            return this;
        }

        public Builder kiln(ResourceKey<Flavor> kiln, int transition) {
            this.kiln = new Transition(kiln, transition);
            return this;
        }

        public Builder malt(ResourceKey<Flavor> malt, int transition) {
            this.malt = new Transition(malt, transition);
            return this;
        }
    }

    public static Codec<Flavor> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codecs.EFFECT.listOf().fieldOf("effects").forGetter(Flavor::effects),
                    Transition.CODEC.optionalFieldOf("ferment", null).forGetter(Flavor::ferment),
                    Transition.CODEC.optionalFieldOf("distill", null).forGetter(Flavor::distill),
                    Transition.CODEC.optionalFieldOf("age", null).forGetter(Flavor::age),
                    Transition.CODEC.optionalFieldOf("kiln", null).forGetter(Flavor::kiln),
                    Transition.CODEC.optionalFieldOf("malt", null).forGetter(Flavor::malt)
            ).apply(instance, Flavor::new)
    );

    public record Transition(ResourceKey<Flavor> flavor, int transitionPoint) {
        public static final Codec<Transition> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ResourceKey.codec(AquaVitae.FLAVOR_REGISTRY).fieldOf("flavor").forGetter(Transition::flavor),
                        Codec.INT.fieldOf("transition_point").forGetter(Transition::transitionPoint)
                ).apply(instance, Transition::new));
    }
}
