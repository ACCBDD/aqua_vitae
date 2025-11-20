package com.accbdd.aqua_vitae.compat.jade;

import com.accbdd.aqua_vitae.block.entity.PotStillBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.JadeIds;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IDisplayHelper;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.ProgressStyle;
import snownee.jade.util.CommonProxy;
import snownee.jade.util.FluidTextHelper;

public class PotStillProvider implements IBlockComponentProvider {
    public static PotStillProvider INSTANCE = new PotStillProvider();

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
        if (accessor.getBlockEntity() instanceof PotStillBlockEntity still) {
            IFluidHandler handler = still.getFluidHandler();
            IElementHelper helper = IElementHelper.get();
            IWailaConfig.HandlerDisplayStyle textStyle = iPluginConfig.getEnum(JadeIds.UNIVERSAL_FLUID_STORAGE_STYLE);
            String key = textStyle == IWailaConfig.HandlerDisplayStyle.PLAIN_TEXT ? "jade.fluid.text" : "jade.fluid";

            for (int i = 0; i < handler.getTanks(); i++) {
                FluidStack fluidStack = handler.getFluidInTank(i);
                JadeFluidObject fluidObject = JadeFluidObject.of(fluidStack.getFluid(), fluidStack.getAmount(), fluidStack.getComponentsPatch());
                ProgressStyle style = helper.progressStyle().overlay(helper.fluid(fluidObject));
                String current = FluidTextHelper.getUnicodeMillibuckets(fluidStack.getAmount(), true);
                String capacity = FluidTextHelper.getUnicodeMillibuckets(PotStillBlockEntity.CAPACITY, true);

                MutableComponent text;
                if (fluidStack.isEmpty()) {
                    text = Component.translatable("jade.fluid", Component.translatable("jade.fluid.empty"), Component.literal(capacity).withStyle(ChatFormatting.GRAY));
                } else {
                    MutableComponent textx;
                    if (!accessor.showDetails()) {
                        textx = IThemeHelper.get().info(current);
                    } else {
                        textx = Component.translatable("jade.fluid.with_capacity",
                                IThemeHelper.get().info(current),
                                capacity);
                    }
                    text = Component.translatable(key, IThemeHelper.get().info(IDisplayHelper.get().stripColor(CommonProxy.getFluidName(fluidObject))), textx);
                }
                tooltip.add(helper.progress((float) fluidStack.getAmount() / PotStillBlockEntity.CAPACITY, text, style, BoxStyle.getNestedBox(), true));
            }
            tooltip.remove(JadeIds.UNIVERSAL_FLUID_STORAGE);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.POT_STILL;
    }

    @Override
    public int getDefaultPriority() {
        return 10000;
    }
}
