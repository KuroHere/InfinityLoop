package com.me.infinity.loop.event.events;

import com.me.infinity.loop.event.EventStage;

public class RenderEvent extends EventStage {

    private final float partialTicks;

    public RenderEvent(float partialTicks) {
        super();
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
