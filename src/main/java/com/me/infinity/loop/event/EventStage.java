package com.me.infinity.loop.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class EventStage
        extends Event {
    private int stage;
    private boolean canceled;

    public EventStage() {
    }

    public EventStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage(final int stage) {
        this.stage = stage;
    }

    public void setCanceledE() {
        this.canceled = true;
    }

    public boolean isCanceledE() {
        return this.canceled;
    }

}

