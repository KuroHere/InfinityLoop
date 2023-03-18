package infinityloop.event.events.network;

import infinityloop.event.Event;
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
