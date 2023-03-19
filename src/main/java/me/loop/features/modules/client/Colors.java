package me.loop.features.modules.client;

import me.loop.InfinityLoop;
import me.loop.features.modules.Module;
import me.loop.features.modules.client.clickgui.ClickGui;
import me.loop.features.setting.Setting;
import me.loop.util.impl.ColorUtil;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Colors extends Module {
    private static Colors INSTANCE = new Colors();
    public Setting<Color> c = this.register(new Setting("Color", new Color(-1)));
    public Setting<Boolean> rainbow = this.register(new Setting<>("Rainbow", Boolean.valueOf(false), "Rainbow colors."));
    public Setting<rainbowMode> rainbowModeHud = this.register(new Setting<Object>("HRainbowMode", rainbowMode.Static, v -> this.rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = this.register(new Setting<Object>("ARainbowMode", rainbowModeArray.Static, v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowHue = this.register(new Setting<Object>("Delay", Integer.valueOf(160), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowSaturation = this.register(new Setting<Object>("Saturation", Integer.valueOf(120), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowBrightness = this.register(new Setting<Object>("Brightness", Integer.valueOf(170), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));

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
            InfinityLoop.colorManager.setColor(INSTANCE.getCurrentColor().getRed(), INSTANCE.getCurrentColor().getGreen(), INSTANCE.getCurrentColor().getBlue(), ClickGui.getInstance().hoverAlpha.getValue());
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


