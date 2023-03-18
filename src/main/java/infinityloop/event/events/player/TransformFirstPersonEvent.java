package infinityloop.event.events.player;

import infinityloop.event.Event;
import net.minecraft.util.EnumHandSide;

public class TransformFirstPersonEvent extends Event
{
    private final EnumHandSide enumHandSide;

    public TransformFirstPersonEvent(final Stage stage, final EnumHandSide enumHandSide) {
        super(stage);
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
