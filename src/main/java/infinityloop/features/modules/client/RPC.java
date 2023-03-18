package infinityloop.features.modules.client;

import infinityloop.DiscordPresence;
import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.setting.Setting;

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
