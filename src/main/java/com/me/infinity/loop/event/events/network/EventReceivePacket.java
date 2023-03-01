package com.me.infinity.loop.event.events.network;

import com.me.infinity.loop.event.Event;
import net.minecraft.network.Packet;

public class EventReceivePacket
        extends Event {
    private Packet packet;

    public EventReceivePacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
