package com.accbdd.aqua_vitae.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public abstract class AbstractDisplayWidget extends AbstractWidget {
    public AbstractDisplayWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public void playDownSound(SoundManager handler) {

    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderContent(guiGraphics, mouseX, mouseY, partialTick);
        if (this.isHovered())
            renderTooltip(guiGraphics, mouseX, mouseY, partialTick);
    }

    abstract void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

    abstract void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);
}
