package me.loop.api.events.impl.network;

import me.loop.api.events.Event;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class EventPacket
        extends Event {

    private final Packet<?> packet;

    public EventPacket(int stage, Packet<?> packet) {
        super(stage);
        this.packet = packet;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T) packet;
    }

    @Cancelable
    public static class Send extends EventPacket {

        public Send(int stage, Packet<?> packet) {
            super(stage, packet);
        }
    }

    @Cancelable
    public static class Receive extends EventPacket {

        public Receive(int stage, Packet<?> packet) {
            super(stage, packet);
        }
    }
}