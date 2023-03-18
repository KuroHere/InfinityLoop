package infinityloop.event.events.world;

import infinityloop.event.Event;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class EventBlock
        extends Event {
    public BlockPos pos;
    public EnumFacing facing;

    public EventBlock(BlockPos pos, EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}
