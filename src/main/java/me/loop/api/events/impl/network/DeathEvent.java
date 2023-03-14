package me.loop.api.events.impl.network;

import me.loop.api.events.Event;
import net.minecraft.entity.player.EntityPlayer;

public class DeathEvent
        extends Event {
    public EntityPlayer player;

    public DeathEvent(EntityPlayer player) {
        this.player = player;
    }
}

