package com.me.infinity.loop.event.events.network;

import com.me.infinity.loop.event.Event;
import net.minecraft.network.Packet;

public class EventPacket
        extends Event {
    public Packet packet;

    public EventPacket(Event.Stage stage, Packet packet) {
        super(stage);
        this.packet = packet;
    }
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }


    public Packet getPacket() {
        return this.packet;
    }

    public static class Receive
            extends EventPacket {
        public Receive(Event.Stage stage, Packet packet) {
            super(stage, packet);
        }
    }

    public static class Send
            extends EventPacket {
        public Send(Event.Stage stage, Packet packet) {
            super(stage, packet);
        }
    }
}