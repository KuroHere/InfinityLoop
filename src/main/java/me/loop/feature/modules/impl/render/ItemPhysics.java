package me.loop.feature.modules.impl.render;

import me.loop.feature.modules.Module;
import me.loop.feature.modules.Category;
import me.loop.feature.modules.settings.Setting;

public class ItemPhysics extends Module {
    public static ItemPhysics INSTANCE;

    public final Setting<Float> Scaling = this.add(new Setting("Scaling", Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(10.0f)));

    public ItemPhysics() {
        super("ItemPhysics", "Apply physics to items.", Category.RENDER, true, false, false);
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
