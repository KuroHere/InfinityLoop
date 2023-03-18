package infinityloop.features.modules.client;

import infinityloop.features.gui.screen.windows.WindowsGui;
import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.setting.Setting;
import infinityloop.util.utils.Util;

public class Windows extends Module {

    private static Windows INSTANCE = new Windows();

    public Windows() {
        super("Windows", "Windows", ModuleCategory.CLIENT);
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
