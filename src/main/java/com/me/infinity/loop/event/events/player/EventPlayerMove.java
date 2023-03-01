package com.me.infinity.loop.event.events.player;

import com.me.infinity.loop.event.Event;
import net.minecraft.entity.MoverType;

public class EventPlayerMove extends Event {
    public MoverType type;
    public double x;
    public double y;
    public double z;

    public EventPlayerMove(MoverType type, double x, double y, double z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setType(final MoverType type) {
        this.type = type;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public MoverType getType() {
        return this.type;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}