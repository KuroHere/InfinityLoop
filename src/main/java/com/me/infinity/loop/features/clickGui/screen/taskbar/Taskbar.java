package com.me.infinity.loop.features.clickGui.screen.taskbar;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.clickGui.screen.DrawableComponent;
import com.me.infinity.loop.features.modules.client.ClickGui;
import com.me.infinity.loop.features.modules.client.Colors;
import com.me.infinity.loop.util.renders.ColorUtil;
import com.me.infinity.loop.util.renders.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.inventory.ClickType;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.lwjgl.opengl.GL11.*;

public class Taskbar extends DrawableComponent {
    private int color;
    @Override
    public void drawComponent() {
        ScaledResolution resolution = new ScaledResolution(mc);
        if (ClickGui.getInstance().colorSync.getValue()) {
            RenderUtil.drawRect(0, 0, resolution.getScaledWidth(), 21, new Color(Colors.getInstance().red.getValue(), Colors.getInstance().green.getValue(), Colors.getInstance().blue.getValue(), 250).getRGB());
        } else {
            RenderUtil.drawRect(0, 0, resolution.getScaledWidth(), 21, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 250).getRGB());
        }
        if (ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
            RenderUtil.drawRect(0, 0, resolution.getScaledWidth(), 21, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color);
        }
        RenderUtil.drawRect(0, 0, resolution.getScaledWidth(), 20, new Color(15, 15, 15, 255).getRGB());
        String time = ChatFormatting.WHITE + (new SimpleDateFormat("h:mm a")).format(new Date());
        String date = ChatFormatting.WHITE + (new SimpleDateFormat("d/m/y")).format(new Date());
        InfinityLoop.textManager.drawStringWithShadow(time, 5, 1, 0);
        InfinityLoop.textManager.drawStringWithShadow(date, 5, 12, 0);


        // taskbar
        RenderUtil.drawRect(0, resolution.getScaledHeight() - 34, resolution.getScaledWidth(), 34, new Color(23, 23, 29).getRGB());

        glPushMatrix();

        glColor4d(1, 1, 1, 1);

        // logo
        mc.getTextureManager().bindTexture(new ResourceLocation("loop", "textures/imgs/logotransparent.png"));
        GuiScreen.drawModalRectWithCustomSizedTexture(resolution.getScaledWidth() - 104, resolution.getScaledHeight() - 31, 0, 0, 104, 28, 104, 28);

        glPopMatrix();

        glPushMatrix();

        // player name scaled width
        float scaledWidth = (InfinityLoop.textManager.getStringWidth(mc.player.getName()) + 8) * 2.75F;

        RenderUtil.drawRect(0, resolution.getScaledHeight() - 44, scaledWidth, 44, new Color(23, 23, 29).getRGB());

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
