package me.loop.api.events.impl.network;

import me.loop.api.events.Event;
import net.minecraft.entity.player.EntityPlayer;

public class EventDeath
        extends Event {
    public EntityPlayer player;

    public EventDeath(EntityPlayer player) {
        this.player = player;
    }
}

