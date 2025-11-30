package com.accbdd.aqua_vitae.util;

import com.accbdd.aqua_vitae.recipe.WortInput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

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

    public static final Codec<List<WortInput>> SORTED_WORT_INPUT_LIST = WortInput.CODEC.listOf().xmap(
            list -> list.stream().sorted(WortInput.COMPARATOR).collect(Collectors.toList()),
            list -> list.stream().sorted(WortInput.COMPARATOR).collect(Collectors.toList())
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<WortInput>> SORTED_WORT_INPUT_LIST_STREAM = StreamCodec.of(
        (buf, inputs) -> {
            List<WortInput> sortedInputs = inputs.stream()
                    .sorted(WortInput.COMPARATOR)
                    .toList();

            buf.writeVarInt(sortedInputs.size());
            for (WortInput input : sortedInputs) {
                WortInput.STREAM_CODEC.encode(buf, input);
            }
        },
        buf -> {
            int size = buf.readVarInt();
            NonNullList<WortInput> readStacks = NonNullList.withSize(size, WortInput.of(ItemStack.EMPTY));
            for (int i = 0; i < size; i++) {
                readStacks.set(i, WortInput.STREAM_CODEC.decode(buf));
            }

            return readStacks.stream()
                    .sorted(WortInput.COMPARATOR)
                    .toList();
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
