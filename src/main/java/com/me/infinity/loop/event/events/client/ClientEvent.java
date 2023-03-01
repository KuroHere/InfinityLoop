package com.me.infinity.loop.event.events.client;

import com.me.infinity.loop.event.Event;
import com.me.infinity.loop.features.Feature;
import com.me.infinity.loop.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ClientEvent
        extends Event {
    private Feature feature;
    private Setting setting;

    public ClientEvent(Stage stage, Feature feature) {
        super(stage);
        this.feature = feature;
    }

    public ClientEvent(Setting setting) {
        this.setting = setting;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public Setting getSetting() {
        return this.setting;
    }
}

