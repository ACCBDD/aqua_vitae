package com.accbdd.aqua_vitae.recipe;

import com.accbdd.aqua_vitae.util.Codecs;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record IngredientColor(int color, int influence) {
    public static Codec<IngredientColor> INFLUENCE_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codecs.HEX_STRING.fieldOf("color").forGetter(IngredientColor::color),
                    Codec.intRange(0, Integer.MAX_VALUE).fieldOf("influence").forGetter(IngredientColor::influence)
            ).apply(instance, IngredientColor::new));
    public static Codec<IngredientColor> SIMPLE_CODEC = Codecs.HEX_STRING.xmap(IngredientColor::new, IngredientColor::color);
    public static Codec<IngredientColor> CODEC = Codec.either(INFLUENCE_CODEC, SIMPLE_CODEC).xmap(either -> either.map(i -> i, i -> i), Either::left);

    public static StreamCodec<ByteBuf, IngredientColor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, IngredientColor::color,
            ByteBufCodecs.INT, IngredientColor::influence,
            IngredientColor::new
    );

    public IngredientColor(int color) {
        this(color, 1);
    }

    public static IngredientColor blendColor(IngredientColor first, IngredientColor second) {
        int c1 = first.color();
        int a1 = (c1 >>> 24) & 0xFF;
        int r1 = (c1 >>> 16) & 0xFF;
        int g1 = (c1 >>> 8) & 0xFF;
        int b1 = (c1 & 0xFF);

        int c2 = second.color();
        int a2 = (c2 >>> 24) & 0xFF;
        int r2 = (c2 >>> 16) & 0xFF;
        int g2 = (c2 >>> 8) & 0xFF;
        int b2 = (c2 & 0xFF);

        float otherWeight = a2 / 255.0f * second.influence();
        float thisWeight = a1 / 255.0f * first.influence();
        float totalWeight = otherWeight + thisWeight;
        float weightCurrentRGB = thisWeight / totalWeight;
        float weightAddedRGB = otherWeight / totalWeight;

        int a = (int) (a1 * weightCurrentRGB + a2 * weightAddedRGB);
        int r = (int) (r1 * weightCurrentRGB + r2 * weightAddedRGB);
        int g = (int) (g1 * weightCurrentRGB + g2 * weightAddedRGB);
        int b = (int) (b1 * weightCurrentRGB + b2 * weightAddedRGB);

        return new IngredientColor((Math.max(a, 80) << 24) | (r << 16) | (g << 8) | b, second.influence() + first.influence());
    }
}
