package me.loop.mods;

import me.loop.InfinityLoop;
import me.loop.api.managers.TextManager;
import me.loop.api.utils.impl.Util;
import me.loop.mods.gui.InfinityLoopGui;
import me.loop.mods.modules.Module;
import me.loop.mods.settings.Setting;

import java.util.ArrayList;
import java.util.List;

public class Mod
        implements Util {
    public List<Setting> settings = new ArrayList<Setting>();
    public TextManager renderer = InfinityLoop.textManager;
    private String name;

    public Mod(String name) {
        this.name = name;
    }


    public Mod() {
    }

    //On Off

    public boolean isEnabled() {
        if (this instanceof Module) {
            return ((Module) this).isOn();
        }
        return false;
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    //Settings

    public void reset() {
        for (Setting setting : settings) {
            setting.setValue(setting.getDefaultValue());
        }
    }

    public void clearSettings() {
        this.settings = new ArrayList<Setting>();
    }

    public Setting add(Setting setting) {
        setting.setMod(this);
        settings.add(setting);

        if (this instanceof Module && Mod.mc.currentScreen instanceof InfinityLoopGui) {
            InfinityLoopGui.INSTANCE.updateModule((Module) this);
        }

        return setting;
    }

    public void unAdd(Setting settingIn) {
        ArrayList<Setting> removeList = new ArrayList<Setting>();
        for (Setting setting : this.settings) {
            if (!setting.equals(settingIn)) continue;
            removeList.add(setting);
        }
        if (!removeList.isEmpty()) {
            this.settings.removeAll(removeList);
        }
        if (this instanceof Module && Mod.mc.currentScreen instanceof InfinityLoopGui) {
            InfinityLoopGui.INSTANCE.updateModule((Module) this);
        }
    }

    public Setting getSettingByName(String name) {
        for (Setting setting : settings) {
            if (!setting.getName().equalsIgnoreCase(name)) continue;
            return setting;
        }
        return null;
    }

    //Getters

    public String getName() {
        return name;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    //Checks

    public static boolean nullCheck() {
        return Mod.mc.player == null;
    }

    public static boolean fullNullCheck() {
        return Mod.mc.player == null || Mod.mc.world == null;
    }

}

