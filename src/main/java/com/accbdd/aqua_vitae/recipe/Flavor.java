package com.accbdd.aqua_vitae.recipe;

import com.accbdd.aqua_vitae.util.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;

public record Flavor(List<MobEffectInstance> effects) {
    //todo: add flavor transition mapping - distilling, aging, kilning, malting, etc.

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
