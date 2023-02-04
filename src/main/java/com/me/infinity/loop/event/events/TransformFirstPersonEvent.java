package com.me.infinity.loop.event.events;

import com.me.infinity.loop.event.EventEnum;
import com.me.infinity.loop.event.EventStage;
import net.minecraft.util.EnumHandSide;

public class TransformFirstPersonEvent extends EventStage
{
    private final EnumHandSide enumHandSide;

    public TransformFirstPersonEvent(final EventEnum stage, final EnumHandSide enumHandSide) {
        super(stage.ordinal());
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
