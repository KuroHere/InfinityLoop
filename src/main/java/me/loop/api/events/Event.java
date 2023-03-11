package me.loop.api.events;

public abstract class Event
        extends net.minecraftforge.fml.common.eventhandler.Event {
    private int stage;

    public Event() {
    }

    public Event(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

}

