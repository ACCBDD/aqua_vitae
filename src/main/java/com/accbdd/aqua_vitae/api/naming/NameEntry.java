package com.accbdd.aqua_vitae.api.naming;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record NameEntry(String key, List<DrinkPredicate> predicates, int priority) {
    public static Codec<NameEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("key").forGetter(NameEntry::key),
                    DrinkPredicate.CODEC.listOf().fieldOf("predicates").forGetter(NameEntry::predicates),
                    Codec.INT.optionalFieldOf("priority", 0).forGetter(NameEntry::priority)
            ).apply(instance, NameEntry::new));

    public boolean test(FluidStack fluid) {
        for (DrinkPredicate predicate : predicates) {
            if (!predicate.test(fluid)) {
                return false;
            }
        }
        return true;
    }

    public Component getComponent() {
        return Component.translatable(key);
    }
}
