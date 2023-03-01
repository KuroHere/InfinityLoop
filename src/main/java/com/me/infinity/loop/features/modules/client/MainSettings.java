package com.me.infinity.loop.features.modules.client;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.event.events.client.ClientEvent;
import com.me.infinity.loop.features.command.Command;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;
import com.me.infinity.loop.features.setting.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MainSettings extends Module {

    public Setting<Boolean> showcapes = this.register(new Setting<>("Capes", true));
    public Setting<Boolean> DownloadCapes = this.register(new Setting<>("DownloadCapes", true));
    public Setting<Boolean> notifyToggles = this.register(new Setting<>("NotifyToggles", false));
    public Setting<Boolean> renderRotations = this.register(new Setting<>("RenderRotations", true));
    public Setting<String> prefix = this.register(new Setting<String>("Prefix", "."));
    public Setting<Boolean> mainMenu = this.register(new Setting<>("MainMenu", true));
    public Setting<ShaderModeEn> shaderMode = register(new Setting("ShaderMode", ShaderModeEn.Smoke2, v -> this.mainMenu.getValue()));

    public MainSettings() {
        super("MainSettings", "MAIN MENU", ModuleCategory.CLIENT);
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.isPre() && event.getSetting().getFeature().equals(this)) {
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
