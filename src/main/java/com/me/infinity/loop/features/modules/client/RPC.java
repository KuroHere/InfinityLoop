package com.me.infinity.loop.features.modules.client;

import com.me.infinity.loop.DiscordPresence;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;
import com.me.infinity.loop.features.setting.Setting;

public class RPC
        extends Module
{
    public static RPC INSTANCE;
    public Setting<Boolean> showIP = this.register(new Setting<>("IP", false));
    public Setting<Boolean> users = this.register(new Setting<>("users", false));

    public RPC() {
        super("RPC", "Discord rich presence", ModuleCategory.CLIENT);
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
