package com.me.infinity.loop.event.events;

import com.me.infinity.loop.event.EventStage;

public class PerspectiveEvent extends EventStage
{
    private float aspect;

    public PerspectiveEvent(final float aspect) {
        this.aspect = aspect;
    }

    public float getAspect() {
        return this.aspect;
    }

    public void setAspect(final float aspect) {
        this.aspect = aspect;
    }
}
