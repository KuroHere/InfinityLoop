package me.loop.feature.modules.impl.render;

import me.loop.feature.modules.Module;
import me.loop.feature.modules.Category;
import me.loop.feature.modules.settings.Setting;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.common.*;
import java.awt.*;

public class FogColor extends Module
{
    private static FogColor INSTANCE = new FogColor();
    private final Setting<Integer> red = this.add(new Setting<>("Red", 0, 0, 255));
    private final Setting<Integer> green = this.add(new Setting<>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.add(new Setting<>("Blue", 0, 0, 255));
    private final Setting<Boolean> rainbow = this.add(new Setting<>("Rainbow", false));
    public Setting<Integer> delay = this.add(new Setting<Object>("Delay", Integer.valueOf(160), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue()));
    private final Setting<Boolean> fog = this.add(new Setting<>("Fog", true));
    public FogColor() {
        super("FogColor", "Changes the color of the sky", Category.RENDER, true, false, false);
    }

    private void setInstance() {
        FogColor.INSTANCE = this;
    }

    public static FogColor getInstance() {
        if (FogColor.INSTANCE == null) {
            FogColor.INSTANCE = new FogColor();
        }
        return FogColor.INSTANCE;
    }

    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        event.setRed(this.red.getValue() / 255.0f);
        event.setGreen(this.green.getValue() / 255.0f);
        event.setBlue(this.blue.getValue() / 255.0f);
    }

    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        if (this.fog.getValue()) {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void onUpdate() {
        if (this.rainbow.getValue()) {
            this.doRainbow();
        }
    }

    public void doRainbow() {
        final float[] tick_color = {delay.getValue() + System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.red.setValue(color_rgb_o >> 16 & 0xFF);
        this.green.setValue(color_rgb_o >> 8 & 0xFF);
        this.blue.setValue(color_rgb_o & 0xFF);
    }

    static {
        FogColor.INSTANCE = new FogColor();
    }
}
