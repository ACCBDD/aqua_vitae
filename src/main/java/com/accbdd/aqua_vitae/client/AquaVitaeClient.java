package com.accbdd.aqua_vitae.client;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.client.renderer.CrushingTubRenderer;
import com.accbdd.aqua_vitae.component.FluidStackComponent;
import com.accbdd.aqua_vitae.registry.*;
import com.accbdd.aqua_vitae.util.FluidUtils;
import com.accbdd.aqua_vitae.util.NumUtils;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;

@Mod(value = AquaVitae.MODID, dist = {Dist.CLIENT})
public class AquaVitaeClient {
    static float PREV_X_SWAY = 0;
    static float PREV_Y_SWAY = 0;
    static float X_SWAY_SPEED = 0;
    static float Y_SWAY_SPEED = 0;
    static float X_SWAY_PHASE = 0;
    static float Y_SWAY_PHASE = 0;
    static float TIME_SINCE_NEW_SWAY = 0;
    static float SWAY_FACTOR = 0;
    public AquaVitaeClient(IEventBus modEventBus, ModContainer container) {
        modEventBus.register(this);
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        ModFluids.REGISTERED.forEach(fluid -> ItemBlockRenderTypes.setRenderLayer(fluid.get(), RenderType.translucent()));
    }

    @SubscribeEvent
    public void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        ModFluidTypes.REGISTERED.forEach((name, set) -> event.registerFluidType(new IClientFluidTypeExtensions() {
            private static final ResourceLocation UNDERWATER_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/underwater.png");
            private static final ResourceLocation WATER_STILL = ResourceLocation.withDefaultNamespace("block/water_still");
            private static final ResourceLocation WATER_FLOW = ResourceLocation.withDefaultNamespace("block/water_flow");
            private static final ResourceLocation WATER_OVERLAY = ResourceLocation.withDefaultNamespace("block/water_overlay");

            @Override
            public int getTintColor() {
                return set.color();
            }

            @Override
            public int getTintColor(FluidStack stack) {
                return FluidUtils.getColorOrInvisible(stack);
            }

            @Override
            public ResourceLocation getStillTexture() {
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return UNDERWATER_LOCATION;
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                int color = getTintColor();
                return new Vector3f(color >> 16 & 255, color >> 8 & 255, color & 255);
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                int color = getTintColor();
                RenderSystem.setShaderFogStart(1);
                RenderSystem.setShaderFogColor((color >> 16 & 255) / 255f, (color >> 8 & 255) / 255f, (color & 255) / 255f, 1);
                RenderSystem.setShaderFogEnd(10);
            }
        }, set.type()));
    }

    @SubscribeEvent
    public void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        ModFluids.REGISTERED.forEach(fluid -> event.register((stack, index) -> {
            if (index == 1) {
                return ModFluidTypes.REGISTERED.get(fluid.get().getFluidType().getDescriptionId()).color();
            }
            return event.getItemColors().getColor(new ItemStack(Items.BUCKET), 0);
        }, fluid.get().getBucket()));

        event.register((stack, index) -> {
            if (index == 1) {
                FluidStack fluidStack = stack.getOrDefault(ModComponents.FLUIDSTACK, FluidStackComponent.EMPTY).stack();
                return FluidUtils.getColorOrInvisible(fluidStack);
            }
            return -1;
        }, ModItems.EYEBALL);
    }

    @SubscribeEvent
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CRUSHING_TUB.get(), CrushingTubRenderer::new);
    }

    @SubscribeEvent
    public void swayCamera(ViewportEvent.ComputeCameraAngles event) {
        //todo implement
        Player player = Minecraft.getInstance().player;

        if (Minecraft.getInstance().player == null) {
            SWAY_FACTOR = 0;
            PREV_X_SWAY = 0;
            PREV_Y_SWAY = 0;
            return;
        }

        float frameTime = Minecraft.getInstance().getFrameTimeNs();
        double effect = Minecraft.getInstance().player.getEffect(ModEffects.TIPSY).getAmplifier();

        if (!Minecraft.getInstance().isPaused()) {
            float targetFactor = (float) NumUtils.blend(0, 20, effect, 0, 1);
            targetFactor *= Minecraft.getInstance().options.screenEffectScale().get().floatValue();

            float interpolationSpeed = 0.05f * frameTime;
            SWAY_FACTOR = SWAY_FACTOR + (targetFactor - SWAY_FACTOR) * interpolationSpeed;

            // Set random sway speed every once in a while
            if (TIME_SINCE_NEW_SWAY > 100 || X_SWAY_SPEED == 0 || Y_SWAY_SPEED == 0)
            {
                TIME_SINCE_NEW_SWAY = 0;
                X_SWAY_SPEED = (float) ((Math.random() * 0.002f + 0.0025f));
                Y_SWAY_SPEED = (float) ((Math.random() * 0.002f + 0.0025f));
            }
            TIME_SINCE_NEW_SWAY += frameTime;

            // Blend to the new sway speed
            X_SWAY_PHASE += 2 * Math.PI * frameTime * X_SWAY_SPEED;
            Y_SWAY_PHASE += 2 * Math.PI * frameTime * Y_SWAY_SPEED;

            // Apply the sway speed to a sin function using the smoothed factor
            float xOffs = (float) (Math.sin(X_SWAY_PHASE) * SWAY_FACTOR);
            float yOffs = (float) ((Math.sin(Y_SWAY_PHASE) + Math.cos(Y_SWAY_PHASE / 4) * 2) * SWAY_FACTOR * 3);

            // Apply the sway
            player.setXRot(player.getXRot() + xOffs - PREV_X_SWAY);
            player.setYRot(player.getYRot() + yOffs - PREV_Y_SWAY);

            // Save the previous sway
            PREV_X_SWAY = xOffs;
            PREV_Y_SWAY = yOffs;
        }
    }
}
