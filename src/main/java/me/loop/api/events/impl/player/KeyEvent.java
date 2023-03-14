package me.loop.api.events.impl.player;

import me.loop.api.events.Event;

public class KeyEvent
        extends Event {
    public boolean info;
    public boolean pressed;

    public KeyEvent(int stage, boolean info, boolean pressed) {
        super(stage);
        this.info = info;
        this.pressed = pressed;
    }
}
