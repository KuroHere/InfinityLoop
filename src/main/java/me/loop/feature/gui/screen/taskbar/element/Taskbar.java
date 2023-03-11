package me.loop.feature.gui.screen.taskbar.element;

import me.loop.feature.modules.impl.client.ClickGui.ClickGui;
import me.loop.feature.modules.impl.client.Colors;
import me.loop.feature.gui.screen.DrawableComponent;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
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
            RenderUtil.drawOutlineRect(0, 0, resolution.getScaledWidth(), 30, new Color(Colors.getInstance().c.getValue().getRed(), Colors.getInstance().c.getValue().getGreen(), Colors.getInstance().c.getValue().getBlue(), 255), 3f);
        } else {
            RenderUtil.drawOutlineRect(0, 0, resolution.getScaledWidth(), 30, new Color(ClickGui.getInstance().moduleMainC.getValue().getRed(), ClickGui.getInstance().moduleMainC.getValue().getGreen(), ClickGui.getInstance().moduleMainC.getValue().getBlue(), 250), 3f);
            //RenderUtil.drawRect(0, 0, resolution.getScaledWidth(), 31, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 250).getRGB());
        }
        if (ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
            RenderUtil.drawOutlineRect(0, 0, resolution.getScaledWidth(), 30, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue())) : new Color(Colors.getInstance().c.getValue().getRed(), Colors.getInstance().c.getValue().getGreen(), Colors.getInstance().c.getValue().getBlue()), 3f);
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
        float scaledWidth = (Managers.textManager.getStringWidth(mc.player.getName()) + 8) * 2.75F;

        RenderUtil.drawRect(0, resolution.getScaledHeight() - 44, scaledWidth, 44, new Color(23, 23, 29).getRGB());

        // player info
        glScaled(2.75, 2.75, 2.75); {
            float scaledX = 7 * 0.36363636F;
            float scaledY = (resolution.getScaledHeight() - 35) * 0.36363636F;
            Managers.textManager.drawStringWithShadow(mc.player.getName(), scaledX, scaledY, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue()).getRGB());
        }

        glScaled(0.36363636F, 0.36363636F, 0.36363636F);

        glPopMatrix();*/

    }
}
