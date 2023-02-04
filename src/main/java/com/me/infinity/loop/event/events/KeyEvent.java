package com.me.infinity.loop.event.events;

import com.me.infinity.loop.event.EventStage;

public class KeyEvent
        extends EventStage {
    private final int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}

