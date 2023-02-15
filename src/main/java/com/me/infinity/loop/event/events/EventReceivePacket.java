package com.me.infinity.loop.event.events;

import com.me.infinity.loop.event.EventStage;
import net.minecraft.network.Packet;

public class EventReceivePacket
        extends EventStage {
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
