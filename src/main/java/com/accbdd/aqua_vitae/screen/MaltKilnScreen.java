package com.accbdd.aqua_vitae.screen;

import com.accbdd.aqua_vitae.block.entity.MaltKilnBlockEntity;
import com.accbdd.aqua_vitae.screen.widget.FluidGaugeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;
import static com.accbdd.aqua_vitae.util.GuiUtils.getScaled;

public class MaltKilnScreen extends AbstractContainerScreen<MaltKilnMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/malt_kiln.png");

    public MaltKilnScreen(MaltKilnMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new FluidGaugeWidget(this.leftPos + 39, this.topPos + 17, 8, 52, menu::getFluid, MaltKilnBlockEntity.MAX_FLUID));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderBurnBar(guiGraphics, this.leftPos, this.topPos);
        renderProgress(guiGraphics, this.leftPos, this.topPos);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public void renderBurnBar(GuiGraphics graphics, int x, int y) {
        int burnScaled = getScaled(menu.getBurnTime(), menu.getMaxBurnTime(), 14);
        graphics.blit(BACKGROUND,
                x + 56,
                y + 35 + (14 - burnScaled),
                176,
                13 - burnScaled,
                14,
                burnScaled);
    }

    private void renderProgress(GuiGraphics graphics, int x, int y) {
        graphics.blit(BACKGROUND, x + 79, y + 35, 190, 0, getScaled(menu.getProgress(), MaltKilnBlockEntity.MAX_PROGRESS, 24), 16);
    }
}
