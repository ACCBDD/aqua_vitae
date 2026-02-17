package com.accbdd.aqua_vitae.api.naming;

import com.accbdd.aqua_vitae.registry.ModComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Set;


/**
 * checks if ONLY the listed ingredients are present in IngredientMap
 */
public class StrictIngredientsPredicate implements DrinkPredicate {
    public static final MapCodec<StrictIngredientsPredicate> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.listOf().fieldOf("ingredients").forGetter(pred -> pred.ingredients)
            ).apply(instance, StrictIngredientsPredicate::new));
    public static final DrinkPredicateType DRINK_PREDICATE_TYPE = new DrinkPredicateType(CODEC);

    private final List<String> ingredients;
    private final Set<String> ingredientSet;

    public StrictIngredientsPredicate(List<String> ingredients) {
        this.ingredients = ingredients;
        this.ingredientSet = Set.copyOf(ingredients);
    }

    @Override
    public DrinkPredicateType getType() {
        return DRINK_PREDICATE_TYPE;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        if (fluidStack.has(ModComponents.ALCOHOL_PROPERTIES)) {
            Set<String> alcoholSet = fluidStack.get(ModComponents.ALCOHOL_PROPERTIES).inputs().getMap().keySet();
            if (alcoholSet.size() != ingredientSet.size()) {
                return false;
            }
            for (String key : alcoholSet) {
                if (!ingredientSet.contains(key))
                    return false;
            }
        }
        return true;
    }
}
