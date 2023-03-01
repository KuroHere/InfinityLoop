package com.me.infinity.loop.features.modules.render;

import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;
import com.me.infinity.loop.features.setting.Setting;

public class ItemPhysics extends Module {
    public static ItemPhysics INSTANCE;

    public final Setting<Float> Scaling = this.register(new Setting("Scaling", Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(10.0f)));

    public ItemPhysics() {
        super("ItemPhysics", "Apply physics to items.", ModuleCategory.RENDER);
        this.setInstance();
    }

    public static ItemPhysics getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemPhysics();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}
