package me.loop.client.modules.impl.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.managers.Managers;
import me.loop.client.commands.Command;
import me.loop.client.modules.Module;
import me.loop.client.modules.Category;
import me.loop.client.modules.settings.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MainSettings extends Module {

    public Setting<Boolean> showcapes = this.add(new Setting<>("Capes", true));
    public Setting<Boolean> DownloadCapes = this.add(new Setting<>("DownloadCapes", true));
    public Setting<Boolean> notifyToggles = this.add(new Setting<>("NotifyToggles", false));
    public Setting<String> prefix = this.add(new Setting<String>("Prefix", "."));
    public Setting<Boolean> mainMenu = this.add(new Setting<>("MainMenu", true));
    public Setting<ShaderModeEn> shaderMode = add(new Setting("ShaderMode", ShaderModeEn.Smoke2, v -> this.mainMenu.getValue()));

    public MainSettings() {
        super("MainSettings", "MAIN MENU", Category.CLIENT, true, false, false);
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 0 && event.getSetting().getClient().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                Managers.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + Managers.commandManager.getPrefix());
            }
        }
    }

    @Override
    public void onLoad() {
        Managers.commandManager.setPrefix(this.prefix.getValue());
    }

    public enum ShaderModeEn {
        Smoke,
        Smoke2,
        Dicks
    }
}
