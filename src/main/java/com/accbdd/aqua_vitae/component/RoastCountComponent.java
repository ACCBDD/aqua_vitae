package com.accbdd.aqua_vitae.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record RoastCountComponent(int roast) implements TooltipProvider {
    public static final Codec<RoastCountComponent> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.INT.fieldOf("roast").forGetter(RoastCountComponent::roast)
            ).apply(inst, RoastCountComponent::new));

    public static final StreamCodec<ByteBuf, RoastCountComponent> STREAM_CODEC = ByteBufCodecs.INT.map(RoastCountComponent::new, RoastCountComponent::roast);

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        if (roast < 5)
            consumer.accept(Component.translatable("ingredient.aqua_vitae.roast."+roast));
        else
            consumer.accept(Component.translatable("ingredient.aqua_vitae.roast.5"));
    }
}
