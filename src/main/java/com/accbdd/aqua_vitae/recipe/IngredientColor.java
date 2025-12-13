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
}
