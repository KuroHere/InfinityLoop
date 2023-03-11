package me.loop;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.loop.feature.modules.impl.client.RPC;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;

public class DiscordPresence
{
    public static DiscordRichPresence presence;
    private static final DiscordRPC rpc;
    private static Thread thread;
    private static int index;

    public static void start() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        DiscordPresence.rpc.Discord_Initialize("1068066291086270484", handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        DiscordPresence.presence.details = ((Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) ? "Main menu" : ("Playing " + ((Minecraft.getMinecraft().currentServerData != null) ? (RPC.INSTANCE.showIP.getValue() ? ("on " + Minecraft.getMinecraft().currentServerData.serverIP + ".") : " multiplayer") : " singleplayer")));
        DiscordPresence.presence.state = "Sex activity";
        DiscordPresence.presence.largeImageKey = "kurohack";
        DiscordPresence.presence.largeImageText = "v6.9.4.20";
        DiscordPresence.presence.smallImageKey = "kurohack";
        DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
        final DiscordRichPresence[] presence = new DiscordRichPresence[1];
        final String[] string = null;
        final String[] string2 = null;
        final StringBuilder sb = null;
        (DiscordPresence.thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                DiscordPresence.rpc.Discord_RunCallbacks();
                presence[0] = DiscordPresence.presence;
                if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) {
                    string[0] = "Main menu";
                }
                else {
                    new StringBuilder().append("Playing ");
                    if (Minecraft.getMinecraft().currentServerData != null) {
                        if (RPC.INSTANCE.showIP.getValue()) {
                            string2[0] = "on " + Minecraft.getMinecraft().currentServerData.serverIP + ".";
                        }
                        else {
                            string2[0] = " multiplayer";
                        }
                    }
                    else {
                        string2[0] = " singleplayer";
                    }
                    string[0] = sb.append(string2[0]).toString();
                }
                presence[0].details = string[0];
                DiscordPresence.presence.state = "Look at me!";
                if (RPC.INSTANCE.users.getValue() && DiscordPresence.index == 6) {
                    DiscordPresence.index = 1;
                }
                DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException ex) {}
            }
        }, "RPC-Callback-Handler")).start();
    }

    public static void stop() {
        if (DiscordPresence.thread != null && !DiscordPresence.thread.isInterrupted()) {
            DiscordPresence.thread.interrupt();
        }
        DiscordPresence.rpc.Discord_Shutdown();
    }

    static {
        DiscordPresence.index = 1;
        rpc = DiscordRPC.INSTANCE;
        DiscordPresence.presence = new DiscordRichPresence();
    }
}