package me.loop.client.modules.impl.client;

import me.loop.DiscordPresence;
import me.loop.client.modules.Module;
import me.loop.client.modules.Category;
import me.loop.client.modules.settings.Setting;

public class RPC
        extends Module
{
    public static RPC INSTANCE;
    public Setting<Boolean> showIP = this.add(new Setting<>("IP", false));
    public Setting<Boolean> users = this.add(new Setting<>("users", false));

    public RPC() {
        super("RPC", "Discord rich presence", Category.CLIENT, true, false, true);
        RPC.INSTANCE = this;
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();
    }

    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }
}
