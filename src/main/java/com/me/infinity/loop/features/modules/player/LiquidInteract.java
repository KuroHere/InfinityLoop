package com.me.infinity.loop.features.modules.player;

import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;

public class LiquidInteract
        extends Module {
    private static LiquidInteract INSTANCE = new LiquidInteract();

    public LiquidInteract() {
        super("LiquidInteract", "Interact with liquids", ModuleCategory.PLAYER);
        this.setInstance();
    }

    public static LiquidInteract getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LiquidInteract();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

