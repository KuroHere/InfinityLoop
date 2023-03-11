package me.loop.feature.modules.impl.client;

import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.feature.modules.Module;
import me.loop.feature.modules.Category;
import me.loop.feature.modules.impl.client.ClickGui.ClickGui;
import me.loop.feature.modules.settings.Setting;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Colors extends Module {
    private static Colors INSTANCE = new Colors();
    public Setting<Color> c = add(new Setting("Color", new Color(-1)));
    public Setting<Boolean> rainbow = this.add(new Setting<>("Rainbow", Boolean.valueOf(false), "Rainbow colors."));
    public Setting<rainbowMode> rainbowModeHud = this.add(new Setting<Object>("HRainbowMode", rainbowMode.Static, v -> this.rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = this.add(new Setting<Object>("ARainbowMode", rainbowModeArray.Static, v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowHue = this.add(new Setting<Object>("Delay", Integer.valueOf(160), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowSaturation = this.add(new Setting<Object>("Saturation", Integer.valueOf(120), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowBrightness = this.add(new Setting<Object>("Brightness", Integer.valueOf(170), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));

    public float hue;
    public Map<Integer, Integer> colorHeightMap = new HashMap<Integer, Integer>();

    public Colors() {
        super("Colors", "Universal colors.", Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static Colors getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Colors();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        int delay = 101;
        double colorSpeed = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        float tempHue = this.hue = (float)(System.currentTimeMillis() % (long)(360 * colorSpeed)) / (360.0f * (float)colorSpeed);
        for (int i = 0; i <= 510; ++i) {
            this.colorHeightMap.put(i, Color.HSBtoRGB(tempHue, this.rainbowSaturation.getValue().floatValue() / 255.0f, this.rainbowBrightness.getValue().floatValue() / 255.0f));
            tempHue += 0.0013071896f;
        }
        if (ClickGui.getInstance().colorSync.getValue()) {
            Managers.colorManager.setColor(INSTANCE.getCurrentColor().getRed(), INSTANCE.getCurrentColor().getGreen(), INSTANCE.getCurrentColor().getBlue(), ClickGui.getInstance().hoverAlpha.getValue());
        }
    }

    public Color getCurrentColor() {
        if (this.rainbow.getValue().booleanValue()) {
            return ColorUtil.rainbow(this.rainbowHue.getValue());
        }
        return new Color(this.c.getValue().getRed(), this.c.getValue().getGreen(), this.c.getValue().getBlue(), this.c.getValue().getAlpha());
    }

    public enum rainbowModeArray {
        Static,
        Up

    }

    public enum rainbowMode {
        Static,
        Sideway

    }
}


