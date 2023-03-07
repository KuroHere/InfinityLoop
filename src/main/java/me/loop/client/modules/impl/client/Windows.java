package me.loop.client.modules.impl.client;

import me.loop.client.gui.windows.WindowsGui;
import me.loop.client.modules.Module;
import me.loop.client.modules.Category;
import me.loop.client.modules.settings.Setting;
import me.loop.api.utils.impl.Util;

public class Windows extends Module {

    private static Windows INSTANCE = new Windows();

    public Windows() {
        super("Windows", "Windows", Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public Setting<Boolean> friends = add(new Setting<>("friends", true));
    public Setting<Boolean> configs = add(new Setting<>("configs", true));
    public Setting<Boolean> altmanager = add(new Setting<>("altmanager", true));
    public Setting<Boolean> packets = add(new Setting<>("packets", true));


    public static Windows getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Windows();
        }
        return INSTANCE;
    }
    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        Util.mc.displayGuiScreen(WindowsGui.getWindowsGui());
        toggle();
    }

}
