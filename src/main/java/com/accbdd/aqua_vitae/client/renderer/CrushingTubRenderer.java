package com.accbdd.aqua_vitae.client.renderer;

import com.accbdd.aqua_vitae.block.entity.CrushingTubBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Random;

public class CrushingTubRenderer implements BlockEntityRenderer<CrushingTubBlockEntity> {
    private final Random random = new Random();

    public CrushingTubRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(CrushingTubBlockEntity tub, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        FluidStack fluid = tub.getFluid();

        IItemHandler inventory = tub.getItemHandler();
        int posLong = (int) tub.getBlockPos().asLong();
        ItemStack stack = inventory.getStackInSlot(0);
        this.random.setSeed(stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + (long) tub.getBlockPos().hashCode() * tub.crush);
        if (!stack.isEmpty()) {
            for (int i = 0; i < inventory.getSlots(); ++i) {
                stack = inventory.getStackInSlot(i);
                poseStack.pushPose();
                float xOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.14F;
                float zOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.14F;
                poseStack.translate(0.5 + (double) xOffset, 0.1 + 0.03 * (double) (i + 1), 0.5 + (double) zOffset);
                poseStack.mulPose(Axis.YP.rotationDegrees(this.random.nextFloat() * 360F));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.scale(0.5F, 0.5F, 0.5F);
                if (tub.getLevel() != null) {
                    Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, tub.getLevel(), posLong);
                }

                poseStack.popPose();
            }
        }

        renderFluidTop(poseStack, multiBufferSource, packedLight, packedOverlay, fluid, ((float) tub.getFluid().getAmount() / 1000) * (6f / 16f));
    }

    private void renderFluidTop(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, FluidStack fluid, float height) {
        if (fluid.isEmpty())
            return;
        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluid.getFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(props.getStillTexture());
        int color = props.getTintColor(fluid);

        poseStack.pushPose();
        poseStack.translate(0, height + 1f / 16f, 0);

        VertexConsumer vc = buffer.getBuffer(Sheets.translucentCullBlockSheet());

        float min = 2f / 16f;
        float max = 14f / 16f;

        vc.addVertex(poseStack.last().pose(), min, 0, min).setColor(color).setUv(sprite.getU0(), sprite.getV0()).setLight(packedLight).setOverlay(packedOverlay).setNormal(0, 1, 0);
        vc.addVertex(poseStack.last().pose(), min, 0, max).setColor(color).setUv(sprite.getU0(), sprite.getV1()).setLight(packedLight).setOverlay(packedOverlay).setNormal(0, 1, 0);
        vc.addVertex(poseStack.last().pose(), max, 0, max).setColor(color).setUv(sprite.getU1(), sprite.getV1()).setLight(packedLight).setOverlay(packedOverlay).setNormal(0, 1, 0);
        vc.addVertex(poseStack.last().pose(), max, 0, min).setColor(color).setUv(sprite.getU1(), sprite.getV0()).setLight(packedLight).setOverlay(packedOverlay).setNormal(0, 1, 0);

        poseStack.popPose();
    }
}
