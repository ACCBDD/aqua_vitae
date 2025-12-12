package com.accbdd.aqua_vitae.screen.widget;

import com.accbdd.aqua_vitae.client.ClientUtils;
import com.accbdd.aqua_vitae.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


public class FluidGaugeWidget extends AbstractDisplayWidget {
    private final Supplier<FluidStack> fluidSupplier;
    private final int maxFluid;

    public FluidGaugeWidget(int x, int y, int width, int height, Supplier<FluidStack> fluidSupplier, int maxFluid) {
        super(x, y, width, height, Component.empty());
        this.fluidSupplier = fluidSupplier;
        this.maxFluid = maxFluid;
    }

    @Override
    public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        FluidStack fluidStack = fluidSupplier.get();
        int fluidHeight = GuiUtils.getScaled(fluidStack.getAmount(), maxFluid, height);
        if (fluidHeight == 0)
            return;
        TextureAtlasSprite fluidSprite = GuiUtils.getFluidSprite(fluidStack);
        int fluidColor = GuiUtils.getFluidColor(fluidStack);
        float r = (float)(fluidColor >> 16 & 255) / 255.0F;
        float g = (float)(fluidColor >> 8 & 255) / 255.0F;
        float b = (float)(fluidColor & 255) / 255.0F;
        float a = (float)(fluidColor >> 24 & 255) / 255.0F;
        guiGraphics.setColor(r, g, b, a);
        GuiUtils.drawTiledSprite(guiGraphics, getX(), getY(), getHeight(), getWidth(), fluidHeight, fluidSprite, 16, 16, 0);
        guiGraphics.setColor(1, 1, 1, 1);
    }

    @Override
    public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        FluidStack fluidStack = fluidSupplier.get();
        List<Component> tooltips = new ArrayList<>();
        if (!fluidStack.isEmpty()) {
            tooltips.add(fluidStack.getHoverName().copy().append(": ").append(Component.literal(fluidSupplier.get().getAmount() + " mB")));
            tooltips.addAll(ClientUtils.getFluidTooltip(fluidStack));
        } else {
            tooltips.add(Component.literal("Empty"));
        }
        guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltips, Optional.empty(), mouseX, mouseY);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
