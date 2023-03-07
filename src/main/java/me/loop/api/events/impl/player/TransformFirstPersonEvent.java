package me.loop.api.events.impl.player;

import me.loop.api.events.Event;
import net.minecraft.util.EnumHandSide;

public class TransformFirstPersonEvent extends Event
{
    private final EnumHandSide enumHandSide;

    public TransformFirstPersonEvent(final EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
