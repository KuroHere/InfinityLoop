package com.me.infinity.loop.manager;

import com.me.infinity.loop.features.ui.InfinityLoopGui;
import com.me.infinity.loop.features.ui.screen.windows.WindowsGui;
import com.me.infinity.loop.mixin.MixinInterface;
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
