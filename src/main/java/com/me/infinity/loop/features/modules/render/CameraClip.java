package com.me.infinity.loop.features.modules.render;

import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;
import com.me.infinity.loop.features.setting.Setting;

public class CameraClip
        extends Module {
    private static CameraClip INSTANCE = new CameraClip();
    public Setting<Boolean> extend = this.register(new Setting<>("Extend", false));
    public Setting<Double> distance = this.register(new Setting<>("Distance", 10.0, 0.0, 50.0, v -> this.extend.getValue(), "By how much you want to extend the distance."));

    public CameraClip() {
        super("CameraClip", "Makes your Camera clip.", ModuleCategory.RENDER);
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
