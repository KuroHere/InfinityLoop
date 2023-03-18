package infinityloop.event.events.render;

import infinityloop.event.Event;

public class PerspectiveEvent extends Event
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
