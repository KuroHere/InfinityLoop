package me.loop.api.events.impl.client;

import me.loop.api.events.Event;
import me.loop.client.Client;
import me.loop.client.modules.settings.Setting;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ClientEvent
        extends Event {
    private Client client;
    private Setting setting;

    public ClientEvent(int stage, Client client) {
        super(stage);
        this.client = client;
    }

    public ClientEvent(Setting setting) {
        super(2);
        this.setting = setting;
    }

    public Client getFeature() {
        return this.client;
    }

    public Setting getSetting() {
        return this.setting;
    }
}