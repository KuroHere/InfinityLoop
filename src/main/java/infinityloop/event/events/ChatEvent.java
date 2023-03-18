package infinityloop.event.events;

import infinityloop.event.Event;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ChatEvent
        extends Event {
    private final String msg;

    public ChatEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}
