package infinityloop.features.modules.misc;

import infinityloop.InfinityLoop;
import infinityloop.event.events.network.EventPacket;
import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.setting.Setting;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatModifier
        extends Module {
    private static ChatModifier INSTANCE = new ChatModifier();
    public Setting<Boolean> clean = this.register(new Setting<Boolean>("NoChatBackground", Boolean.valueOf(false), "Cleans your chat"));
    public Setting<Boolean> infinite = this.register(new Setting<Boolean>("InfiniteChat", Boolean.valueOf(false), "Makes your chat infinite."));
    public boolean check;

    public ChatModifier() {
        super("BetterChat", "Modifies your chat", ModuleCategory.MISC);
        this.setInstance();
    }

    public static ChatModifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatModifier();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketSend(EventPacket.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String s = ((CPacketChatMessage) event.getPacket()).getMessage();
            this.check = !s.startsWith(InfinityLoop.commandManager.getPrefix());
        }
    }
}

