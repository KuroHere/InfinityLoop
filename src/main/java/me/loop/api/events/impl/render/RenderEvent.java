package me.loop.api.events.impl.render;

import me.loop.api.events.Event;

public class RenderEvent extends Event {

    private final float partialTicks;

    public RenderEvent(int stage,float partialTicks) {
        super(stage);
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
