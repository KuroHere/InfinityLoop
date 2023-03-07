package me.loop.client.modules.impl.render;

import me.loop.client.modules.Module;
import me.loop.client.modules.Category;
import me.loop.client.modules.settings.Setting;

public class CameraClip
        extends Module {
    private static CameraClip INSTANCE = new CameraClip();
    public Setting<Boolean> extend = this.add(new Setting<>("Extend", false));
    public Setting<Double> distance = this.add(new Setting<>("Distance", 10.0, 0.0, 50.0, v -> this.extend.getValue(), "By how much you want to extend the distance."));

    public CameraClip() {
        super("CameraClip", "Makes your Camera clip.", Category.RENDER, true, false, true);
        this.setInstance();
    }

    public static CameraClip getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CameraClip();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}
