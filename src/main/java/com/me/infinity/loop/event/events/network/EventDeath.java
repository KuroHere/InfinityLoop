package com.me.infinity.loop.event.events.network;

import com.me.infinity.loop.event.Event;
import net.minecraft.entity.player.EntityPlayer;

public class EventDeath
        extends Event {
    public EntityPlayer player;

    public EventDeath(EntityPlayer player) {
        this.player = player;
    }
}

