package me.loop.mods.modules.impl.player;

import me.loop.mods.modules.Module;
import me.loop.mods.modules.Category;
import me.loop.mods.settings.Setting;

public class TpsSync
        extends Module {
    private static TpsSync INSTANCE = new TpsSync();
    public Setting<Boolean> attack = this.add(new Setting<Boolean>("Attack", Boolean.FALSE));
    public Setting<Boolean> mining = this.add(new Setting<Boolean>("Mine", Boolean.TRUE));

    public TpsSync() {
        super("TpsSync", "Syncs your client with the TPS.", Category.PLAYER, true, false);
        this.setInstance();
    }

    public static TpsSync getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TpsSync();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

