package com.accbdd.aqua_vitae.screen;

import com.accbdd.aqua_vitae.block.entity.MashTunBlockEntity;
import com.accbdd.aqua_vitae.screen.widget.FluidGaugeWidget;
import com.accbdd.aqua_vitae.screen.widget.HeatWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;
import static com.accbdd.aqua_vitae.util.GuiUtils.getScaled;

public class MashTunScreen extends AbstractContainerScreen<MashTunMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/mash_tun.png");

    public MashTunScreen(MashTunMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 176;
        this.inventoryLabelY = this.imageHeight - 95;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new FluidGaugeWidget(this.leftPos + 11, this.topPos + 18, 7, 52, menu::getInputFluid, MashTunBlockEntity.MAX_FLUID));
        addRenderableWidget(new FluidGaugeWidget(this.leftPos + 114, this.topPos + 18, 51, 52, menu::getOutputFluid, MashTunBlockEntity.MAX_FLUID));
        addRenderableWidget(new HeatWidget(this.leftPos + 86, this.topPos + 65, () -> menu.getProgress() != 0)); //todo actually implement
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderProgress(guiGraphics, this.leftPos, this.topPos);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderProgress(GuiGraphics graphics, int x, int y) {
        graphics.blit(BACKGROUND, x + 83, y + 17, 176, 0, getScaled(menu.getProgress(), menu.getMaxProgress(), 25), 20);
    }
}
