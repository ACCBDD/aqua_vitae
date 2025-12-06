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
import java.util.Optional;

/**
 * <p>An entry for a 'flavors' - a group of effects applied when a drink is drunk - containing transition information. Transition information is broken up into separate transition categories.</p>
 * <p>When a transition list is null, the flavors is unchanged; when a transition list is empty, the flavors is removed.</p>
 * <p>See also: {@link Transition}</p>
 * @param effects the effects applied when drank
 * @param ferment transition point is ABB content
 * @param distill transition point is ABB content
 * @param age transition point is days
 * @param kiln transition point is kiln count
 * @param malt transition point is ignored
 */
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
            this.effects = new ArrayList<>(effects);
            this.ferment = null;
            this.distill = null;
            this.age = null;
            this.kiln = null;
            this.malt = null;
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

        public Builder ferment(List<ResourceKey<Flavor>> ferment, int transition) {
            this.ferment = new Transition(ferment, transition);
            return this;
        }

        public Builder distill(List<ResourceKey<Flavor>> distill, int transition) {
            this.distill = new Transition(distill, transition);
            return this;
        }

        public Builder age(List<ResourceKey<Flavor>> age, int transition) {
            this.age = new Transition(age, transition);
            return this;
        }

        public Builder kiln(List<ResourceKey<Flavor>> kiln, int transition) {
            this.kiln = new Transition(kiln, transition);
            return this;
        }

        public Builder malt(List<ResourceKey<Flavor>> malt, int transition) {
            this.malt = new Transition(malt, transition);
            return this;
        }
    }

    public static Codec<Flavor> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codecs.EFFECT.listOf().optionalFieldOf("effects", List.of()).forGetter(Flavor::effects),
                    Transition.CODEC.optionalFieldOf("ferment").forGetter(flavor -> Optional.ofNullable(flavor.ferment)),
                    Transition.CODEC.optionalFieldOf("distill").forGetter(flavor -> Optional.ofNullable(flavor.distill)),
                    Transition.CODEC.optionalFieldOf("age").forGetter(flavor -> Optional.ofNullable(flavor.age)),
                    Transition.CODEC.optionalFieldOf("kiln").forGetter(flavor -> Optional.ofNullable(flavor.kiln)),
                    Transition.CODEC.optionalFieldOf("malt").forGetter(flavor -> Optional.ofNullable(flavor.malt))
            ).apply(instance, (effects, ferment, distill, age, kiln, malt) -> new Flavor(effects,
                    ferment.orElse(null),
                    distill.orElse(null),
                    age.orElse(null),
                    kiln.orElse(null),
                    malt.orElse(null)))
    );

    public record Transition(List<ResourceKey<Flavor>> flavors, int transitionPoint) {
        public static final Codec<Transition> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ResourceKey.codec(AquaVitae.FLAVOR_REGISTRY).listOf().fieldOf("flavors").forGetter(Transition::flavors),
                        Codec.INT.fieldOf("transition_point").forGetter(Transition::transitionPoint)
                ).apply(instance, Transition::new));
    }
}
