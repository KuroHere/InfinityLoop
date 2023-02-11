package com.me.infinity.loop.features.clickGui.screen.windows;

import com.me.infinity.loop.features.clickGui.screen.windows.window.WindowConfig;
import com.me.infinity.loop.features.clickGui.screen.windows.window.WindowFriends;
import com.me.infinity.loop.features.clickGui.screen.windows.window.WindowPackets;
import com.me.infinity.loop.features.clickGui.screen.windows.window.WindowAltManager;
import com.me.infinity.loop.features.modules.client.ClickGui;
import com.me.infinity.loop.features.modules.client.Colors;
import com.me.infinity.loop.features.modules.client.Windows;
import com.me.infinity.loop.features.setting.Bind;
import com.me.infinity.loop.util.renders.ColorUtil;
import com.me.infinity.loop.util.renders.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.*;

import static com.me.infinity.loop.util.interfaces.Util.mc;

public class WindowsGui extends GuiScreen {

    private static WindowsGui INSTANCE;

    static {
        INSTANCE = new WindowsGui();
    }

    public WindowsGui() {
        this.setInstance();
        this.load();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public static WindowsGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WindowsGui();
        }
        return INSTANCE;
    }

    public static WindowsGui getWindowsGui() {
        return getInstance();
    }

    private void load() {

    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        int color = new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue()).getRGB();
        if (ClickGui.getInstance().background.getValue()) {
            if (ClickGui.getInstance().dark.getValue()) {
                this.drawDefaultBackground();
            }
            if (ClickGui.getInstance().blur.getValue()) {
                RenderUtil.drawBlurryRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), ClickGui.getInstance().blurAmount.getValue(), ClickGui.getInstance().blurSize.getValue());
            }
            if (ClickGui.getInstance().gradiant.getValue()) {
                RenderUtil.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() + ClickGui.getInstance().gradiantHeight.getValue(), 0, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().gradiantAlpha.getValue()).getRGB());
                if (ClickGui.getInstance().gradiant.getValue() && ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
                    RenderUtil.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() + ClickGui.getInstance().gradiantHeight.getValue().intValue(), 0, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : color);
                }
            }
        }
        if(Windows.getInstance().altmanager.getValue())
            WindowAltManager.drawScreen(mouseX,mouseY, partialTicks);
        if(Windows.getInstance().configs.getValue())
            WindowConfig.drawScreen(mouseX,mouseY, partialTicks);
        if(Windows.getInstance().packets.getValue())
            WindowPackets.drawScreen(mouseX,mouseY, partialTicks);
        if(Windows.getInstance().friends.getValue())
            WindowFriends.drawScreen(mouseX,mouseY, partialTicks);
    }

    public void keyTyped(char typedChar, int keyCode) {
        Bind bind = new Bind(keyCode);

        if (bind.toString().equalsIgnoreCase("Escape")){
            Mouse.setGrabbed(false);
            mc.currentScreen = null;
        }
        if(Windows.getInstance().altmanager.getValue())
            WindowAltManager.keyTyped(typedChar, keyCode);
        if(Windows.getInstance().configs.getValue())
            WindowConfig.keyTyped(typedChar, keyCode);
        if(Windows.getInstance().packets.getValue())
            WindowPackets.keyTyped(typedChar, keyCode);
        if(Windows.getInstance().friends.getValue())
            WindowFriends.keyTyped(typedChar, keyCode);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(Windows.getInstance().altmanager.getValue())
            WindowAltManager.mouseClicked(mouseX, mouseY,mouseButton);
        if(Windows.getInstance().configs.getValue())
            WindowConfig.mouseClicked(mouseX, mouseY,mouseButton);
        if(Windows.getInstance().packets.getValue())
            WindowPackets.mouseClicked(mouseX, mouseY,mouseButton);
        if(Windows.getInstance().friends.getValue())
            WindowFriends.mouseClicked(mouseX, mouseY,mouseButton);
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(Windows.getInstance().altmanager.getValue())
            WindowAltManager.mouseReleased(mouseX, mouseY,state);
        if(Windows.getInstance().configs.getValue())
            WindowConfig.mouseReleased(mouseX, mouseY,state);
        if(Windows.getInstance().packets.getValue())
            WindowPackets.mouseReleased(mouseX, mouseY,state);
        if(Windows.getInstance().friends.getValue())
            WindowFriends.mouseReleased(mouseX, mouseY,state);
    }


}
