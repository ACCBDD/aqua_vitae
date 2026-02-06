package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.api.naming.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModDrinkPredicateType {
    public static final ResourceKey<Registry<DrinkPredicateType>> DRINK_PREDICATE_TYPE_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MODID, "drink_predicate_type"));
    public static final Registry<DrinkPredicateType> DRINK_PREDICATE_TYPES = new RegistryBuilder<>(DRINK_PREDICATE_TYPE_KEY)
            .sync(false)
            .create();

    public static final DeferredRegister<DrinkPredicateType> DRINK_PREDICATE_TYPE = DeferredRegister.create(DRINK_PREDICATE_TYPES, MODID);

    public static final DeferredHolder<DrinkPredicateType, DrinkPredicateType> MINIMUM_ABV = DRINK_PREDICATE_TYPE.register("minimum_abv", () -> MinimumABVPredicate.DRINK_PREDICATE_TYPE);
    public static final DeferredHolder<DrinkPredicateType, DrinkPredicateType> MAXIMUM_ABV = DRINK_PREDICATE_TYPE.register("maximum_abv", () -> MaximumABVPredicate.DRINK_PREDICATE_TYPE);
    public static final DeferredHolder<DrinkPredicateType, DrinkPredicateType> ABV_RANGE = DRINK_PREDICATE_TYPE.register("abv_range", () -> RangeABVPredicate.DRINK_PREDICATE_TYPE);
    public static final DeferredHolder<DrinkPredicateType, DrinkPredicateType> MINIMUM_AGE = DRINK_PREDICATE_TYPE.register("minimum_age", () -> MinimumAgePredicate.DRINK_PREDICATE_TYPE);
    public static final DeferredHolder<DrinkPredicateType, DrinkPredicateType> MAXIMUM_AGE = DRINK_PREDICATE_TYPE.register("maximum_age", () -> MaximumAgePredicate.DRINK_PREDICATE_TYPE);
    public static final DeferredHolder<DrinkPredicateType, DrinkPredicateType> AGE_RANGE = DRINK_PREDICATE_TYPE.register("age_range", () -> RangeAgePredicate.DRINK_PREDICATE_TYPE);
    public static final DeferredHolder<DrinkPredicateType, DrinkPredicateType> INGREDIENT = DRINK_PREDICATE_TYPE.register("ingredient", () -> IngredientPredicate.DRINK_PREDICATE_TYPE);
    public static final DeferredHolder<DrinkPredicateType, DrinkPredicateType> MALT = DRINK_PREDICATE_TYPE.register("malt_ingredient", () -> MaltPredicate.DRINK_PREDICATE_TYPE);
    public static final DeferredHolder<DrinkPredicateType, DrinkPredicateType> NOT = DRINK_PREDICATE_TYPE.register("not", () -> NotPredicate.DRINK_PREDICATE_TYPE);
    public static final DeferredHolder<DrinkPredicateType, DrinkPredicateType> ANY_OF = DRINK_PREDICATE_TYPE.register("any_of", () -> AnyOfPredicate.DRINK_PREDICATE_TYPE);
    public static final DeferredHolder<DrinkPredicateType, DrinkPredicateType> ALL_OF = DRINK_PREDICATE_TYPE.register("all_of", () -> AllOfPredicate.DRINK_PREDICATE_TYPE);

}
