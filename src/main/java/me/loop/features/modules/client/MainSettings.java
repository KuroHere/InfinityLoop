package me.loop.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.InfinityLoop;
import me.loop.event.events.ClientEvent;
import me.loop.features.command.Command;
import me.loop.features.modules.Module;
import me.loop.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MainSettings extends Module {

    public Setting<Boolean> showcapes = this.register(new Setting<>("Capes", true));
    public Setting<Boolean> DownloadCapes = this.register(new Setting<>("DownloadCapes", true));
    public Setting<Boolean> notifyToggles = this.register(new Setting<>("NotifyToggles", false));
    public Setting<String> prefix = this.register(new Setting<String>("Prefix", "."));
    public Setting<Boolean> mainMenu = this.register(new Setting<>("MainMenu", true));
    public Setting<ShaderModeEn> shaderMode = this.register(new Setting("ShaderMode", ShaderModeEn.Smoke2, v -> this.mainMenu.getValue()));
    public Setting<Boolean> ofFastRender = this.register(new Setting<>("TurnOffFastRender", true));

    public MainSettings() {
        super("MainSettings", "MAIN MENU", Category.CLIENT, true, false, true);
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 0 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                InfinityLoop.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + InfinityLoop.commandManager.getPrefix());
            }
        }
    }

    @Override
    public void onLoad() {
        InfinityLoop.commandManager.setPrefix(this.prefix.getValue());
    }

    public enum ShaderModeEn {
        Smoke,
        Smoke2,
        Dicks
    }
}
