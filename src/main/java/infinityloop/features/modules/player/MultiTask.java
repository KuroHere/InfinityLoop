package infinityloop.features.modules.player;

import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;

public class MultiTask
        extends Module {
    private static MultiTask INSTANCE = new MultiTask();

    public MultiTask() {
        super("MultiTask", "Allows you to eat while mining.", ModuleCategory.PLAYER);
        this.setInstance();
    }

    public static MultiTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MultiTask();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

