package me.loop.api.managers;

import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.mods.gui.click.Component;
import me.loop.mods.modules.impl.client.Colors;

import java.awt.*;

public class ColorManager {
    private float red = 1.0f;
    private float green = 1.0f;
    private float blue = 1.0f;
    private float alpha = 1.0f;
    private Color current = new Color(-1);

    private Color color;
    public ColorManager() {
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.alpha = 1.0f;
        this.color = new Color(this.red, this.green, this.blue, this.alpha);
    }

    public boolean isRainbow() {
        return Colors.getInstance().rainbow.getValue();
    }

    //Getters

    public Color getCurrent() {
        if (isRainbow()) {
            return getRainbow();
        }

        return current;
    }

    public Color getRainbow() {
        return ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue());
    }


    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getColorAsInt() {
        return ColorUtil.toRGBA(this.color);
    }

    public int getColorAsIntFullAlpha() {
        return ColorUtil.toRGBA(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 255));
    }

    public int getColorWithAlpha(int alpha) {
        if (Colors.getInstance().rainbow.getValue().booleanValue()) {
            return ColorUtil.rainbow(Component.counter1[0] * Colors.getInstance().rainbowHue.getValue()).getRGB();
        }
        return ColorUtil.toRGBA(new Color(this.red, this.green, this.blue, (float) alpha / 255.0f));
    }

    public void setColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.updateColor();
    }

    public void updateColor() {
        this.setColor(new Color(this.red, this.green, this.blue, this.alpha));
    }

    public void setColor(int red, int green, int blue, int alpha) {
        this.red = (float) red / 255.0f;
        this.green = (float) green / 255.0f;
        this.blue = (float) blue / 255.0f;
        this.alpha = (float) alpha / 255.0f;
        this.updateColor();
    }

    public void setRed(float red) {
        this.red = red;
        this.updateColor();
    }

    public void setGreen(float green) {
        this.green = green;
        this.updateColor();
    }

    public void setBlue(float blue) {
        this.blue = blue;
        this.updateColor();
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        this.updateColor();
    }

}

