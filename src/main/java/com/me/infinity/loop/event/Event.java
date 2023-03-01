package com.me.infinity.loop.event;

public abstract class Event
        extends net.minecraftforge.fml.common.eventhandler.Event {
    private boolean cancelled;
    private Stage  stage;

    public Event() {
    }

    public Event(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    public boolean isPre() {
        return this.stage == Stage.PRE;
    }

    public boolean isPost() {
        return this.stage == Stage.POST;
    }
    
    public static enum Stage {
        PRE,
        POST

    }

}

