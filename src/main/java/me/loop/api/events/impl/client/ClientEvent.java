package me.loop.api.events.impl.client;

import me.loop.api.events.Event;
import me.loop.mods.Mod;
import me.loop.mods.settings.Setting;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ClientEvent
        extends Event {
    private Mod mod;
    private Setting setting;

    public ClientEvent(int stage, Mod mod) {
        super(stage);
        this.mod = mod;
    }

    public ClientEvent(Setting setting) {
        super(2);
        this.setting = setting;
    }

    public Mod getFeature() {
        return this.mod;
    }

    public Setting getSetting() {
        return this.setting;
    }
}