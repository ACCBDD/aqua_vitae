package com.accbdd.aqua_vitae.screen;

import com.accbdd.aqua_vitae.block.entity.MaltKilnBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class MaltKilnScreen extends AbstractContainerScreen<MaltKilnMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/malt_kiln.png");

    public MaltKilnScreen(MaltKilnMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
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
        guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(menu.getProgress()), leftPos, topPos - 20, 0xFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(menu.getBurnTime()), leftPos, topPos - 10, 0xFFFFFF);
    }

    public void renderBurnBar(GuiGraphics graphics, int x, int y) {
        int burnScaled = getScaled(menu.getBurnTime(), menu.getMaxBurnTime(), 14);
        graphics.blit(BACKGROUND,
                x + 56,
                y + 37 + (14 - burnScaled),
                176,
                14 - burnScaled,
                14,
                burnScaled);
    }

    private void renderProgress(GuiGraphics graphics, int x, int y) {
        graphics.blit(BACKGROUND, x + 79, y + 35, 190, 0, getScaled(menu.getProgress(), MaltKilnBlockEntity.MAX_PROGRESS, 24), 16);
    }

    public int getScaled(int value, int max, int scaleTo) {
        if (max == 0) return 0;
        return (int) Math.ceil((double) (value * scaleTo) / max);
    }
}
