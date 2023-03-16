package me.loop.mods.modules.impl.client;

import me.loop.DiscordPresence;
import me.loop.mods.modules.Module;
import me.loop.mods.modules.Category;
import me.loop.mods.settings.Setting;

public class RPC
        extends Module
{
    public static RPC INSTANCE;
    public Setting<Boolean> showIP = this.add(new Setting<>("IP", false));
    public Setting<Boolean> users = this.add(new Setting<>("users", false));

    public RPC() {
        super("RPC", "Discord rich presence", Category.CLIENT, true, false);
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
