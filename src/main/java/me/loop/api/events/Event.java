package me.loop.api.events;

import me.loop.InfinityLoop;

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

    public void cancel() {
        try {
            setCanceled(true);

        } catch (Exception e) {
            InfinityLoop.LOGGER.info(getClass().toString() + " Isn't cancellable!");
        }
    }
}

