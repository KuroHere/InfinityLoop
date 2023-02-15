package com.me.infinity.loop.features.ui.screen.taskbar;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.ui.font.FontRender;
import com.me.infinity.loop.features.ui.screen.DrawableComponent;
import com.me.infinity.loop.features.modules.client.ClickGui;
import com.me.infinity.loop.features.modules.client.Colors;
import com.me.infinity.loop.util.interfaces.Util;
import com.me.infinity.loop.util.renders.ColorUtil;
import com.me.infinity.loop.util.renders.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.lwjgl.opengl.GL11.*;

public class Taskbar extends DrawableComponent {
    private int color;
    public static String description = "";

    @Override
    public void drawComponent() {
        ScaledResolution resolution = new ScaledResolution(mc);
        if (ClickGui.getInstance().colorSync.getValue()) {
            RenderUtil.drawRect(0, 0, resolution.getScaledWidth(), 31, new Color(Colors.getInstance().red.getValue(), Colors.getInstance().green.getValue(), Colors.getInstance().blue.getValue(), 250).getRGB());
        } else {
            RenderUtil.drawRect(0, 0, resolution.getScaledWidth(), 31, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 250).getRGB());
        }
        if (ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
            RenderUtil.drawRect(0, 0, resolution.getScaledWidth(), 31, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color);
        }
        RenderUtil.drawRect(0, 0, resolution.getScaledWidth(), 30, new Color(23, 23, 29).getRGB());

        // taskbar
        RenderUtil.drawRect(0, resolution.getScaledHeight() - 34, resolution.getScaledWidth(), 34, new Color(23, 23, 29).getRGB());

        glPushMatrix();

        glColor4d(1, 1, 1, 1);

        // logo
        mc.getTextureManager().bindTexture(new ResourceLocation("loop", "imgs/logotransparent.png"));
        GuiScreen.drawModalRectWithCustomSizedTexture(resolution.getScaledWidth() - 130, resolution.getScaledHeight() - 57, 0, 0, 132, 54, 132, 54);

        glPopMatrix();

        glPushMatrix();

        // player name scaled width
        float scaledWidth = (InfinityLoop.textManager.getStringWidth(mc.player.getName()) + 8) * 2.75F;

        RenderUtil.drawRect(0, resolution.getScaledHeight() - 44, scaledWidth, 44, new Color(23, 23, 29).getRGB());

        // time and date
        glScaled(1.25, 1.25, 1.25); {
            float scaledX = 7 * 0.3F;
            float scaledY = (resolution.getScaledHeight() - 35) * 0.3F;
            String time = ChatFormatting.WHITE + (new SimpleDateFormat("h:mm:s a")).format(new Date());
            //String date = ChatFormatting.WHITE + (new SimpleDateFormat("d/m/y")).format(new Date());
            mc.fontRenderer.drawString(time, scaledX, 0, -1, false);
            //mc.fontRenderer.drawString(date, scaledX * 2, scaledY * 2, -1, false);
        }

        glScaled(0.3F, 0.3F, 0.3F);

        glPopMatrix();

        // player info
        glScaled(2.75, 2.75, 2.75); {
            float scaledX = 7 * 0.36363636F;
            float scaledY = (resolution.getScaledHeight() - 35) * 0.36363636F;
            InfinityLoop.textManager.drawStringWithShadow(mc.player.getName(), scaledX, scaledY, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue()).getRGB());
        }

        glScaled(0.36363636F, 0.36363636F, 0.36363636F);

        glPopMatrix();

    }
}
