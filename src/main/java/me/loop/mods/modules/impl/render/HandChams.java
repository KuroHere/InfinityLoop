package me.loop.mods.modules.impl.render;

import me.loop.mods.modules.Module;
import me.loop.mods.modules.Category;
import me.loop.mods.settings.Setting;

public class HandChams
        extends Module {
    private static HandChams INSTANCE = new HandChams();
    public Setting<RenderMode> mode = this.add(new Setting<RenderMode>("Mode", RenderMode.SOLID));
    public Setting<Integer> red = this.add(new Setting<Integer>("Red", 255, 0, 255));
    public Setting<Integer> green = this.add(new Setting<Integer>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.add(new Setting<Integer>("Blue", 0, 0, 255));
    public Setting<Integer> alpha = this.add(new Setting<Integer>("Alpha", 240, 0, 255));

    public HandChams() {
        super("HandChams", "Changes your hand color.", Category.RENDER);
        this.setInstance();
    }

    public static HandChams getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new HandChams();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public enum RenderMode {
        SOLID,
        WIREFRAME

    }
}

