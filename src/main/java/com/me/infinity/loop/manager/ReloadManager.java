package com.me.infinity.loop.manager;

import com.me.infinity.loop.InfinityLoop;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.me.infinity.loop.event.events.network.EventPacket;
import com.me.infinity.loop.features.Feature;
import com.me.infinity.loop.features.command.Command;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReloadManager
        extends Feature {
    public String prefix;

    public void init(String prefix) {
        this.prefix = prefix;
        MinecraftForge.EVENT_BUS.register(this);
        if (!ReloadManager.fullNullCheck()) {
            Command.sendMessage(ChatFormatting.RED + "Loop has been unloaded. Type " + prefix + "reload to reload.");
        }
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onPacketSend(EventPacket.Send event) {
        CPacketChatMessage packet;
        if (event.getPacket() instanceof CPacketChatMessage && (packet = (CPacketChatMessage) event.getPacket()).getMessage().startsWith(this.prefix) && packet.getMessage().contains("reload")) {
            InfinityLoop.load();
            event.setCanceled(true);
        }
    }
}

