package me.loop.features.modules.client;


import me.loop.features.gui.other.windows.WindowsGui;
import me.loop.features.modules.Module;
import me.loop.features.setting.Setting;
import me.loop.util.impl.Util;

public class Windows extends Module {

    private static Windows INSTANCE = new Windows();

    public Windows() {
        super("Windows", "Windows", Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public Setting<Boolean> friends = register(new Setting<>("friends", true));
    public Setting<Boolean> configs = register(new Setting<>("configs", true));
    public Setting<Boolean> altmanager = register(new Setting<>("altmanager", true));
    public Setting<Boolean> packets = register(new Setting<>("packets", true));


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