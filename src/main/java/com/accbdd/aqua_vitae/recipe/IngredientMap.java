package com.accbdd.aqua_vitae.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class IngredientMap {
    public static final Codec<IngredientMap> CODEC = Codec.unboundedMap(ComponentSerialization.FLAT_CODEC, Codec.INT).xmap(IngredientMap::new, IngredientMap::getMap);
    public static final StreamCodec<RegistryFriendlyByteBuf, IngredientMap> STREAM_CODEC = ByteBufCodecs.map(HashMap::new, ComponentSerialization.STREAM_CODEC, ByteBufCodecs.INT).map(IngredientMap::new, IngredientMap::getMap);

    private HashMap<Component, Integer> map;

    public IngredientMap(HashMap<Component, Integer> map) {
        this.map = map;
    }

    public IngredientMap(Map<Component, Integer> map) {
        this.map = new HashMap<>(map);
    }

    public IngredientMap() {
        this(new HashMap<>());
    }

    public HashMap<Component, Integer> getMap() {
        return map;
    }

    public void add(Component name) {
        map.put(name, map.getOrDefault(name, 0) + 1);
    }

    public void add(ItemStack stack) {
        this.add(stack.getHoverName());
    }

    public void add(FluidStack stack) {
        this.add(stack.getHoverName());
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public int getIngredientCount() {
        return map.keySet().stream().reduce(0, (i, component) -> i + map.get(component), Integer::sum);
    }

    public Component getTooltipComponent() {
        final int totalCount = getIngredientCount();
        if (totalCount == 0)
            return Component.translatable("ingredient.aqua_vitae.no_ingredients");

        MutableComponent finalComponent = Component.empty();
        boolean isFirst = true;

        for (Iterator<Map.Entry<Component, Integer>> iterator = this.map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Component, Integer> entry = iterator.next();
            Component nameComponent = entry.getKey();
            int count = entry.getValue();
            float percentage = ((float) count / totalCount) * 100.0f;
            String percentageString = String.format("%.0f%%", percentage);
            MutableComponent ingredientSegment = nameComponent.copy()
                    .append(Component.literal(" (" + percentageString + ")")
                            .withStyle(style -> style.withColor(0xAAAAAA)));

            if (iterator.hasNext())
                finalComponent.append(Component.translatable("grammar.aqua_vitae.list_combine", ingredientSegment).withStyle(style -> style.withColor(0xFFFFFF)));
            else
                finalComponent.append(ingredientSegment);
        }
        
        return Component.translatable("grammar.aqua_vitae.label", Component.translatable("ingredient.aqua_vitae.label").withStyle(ChatFormatting.AQUA), finalComponent);
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
