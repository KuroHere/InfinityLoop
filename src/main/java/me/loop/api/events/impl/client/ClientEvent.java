package me.loop.api.events.impl.client;

import me.loop.api.events.Event;
import me.loop.client.Client;
import me.loop.client.modules.settings.Setting;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ClientEvent
        extends Event {
    private static final int stage = 0;
    private Client client;
    private Setting setting;

    public ClientEvent(int stage, Client client) {
        super(stage);
        this.client = client;
    }

    public ClientEvent(Setting setting) {
        super(stage);
        this.setting = setting;
    }

    public Client getFeature() {
        return this.client;
    }

    public Setting getSetting() {
        return this.setting;
    }
}

