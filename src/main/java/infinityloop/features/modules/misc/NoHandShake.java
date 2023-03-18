package infinityloop.features.modules.misc;

import infinityloop.event.events.network.EventPacket;
import infinityloop.features.modules.ModuleCategory;
import io.netty.buffer.Unpooled;
import infinityloop.features.modules.Module;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class NoHandShake
        extends Module {
    public NoHandShake() {
        super("NoHandshake", "Doesnt send your modlist to the server.", ModuleCategory.MISC);
    }

    @SubscribeEvent
    public void onPacketSend(EventPacket.Send event) {
        CPacketCustomPayload packet;
        if (event.getPacket() instanceof FMLProxyPacket && !mc.isSingleplayer()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCustomPayload && (packet = (CPacketCustomPayload) event.getPacket()).getChannelName().equals("MC|Brand")) {
            packet.data = new PacketBuffer(Unpooled.buffer()).writeString("vanilla");
        }
    }
}
