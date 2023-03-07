package me.loop.api.events.impl.world;

import me.loop.api.events.Event;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class EventBlock
        extends Event {
    public BlockPos pos;
    public EnumFacing facing;

    public EventBlock(int stage,BlockPos pos, EnumFacing facing) {
        super(stage);
        this.pos = pos;
        this.facing = facing;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}
