package com.me.infinity.loop.features.gui.mainmenu;

import com.me.infinity.loop.Loop;
import com.me.infinity.loop.features.gui.font.FontRender;
import com.me.infinity.loop.features.modules.client.MainSettings;
import com.me.infinity.loop.util.renders.helper.RoundedShader;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.IOException;


public class InfinityLoopMenu extends GuiScreen
{
    private MainMenuShader backgroundShader;

    public InfinityLoopMenu() {
        try {
            if(Loop.moduleManager != null){
                switch (Loop.moduleManager.getModuleByClass(MainSettings.class).shaderMode.getValue()){
                    case Smoke2:
                        backgroundShader = new MainMenuShader("/shaders/moon.fsh");
                        break;
                    case Smoke:
                        backgroundShader = new MainMenuShader("/shaders/mainmenu.fsh");
                        break;
                    case Dicks:
                        backgroundShader = new MainMenuShader("/shaders/dicks.fsh");
                        break;
                }
            }
        } catch (IOException var9) {
            throw new IllegalStateException("Failed to load backgound shader", var9);
        }
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.width = sr.getScaledWidth();
        this.height = sr.getScaledHeight();

        this.buttonList.add(new GuiMainMenuButton(1,sr.getScaledWidth() / 2 - 110, sr.getScaledHeight() / 2 - 70,false,"SINGLEPLAYER", false));
        this.buttonList.add(new GuiMainMenuButton(2,sr.getScaledWidth() / 2 + 4, sr.getScaledHeight() / 2 - 70,false,"MULTIPLAYER", false));
        this.buttonList.add(new GuiMainMenuButton(0,sr.getScaledWidth() / 2 - 110, sr.getScaledHeight() / 2 - 29,false,"SETTINGS", false));
        this.buttonList.add(new GuiMainMenuButton(14,sr.getScaledWidth() / 2 + 4, sr.getScaledHeight() / 2 - 29,false,"ALTMANAGER", false));
        this.buttonList.add(new GuiMainMenuButton(666,sr.getScaledWidth() / 2 - 110, sr.getScaledHeight() / 2 + 12,true,"EXIT", false));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        GlStateManager.disableCull();
        this.backgroundShader.useShader((int) (sr.getScaledWidth() * 2.0f), (int) (sr.getScaledHeight() * 2.0f), (float)mouseX, (float)mouseY, (float)(System.currentTimeMillis() - Loop.initTime) / 1000.0F);
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0F, -1F);
        GL11.glVertex2f(-1.0F, 1.0F);
        GL11.glVertex2f(1.0F, 1.0F);
        GL11.glVertex2f(1.0F, -1.0F);
        GL11.glEnd();
        GL20.glUseProgram(0);
        GlStateManager.disableCull();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Color color = new Color(0x86000000, true);

        float half_width = sr.getScaledWidth() / 2f;
        float half_height = sr.getScaledHeight() / 2f;

        RoundedShader.drawGradientRound(half_width - 120, half_height - 80, 240,  140, 15f, color,color,color,color);
        FontRender.drawCentString8("1NF1N1TY LOOP" ,(int) half_width - 52, (int) half_height - 82 - FontRender.getFontHeight8(),-1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public static float func(float var0) {
        if ((var0 %= 360.0F) >= 180.0F) {
            var0 -= 360.0F;
        }

        if (var0 < -180.0F) {
            var0 += 360.0F;
        }

        return var0;
    }

    @Override
    protected void actionPerformed(final GuiButton button){
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 14) {
            this.mc.displayGuiScreen( new GuiAltManager());
        }
        if (button.id == 666) {
            this.mc.shutdown();
        }
    }

}
