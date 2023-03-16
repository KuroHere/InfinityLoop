package me.loop.api.managers;

import me.loop.InfinityLoop;
import me.loop.mods.Mod;
import me.loop.mods.commands.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.api.events.impl.network.PacketEvent;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReloadManager
        extends Mod {
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
    public void onPacketSend(PacketEvent.Send event) {
        CPacketChatMessage packet;
        if (event.getPacket() instanceof CPacketChatMessage && (packet = (CPacketChatMessage) event.getPacket()).getMessage().startsWith(this.prefix) && packet.getMessage().contains("reload")) {
            InfinityLoop.load();
            event.setCanceled(true);
        }
    }
}

