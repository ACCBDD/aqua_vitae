package com.accbdd.aqua_vitae.util;

import com.accbdd.aqua_vitae.client.ModKeyMappings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class Constants {
    public static final MutableComponent COMPONENT_INGREDIENTS = Component.translatable("properties.aqua_vitae.ingredients", ModKeyMappings.INGREDIENTS_MAPPING.get().getTranslatedKeyMessage().copy().withStyle(ChatFormatting.AQUA));
    public static final MutableComponent COMPONENT_PROPERTIES = Component.translatable("properties.aqua_vitae.properties", ModKeyMappings.PROPERTIES_MAPPING.get().getTranslatedKeyMessage().copy().withStyle(ChatFormatting.LIGHT_PURPLE));
}
