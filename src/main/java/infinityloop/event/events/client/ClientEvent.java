package infinityloop.event.events.client;

import infinityloop.event.Event;
import infinityloop.features.Feature;
import infinityloop.features.setting.Setting;
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

