package com.accbdd.aqua_vitae.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BooleanSupplier;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class HeatWidget extends AbstractDisplayWidget {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/sprite/heat.png");
    private final BooleanSupplier heated;

    public HeatWidget(int x, int y, BooleanSupplier heated) {
        super(x, y, 17, 15, Component.empty());
        this.heated = heated;
    }

    @Override
    void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(TEXTURE,
                getX(),
                getY(),
                heated.getAsBoolean() ? 17 : 0,
                0,
                17,
                15,
                34,
                15);
    }

    @Override
    void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.renderTooltip(Minecraft.getInstance().font,
                Component.translatable(heated.getAsBoolean() ? "tooltip.aqua_vitae.heated" : "tooltip.aqua_vitae.needs_heat"),
                mouseX,
                mouseY);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
