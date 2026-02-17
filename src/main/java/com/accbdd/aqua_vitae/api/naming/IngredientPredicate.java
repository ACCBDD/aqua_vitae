package com.accbdd.aqua_vitae.api.naming;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.api.BrewingIngredient;
import com.accbdd.aqua_vitae.api.IngredientMap;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.server.ServerLifecycleHooks;


/**
 * checks if ingredient is present in IngredientMap, supports ingredient tags
 */
public class IngredientPredicate implements DrinkPredicate {
    public static final MapCodec<IngredientPredicate> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.fieldOf("ingredient").forGetter(pred -> pred.ingredientKey)
            ).apply(instance, IngredientPredicate::new));
    public static final DrinkPredicateType DRINK_PREDICATE_TYPE = new DrinkPredicateType(CODEC);

    private final String ingredientKey;

    public IngredientPredicate(String ingredientKey) {
        this.ingredientKey = ingredientKey;
    }

    @Override
    public DrinkPredicateType getType() {
        return DRINK_PREDICATE_TYPE;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        if (!fluidStack.has(ModComponents.ALCOHOL_PROPERTIES)) return false;

        IngredientMap ingredientMap = fluidStack.get(ModComponents.ALCOHOL_PROPERTIES).inputs();

        if (!ingredientKey.startsWith("#")) {
            return ingredientMap.getMap().containsKey(ingredientKey);
        }

        return checkTags(ingredientMap);
    }

    private boolean checkTags(IngredientMap ingredientMap) {
        Registry<BrewingIngredient> registry = ServerLifecycleHooks.getCurrentServer()
                .registryAccess()
                .registryOrThrow(AquaVitae.INGREDIENT_REGISTRY);

        TagKey<BrewingIngredient> tagKey = TagKey.create(
                AquaVitae.INGREDIENT_REGISTRY,
                ResourceLocation.parse(ingredientKey.substring(1))
        );

        for (String key : ingredientMap.getMap().keySet()) {
            // strip .malt
            String baseId = key.split("\\.")[0];

            var holder = registry.getHolder(ResourceKey.create(AquaVitae.INGREDIENT_REGISTRY, ResourceLocation.parse(baseId)));

            if (holder.isPresent() && holder.get().is(tagKey)) {
                return true;
            }
        }
        return false;
    }
}
