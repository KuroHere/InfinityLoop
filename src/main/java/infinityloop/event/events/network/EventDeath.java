package infinityloop.event.events.network;

import infinityloop.event.Event;
import net.minecraft.entity.player.EntityPlayer;

public class EventDeath
        extends Event {
    public EntityPlayer player;

    public EventDeath(EntityPlayer player) {
        this.player = player;
    }
}

