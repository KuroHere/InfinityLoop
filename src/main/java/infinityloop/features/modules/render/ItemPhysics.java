package infinityloop.features.modules.render;

import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.setting.Setting;

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
