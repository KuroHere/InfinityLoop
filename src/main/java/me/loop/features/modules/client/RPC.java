package me.loop.features.modules.client;

import me.loop.DiscordPresence;
import me.loop.features.modules.Module;
import me.loop.features.setting.Setting;

public class RPC
        extends Module {
    public static RPC INSTANCE;
    public Setting<Boolean> showIP = this.register(new Setting<>("IP", false));
    public Setting<Boolean> users = this.register(new Setting<>("users", false));

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
