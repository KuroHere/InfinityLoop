package me.loop.client.modules.impl.render;

import me.loop.api.events.impl.network.EventPacket;
import me.loop.client.modules.Category;
import me.loop.client.modules.Module;
import me.loop.client.modules.settings.Setting;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CustomTime extends Module {
    public Setting<Integer> time = this.add(new Setting<>("Time", 1, 1, 24));

    public CustomTime() {
        super("CustomTime","Change your world time(Clientside)", Category.RENDER, true, false, false);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent e) {
        if (this.nullCheck()) {
            return;
        }
        this.mc.world.setWorldTime((long)(this.time.getValue() * 1000));
        this.setHudInfo("" + this.time.getValue());
    }

    @SubscribeEvent
    public void onPacketReceive(EventPacket.Receive event) {
        if (this.nullCheck()) {
            return;
        }
        if (!(event.getPacket() instanceof SPacketTimeUpdate)) return;
        event.setCanceled(true);
    }
}
