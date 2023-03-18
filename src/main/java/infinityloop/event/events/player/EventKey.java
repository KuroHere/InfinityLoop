package infinityloop.event.events.player;

import infinityloop.event.Event;

public class EventKey
        extends Event {
    public boolean info;
    public boolean pressed;

    public EventKey(boolean info, boolean pressed) {
        this.info = info;
        this.pressed = pressed;
    }
}
