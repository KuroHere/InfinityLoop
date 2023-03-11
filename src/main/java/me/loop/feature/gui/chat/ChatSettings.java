



package me.loop.feature.gui.chat;

import net.minecraft.client.*;
import net.minecraftforge.common.config.*;

public class ChatSettings
{
    private final Configuration config;
    public boolean smooth;
    public boolean clear;
    public int xOffset;
    public int yOffset;
    
    public ChatSettings(final Configuration config) {
        this.config = config;
    }
    
    public void saveConfig() {
        this.updateConfig(false);
        this.config.save();
    }
    
    public void loadConfig() {
        this.config.load();
        this.updateConfig(true);
    }
    
    public void resetConfig() {
        Property prop = this.config.get("All",  "Clear",  false);
        prop.set(this.clear = false);
        prop = this.config.get("All",  "Smooth",  true);
        prop.set(this.smooth = true);
        prop = this.config.get("All",  "xOffset",  0);
        prop.set(this.xOffset = 0);
        prop = this.config.get("All",  "yOffset",  0);
        prop.set(this.yOffset = 0);
        Minecraft.getMinecraft().gameSettings.chatScale = 1.0f;
        Minecraft.getMinecraft().gameSettings.chatWidth = 1.0f;
        this.config.save();
    }
    
    private void updateConfig(final boolean load) {
        Property prop = this.config.get("All",  "Clear",  false);
        if (load) {
            this.clear = prop.getBoolean();
        }
        else {
            prop.set(this.clear);
        }
        prop = this.config.get("All",  "Smooth",  true);
        if (load) {
            this.smooth = prop.getBoolean();
        }
        else {
            prop.set(this.smooth);
        }
        prop = this.config.get("All",  "xOffset",  0);
        if (load) {
            this.xOffset = prop.getInt();
        }
        else {
            prop.set(this.xOffset);
        }
        prop = this.config.get("All",  "yOffset",  0);
        if (load) {
            this.yOffset = prop.getInt();
        }
        else {
            prop.set(this.yOffset);
        }
    }
}
