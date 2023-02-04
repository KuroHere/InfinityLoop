package com.me.infinity.loop.util.renders;

import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

/**
 * @author lukflug
 */

public class TestColor extends Color {

    private static final long serialVersionUID = 1L;

    public TestColor(int rgb) {
        super(rgb);
    }

    public TestColor(int rgba, boolean hasalpha) {
        super(rgba, hasalpha);
    }

    public TestColor(int r, int g, int b) {
        super(r, g, b);
    }

    public TestColor(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public TestColor(Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public TestColor(TestColor color, int a) {
        super(color.getRed(), color.getGreen(), color.getBlue(), a);
    }

    public static TestColor fromHSB(float hue, float saturation, float brightness) {
        return new TestColor(Color.getHSBColor(hue, saturation, brightness));
    }

    public float getHue() {
        return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[0];
    }

    public float getSaturation() {
        return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[1];
    }

    public float getBrightness() {
        return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[2];
    }

    public void glColor() {
        GlStateManager.color(getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f, getAlpha() / 255.0f);
    }
}