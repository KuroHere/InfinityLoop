package me.loop.client;

import me.loop.api.managers.Managers;
import me.loop.api.managers.impl.TextManager;
import me.loop.api.utils.impl.Util;
import me.loop.client.gui.InfinityLoopGui;
import me.loop.client.modules.Module;
import me.loop.client.modules.settings.Setting;

import java.util.ArrayList;
import java.util.List;

public class Client
        implements Util {
    public List<Setting> settings = new ArrayList<Setting>();
    public TextManager renderer = Managers.textManager;
    private String name;

    public Client(String name) {
        this.name = name;
    }


    public Client() {
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
        setting.setClient(this);
        settings.add(setting);

        if (this instanceof Module && Client.mc.currentScreen instanceof InfinityLoopGui) {
            InfinityLoopGui.getInstance().updateModule((Module) this);
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
        if (this instanceof Module && Client.mc.currentScreen instanceof InfinityLoopGui) {
            InfinityLoopGui.getInstance().updateModule((Module) this);
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
        return Client.mc.player == null;
    }

    public static boolean fullNullCheck() {
        return Client.mc.player == null || Client.mc.world == null;
    }

}

