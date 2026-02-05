package com.accbdd.aqua_vitae.api;

import com.accbdd.aqua_vitae.component.BrewingIngredientComponent;
import com.accbdd.aqua_vitae.component.RoastCountComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.util.BrewingUtils;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class IngredientMap {
    public static final Codec<IngredientMap> CODEC = Codec.unboundedMap(Codec.STRING, Codec.INT).xmap(IngredientMap::new, IngredientMap::getMap);
    public static final StreamCodec<ByteBuf, IngredientMap> STREAM_CODEC = ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.INT).map(IngredientMap::new, IngredientMap::getMap);

    private HashMap<String, Integer> map;

    public IngredientMap(HashMap<String, Integer> map) {
        this.map = map;
    }

    public IngredientMap(Map<String, Integer> map) {
        this.map = new HashMap<>(map);
    }

    public IngredientMap() {
        this(new HashMap<>());
    }

    public HashMap<String, Integer> getMap() {
        return map;
    }

    public void add(String key) {
        map.put(key, map.getOrDefault(key, 0) + 1);
    }

    public void add(ItemStack stack) {
        ResourceLocation ingredientLoc = BrewingUtils.getIngredientLoc(stack);
        if (ingredientLoc != null) {
            this.add(ingredientLoc.toString());
        } else if (stack.has(ModComponents.BREWING_INGREDIENT)) {
            BrewingIngredientComponent brewingIngredient = stack.get(ModComponents.BREWING_INGREDIENT);
            if (brewingIngredient.origin() != null) {
                String key = brewingIngredient.origin().toString();
                if (stack.has(ModComponents.ROAST_COUNTER)) { //store as malt for name decoding
                    key += "." + stack.get(ModComponents.ROAST_COUNTER).roast() + ".malt";
                }
                this.add(key);
            }
        }
    }

    public void add(FluidStack stack) {
        ResourceLocation ingredientLoc = BrewingUtils.getIngredientLoc(stack);
        if (ingredientLoc != null)
            this.add(ingredientLoc.toString());
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public int getIngredientCount() {
        return map.keySet().stream().reduce(0, (i, location) -> i + map.get(location), Integer::sum);
    }

    public Component getTooltipComponent() {
        final int totalCount = getIngredientCount();
        if (totalCount == 0)
            return Component.translatable("ingredient.aqua_vitae.no_ingredients");

        MutableComponent finalComponent = Component.empty();

        for (Iterator<Map.Entry<String, Integer>> iterator = this.map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Integer> entry = iterator.next();
            String key = entry.getKey();
            Component nameComponent = Component.translatable("ingredient.aqua_vitae." + entry.getKey());
            if (key.endsWith(".malt")) { //handle malt naming
                int numberIndex = key.length() - 6;
                nameComponent = Component.translatable("grammar.aqua_vitae.malt",
                        RoastCountComponent.getRoastName(Integer.parseInt(key.substring(numberIndex, numberIndex+1))),
                        Component.translatable("ingredient.aqua_vitae." + key.substring(0, numberIndex - 1)),
                        Component.translatable("item.aqua_vitae.malt"));
            }

            int count = entry.getValue();
            float percentage = ((float) count / totalCount) * 100.0f;
            String percentageString = String.format("%.0f%%", percentage);
            MutableComponent ingredientSegment = nameComponent.copy()
                    .append(Component.literal(" (" + percentageString + ")")
                            .withStyle(style -> style.withColor(0xAAAAAA)));

            if (iterator.hasNext())
                finalComponent.append(Component.translatable("grammar.aqua_vitae.list_combine", ingredientSegment)
                        .withStyle(style -> style.withColor(0xFFFFFF)));
            else
                finalComponent.append(ingredientSegment);
        }
        
        return Component.translatable("grammar.aqua_vitae.label",
                Component.translatable("ingredient.aqua_vitae.label")
                        .withStyle(ChatFormatting.AQUA),
                finalComponent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientMap that)) return false;
        return Objects.equals(getMap(), that.getMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMap());
    }
}
