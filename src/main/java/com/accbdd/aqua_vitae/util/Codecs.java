package com.accbdd.aqua_vitae.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class Codecs {
    //hex string parser (no alpha)
    public static final Codec<Integer> HEX_STRING = Codec.STRING.comapFlatMap(
            str -> {
                try {
                    if (str.startsWith("#")) {
                        str = str.substring(1);
                    }
                    if (str.length() != 8) {
                        return DataResult.error(() -> "Hex color must be 8 characters (AARRGGBB)");
                    }
                    int value = (int) Long.parseLong(str, 16);
                    return DataResult.success(value);
                } catch (NumberFormatException e) {
                    String finalStr = str;
                    return DataResult.error(() -> ("Invalid hex color: " + finalStr));
                }
            },
            val -> String.format("%08X", val)
    );

    public static final StreamCodec<FriendlyByteBuf, Integer> HEX_STREAM_CODEC =
            StreamCodec.of(
                    // Writer: Integer -> ByteBuf
                    (buf, value) -> {
                        // Ensure 8-char AARRGGBB
                        String hex = String.format("%08x", value);
                        buf.writeUtf(hex);
                    },

                    // Reader: ByteBuf -> Integer
                    buf -> {
                        String str = buf.readUtf();

                        if (str.startsWith("#")) {
                            str = str.substring(1);
                        }
                        if (str.length() != 8) {
                            throw new IllegalArgumentException("Hex color must be 8 characters (AARRGGBB)");
                        }

                        try {
                            return (int) Long.parseLong(str, 16);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid hex color: " + str);
                        }
                    }
            );

    public static final Codec<MobEffectInstance> EFFECT = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceKey.codec(BuiltInRegistries.MOB_EFFECT.key()).xmap(resourceKey -> BuiltInRegistries.MOB_EFFECT.getHolder(resourceKey).get(), Holder.Reference::getKey).fieldOf("effect").forGetter(inst -> (Holder.Reference<MobEffect>) inst.getEffect()),
                    Codec.INT.fieldOf("duration").forGetter(MobEffectInstance::getDuration),
                    Codec.INT.fieldOf("amplifier").forGetter(MobEffectInstance::getAmplifier)
            ).apply(instance, MobEffectInstance::new)
    );
}
