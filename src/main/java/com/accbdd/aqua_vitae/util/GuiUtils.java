package com.accbdd.aqua_vitae.util;

import com.accbdd.aqua_vitae.component.AlcoholPropertiesComponent;
import com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class GuiUtils {
    public static void drawTiledSprite(GuiGraphics guiGraphics, int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel, boolean blend) {
        if (desiredWidth != 0 && desiredHeight != 0 && textureWidth != 0 && textureHeight != 0) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, sprite.atlasLocation());
            int xTileCount = desiredWidth / textureWidth;
            int xRemainder = desiredWidth - xTileCount * textureWidth;
            int yTileCount = desiredHeight / textureHeight;
            int yRemainder = desiredHeight - yTileCount * textureHeight;
            int yStart = yPosition + yOffset;
            float uMin = sprite.getU0();
            float uMax = sprite.getU1();
            float vMin = sprite.getV0();
            float vMax = sprite.getV1();
            float uDif = uMax - uMin;
            float vDif = vMax - vMin;
            if (blend) {
                RenderSystem.enableBlend();
            }

            BufferBuilder vertexBuffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            Matrix4f matrix4f = guiGraphics.pose().last().pose();

            for (int xTile = 0; xTile <= xTileCount; ++xTile) {
                int width = xTile == xTileCount ? xRemainder : textureWidth;
                if (width == 0) {
                    break;
                }

                int x = xPosition + xTile * textureWidth;
                int maskRight = textureWidth - width;
                int shiftedX = x + textureWidth - maskRight;
                float uLocalDif = uDif * (float)maskRight / (float)textureWidth;
                float uLocalMin;
                float uLocalMax;
                uLocalMin = uMin;
                uLocalMax = uMax - uLocalDif;

                for (int yTile = 0; yTile <= yTileCount; ++yTile) {
                    int height = yTile == yTileCount ? yRemainder : textureHeight;
                    if (height == 0) {
                        break;
                    }

                    int y = yStart - (yTile + 1) * textureHeight;
                    int maskTop = textureHeight - height;
                    float vLocalDif = vDif * (float)maskTop / (float)textureHeight;
                    float vLocalMin;
                    float vLocalMax;
                    vLocalMin = vMin + vLocalDif;
                    vLocalMax = vMax;

                    vertexBuffer.addVertex(matrix4f, (float)x, (float)(y + textureHeight), (float)zLevel).setUv(uLocalMin, vLocalMax);
                    vertexBuffer.addVertex(matrix4f, (float)shiftedX, (float)(y + textureHeight), (float)zLevel).setUv(uLocalMax, vLocalMax);
                    vertexBuffer.addVertex(matrix4f, (float)shiftedX, (float)(y + maskTop), (float)zLevel).setUv(uLocalMax, vLocalMin);
                    vertexBuffer.addVertex(matrix4f, (float)x, (float)(y + maskTop), (float)zLevel).setUv(uLocalMin, vLocalMin);
                }
            }

            BufferUploader.drawWithShader(vertexBuffer.buildOrThrow());
            if (blend) {
                RenderSystem.disableBlend();
            }
        }
    }

    public static void drawTiledSprite(GuiGraphics guiGraphics, int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel) {
        drawTiledSprite(guiGraphics, xPosition, yPosition, yOffset, desiredWidth, desiredHeight, sprite, textureWidth, textureHeight, zLevel, true);
    }

    public static int getScaled(int value, int max, int scaleTo) {
        if (max == 0) return 0;
        return (int) Math.ceil((double) (value * scaleTo) / max);
    }

    public static TextureAtlasSprite getFluidSprite(FluidStack fluidStack) {
        IClientFluidTypeExtensions handler = IClientFluidTypeExtensions.of(fluidStack.getFluidType());
        ResourceLocation fluidStill = handler.getStillTexture(fluidStack);
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
    }

    public static int getFluidColor(FluidStack fluidStack) {
        if (fluidStack.has(ModComponents.ALCOHOL_PROPERTIES))
            return fluidStack.getOrDefault(ModComponents.ALCOHOL_PROPERTIES, AlcoholPropertiesComponent.EMPTY).color().color();
        if (fluidStack.has(ModComponents.PRECURSOR_PROPERTIES))
            return fluidStack.getOrDefault(ModComponents.PRECURSOR_PROPERTIES, PrecursorPropertiesComponent.EMPTY).properties().color().color();
        return IClientFluidTypeExtensions.of(fluidStack.getFluidType()).getTintColor();
    }
}
