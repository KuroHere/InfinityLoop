package com.me.infinity.loop.event.events.render;

import com.me.infinity.loop.event.Event;

public class Render3DEvent
        extends Event {
    private final float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

