package infinityloop.manager;

import infinityloop.features.gui.InfinityLoopGui;
import infinityloop.features.gui.screen.windows.WindowsGui;
import infinityloop.mixin.MixinInterface;
import net.minecraft.client.gui.GuiScreen;

public class ScreenManager implements MixinInterface {

    public static Screen screen = Screen.ClickGui;

    public static void setScreen(GuiScreen guiScreen) {
        mc.displayGuiScreen(guiScreen);
        if (guiScreen instanceof InfinityLoopGui)
            screen = Screen.ClickGui;
        //else if (guiScreen instanceof HUD)
            //screen = Screen.Hud;
        else if (guiScreen instanceof WindowsGui)
            screen = Screen.Windows;

        //mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    }

    public enum Screen {
        ClickGui,
        Hud,
        Windows
    }
}
