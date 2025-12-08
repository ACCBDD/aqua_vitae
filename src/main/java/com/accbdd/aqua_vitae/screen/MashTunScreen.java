package com.accbdd.aqua_vitae.screen;

import com.accbdd.aqua_vitae.block.entity.MashTunBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class MashTunScreen extends AbstractContainerScreen<MashTunMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/mash_tun.png");

    public MashTunScreen(MashTunMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderProgress(guiGraphics, this.leftPos, this.topPos);
        renderFluids(guiGraphics, this.leftPos, this.topPos);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        if (mouseX >= leftPos + 39 && mouseX <= leftPos + 47 && mouseY >= topPos + 17 && mouseY <= topPos + 69) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("block.minecraft.water").append(": ").append(Component.literal(menu.getInputFluid().getAmount() + " mB")), mouseX, mouseY);
        }
    }

    private void renderProgress(GuiGraphics graphics, int x, int y) {
        graphics.blit(BACKGROUND, x + 83, y + 21, 176, 0, getScaled(menu.getProgress(), menu.getMaxProgress(), 25), 20);
    }

    private void renderFluids(GuiGraphics graphics, int x, int y) {
        FluidStack fluidStack = menu.getInputFluid();
        int fluidHeight = getScaled(menu.getInputFluid().getAmount(), MashTunBlockEntity.MAX_FLUID, 52);
        if (fluidHeight == 0)
            return;
        IClientFluidTypeExtensions handler = IClientFluidTypeExtensions.of(fluidStack.getFluidType());
        ResourceLocation fluidStill = handler.getStillTexture(fluidStack);
        TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
        int fluidColor = handler.getTintColor(fluidStack);
        float r = (float)(fluidColor >> 16 & 255) / 255.0F;
        float g = (float)(fluidColor >> 8 & 255) / 255.0F;
        float b = (float)(fluidColor & 255) / 255.0F;
        float a = (float)(fluidColor >> 24 & 255) / 255.0F;
        int tilesToDraw = (int) Math.ceil((double) fluidHeight / 16.0);
        int topTileStart = y + 17 + (52 - fluidHeight);
        for (int i = 0; i < tilesToDraw; i++) {
            int drawHeight = Math.min(fluidHeight - i * 16, 16);
            int currentTileStart = topTileStart + (fluidHeight - (i * 16) - drawHeight);
            graphics.blit(x + 39, currentTileStart, 0, 8, drawHeight, fluidStillSprite, r, g, b, a);
        }
    }

    public int getScaled(int value, int max, int scaleTo) {
        if (max == 0) return 0;
        return (int) Math.ceil((double) (value * scaleTo) / max);
    }
}
