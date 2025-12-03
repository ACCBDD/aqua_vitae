package com.accbdd.aqua_vitae.util;

import java.awt.*;

public class NumUtils {
    public static int lightenColor(int argb, float factor) { //clamped alpha to 22
        factor = Math.max(0f, Math.min(1f, factor));

        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        r = (int) (r + (255 - r) * factor);
        g = (int) (g + (255 - g) * factor);
        b = (int) (b + (255 - b) * factor);

        return (Math.max(a, 0x22) << 24) | (r << 16) | (g << 8) | b;
    }

    public static int darkenColor(int argb, float factor) { //clamped alpha to DD
        factor = Math.max(0f, Math.min(1f, factor));

        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        a = (int) Math.min(a + factor * 100, 0xDD);
        r = (int) (r * (1f - factor));
        g = (int) (g * (1f - factor));
        b = (int) (b * (1f - factor));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int saturateColor(int argb, float factor) {

        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        float[] hsbValues = Color.RGBtoHSB(r, g, b, null);

        float hue = hsbValues[0];
        float saturation = hsbValues[1];
        float brightness = hsbValues[2];

        float newSaturation = saturation + factor;
        newSaturation = Math.clamp(newSaturation, 0.0f, 1.0f);

        int newColor = Color.HSBtoRGB(hue, newSaturation, brightness);
        r = (newColor >> 16) & 0xFF;
        g = (newColor >> 8) & 0xFF;
        b = newColor & 0xFF;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
