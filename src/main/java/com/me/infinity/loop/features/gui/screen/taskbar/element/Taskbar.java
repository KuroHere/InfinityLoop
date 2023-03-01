package com.me.infinity.loop.features.gui.screen.taskbar.element;

import com.me.infinity.loop.features.gui.screen.DrawableComponent;
import com.me.infinity.loop.features.modules.client.ClickGui.ClickGui;
import com.me.infinity.loop.features.modules.client.Colors;
import com.me.infinity.loop.util.utils.renders.ColorUtil;
import com.me.infinity.loop.util.utils.renders.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class Taskbar extends DrawableComponent {
    private int color;

    @Override
    public void drawComponent() {
        ScaledResolution resolution = new ScaledResolution(mc);
        if (ClickGui.getInstance().colorSync.getValue()) {
            RenderUtil.drawOutlineRect(0, 0, resolution.getScaledWidth(), 30, new Color(Colors.getInstance().red.getValue(), Colors.getInstance().green.getValue(), Colors.getInstance().blue.getValue(), 250), 3f);
        } else {
            RenderUtil.drawOutlineRect(0, 0, resolution.getScaledWidth(), 30,  new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 250), 3f);
            //RenderUtil.drawRect(0, 0, resolution.getScaledWidth(), 31, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 250).getRGB());
        }
        if (ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
            RenderUtil.drawOutlineRect(0, 0, resolution.getScaledWidth(), 30, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue())) : new Color(Colors.getInstance().red.getValue(), Colors.getInstance().green.getValue(), Colors.getInstance().blue.getValue()), 3f);
        }
        RenderUtil.drawRect(0, 0, resolution.getScaledWidth(), 30, new Color(23, 23, 29).getRGB());
        glPushMatrix();

        glColor4d(1, 1, 1, 1);

        // logo
        mc.getTextureManager().bindTexture(new ResourceLocation("loop", "imgs/logotransparent.png"));
        GuiScreen.drawModalRectWithCustomSizedTexture(resolution.getScaledWidth() - 129, resolution.getScaledHeight() - 57, 0, 0, 132, 54, 132, 54);

        glPopMatrix();

        glPushMatrix();

        /* // player name scaled width
        float scaledWidth = (InfinityLoop.textManager.getStringWidth(mc.player.getName()) + 8) * 2.75F;

        RenderUtil.drawRect(0, resolution.getScaledHeight() - 44, scaledWidth, 44, new Color(23, 23, 29).getRGB());

        // player info
        glScaled(2.75, 2.75, 2.75); {
            float scaledX = 7 * 0.36363636F;
            float scaledY = (resolution.getScaledHeight() - 35) * 0.36363636F;
            InfinityLoop.textManager.drawStringWithShadow(mc.player.getName(), scaledX, scaledY, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue()).getRGB());
        }

        glScaled(0.36363636F, 0.36363636F, 0.36363636F);

        glPopMatrix();*/

    }
}
