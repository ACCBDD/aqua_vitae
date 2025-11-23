package com.accbdd.aqua_vitae.util;

public class NumUtils {
    public static int lightenColor(int argb, float factor) { //clamped alpha to 40
        factor = Math.max(0f, Math.min(1f, factor));

        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        r = (int) (r + (255 - r) * factor);
        g = (int) (g + (255 - g) * factor);
        b = (int) (b + (255 - b) * factor);

        return (Math.max(a, 40) << 24) | (r << 16) | (g << 8) | b;
    }

    public static int darkenColor(int argb, float factor) {
        factor = Math.max(0f, Math.min(1f, factor));

        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        r = (int) (r * (1f - factor));
        g = (int) (g * (1f - factor));
        b = (int) (b * (1f - factor));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static double blend(double blendFrom, double blendTo, double factor, double rangeMin, double rangeMax) {
        if (rangeMin > rangeMax) return blend(blendTo, blendFrom, factor, rangeMax, rangeMin);

        if (factor <= rangeMin) return blendFrom;
        if (factor >= rangeMax) return blendTo;
        return (blendTo - blendFrom) / (rangeMax - rangeMin) * (factor - rangeMin) + blendFrom;
    }
}
