package me.loop.api.events.impl.render;

import me.loop.api.events.Event;

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

