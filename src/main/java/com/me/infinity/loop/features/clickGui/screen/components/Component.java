package com.me.infinity.loop.features.clickGui.screen.components;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.Feature;
import com.me.infinity.loop.features.clickGui.InfinityLoopGui;
import com.me.infinity.loop.features.clickGui.screen.components.items.Item;
import com.me.infinity.loop.features.clickGui.screen.components.items.buttons.Button;
import com.me.infinity.loop.features.modules.client.ClickGui;
import com.me.infinity.loop.features.modules.client.Colors;
import com.me.infinity.loop.manager.TextManager;
import com.me.infinity.loop.util.renders.ColorUtil;
import com.me.infinity.loop.util.renders.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public abstract class Component
        extends Feature {
    public static int[] counter1 = new int[]{1};
    private final ArrayList<Item> items = new ArrayList();
    private int color;
    public boolean drag;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private boolean hidden = false;

    public Component(String name, int x, int y, boolean open) {
        super(name);
        this.x = x;
        this.y = y;
        this.width = 96;
        this.height = 18;
        this.open = open;
        this.setupItems();
    }

    public void setupItems() {
    }

    private void drag(int mouseX, int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drag(mouseX, mouseY);
        float totalItemHeight = this.open ? this.getTotalItemHeight() - 2.0f : 0.0f;
        int color = ColorUtil.toARGB(ClickGui.getInstance().topRed.getValue(), ClickGui.getInstance().topGreen.getValue(), ClickGui.getInstance().topBlue.getValue(), 255);
        Gui.drawRect(this.x, this.y - 2, this.x + this.width, this.y + this.height - 6, Colors.getInstance().rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()).getRGB() : color);
        Gui.drawRect(this.x, this.y + height - 5, this.x + this.width, this.y + this.height - 6, new Color(255, 255, 255, 255).getRGB());
        RenderUtil.drawRect(this.x, (float) this.y + 12.5f, this.x + this.width, (float) (this.y + this.height) + totalItemHeight, 0x77000000);
        InfinityLoop.textManager.drawString(this.getName(), (float) this.x + 3.0f, (float) this.y - 4.0f - (float) InfinityLoopGui.getClickGui().getTextOffset(), -1, true);
        if (this.open) {

            float y = (float) (this.getY() + this.getHeight()) - 3.0f;
            for (Item item : this.getItems()) {
                Component.counter1[0] = counter1[0] + 1;
                if (item.isHidden()) continue;
                item.setLocation((float) this.x + 2.0f, y);
                item.setWidth(this.getWidth() - 4);
                item.drawScreen(mouseX, mouseY, partialTicks);
                y += (float) item.getHeight() + 1.5f;
            }
        }
        //if (this.open) {

            // Gradient under module button
            /*if (ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
                RenderUtil.drawGradientRect(this.x + 0.5f, this.y + totalItemHeight, this.width, this.height, 0, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color);
                RenderUtil.drawGradientRect(this.x, this.y + height - 5,this.width,this.height,(Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color, 0);
            } else {
                RenderUtil.drawGradientRect(this.x + 0.5f, this.y + totalItemHeight, this.width, this.height, 0, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().hoverAlpha.getValue() / 2).getRGB());
                RenderUtil.drawGradientRect(this.x, this.y + height - 5,this.width, this.height - 6, new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().hoverAlpha.getValue() / 2).getRGB(), 0);
            }*/

            // old outline
            /*if (ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.shadeModel(7425);
                GL11.glBegin(1);
                Color currentColor = new Color((Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color);
                GL11.glColor4f(currentColor.getRed() / 255.0f, currentColor.getGreen() / 255.0f, currentColor.getBlue() / 255.0f, currentColor.getAlpha() / 255.0f);
                GL11.glVertex3f((float) (this.x + this.width), this.y - 1.5f, 0.0f);
                GL11.glVertex3f((float) this.x, this.y - 1.4f, 0.0f);
                GL11.glVertex3f((float) this.x, this.y - 1.4f, 0.0f);
                float currentHeight = this.getHeight() - 1.5f;
                for (final Item item : this.getItems()) {
                    currentColor = new Color((Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color);
                    GL11.glColor4f(currentColor.getRed() / 255.0f, currentColor.getGreen() / 255.0f, currentColor.getBlue() / 255.0f, currentColor.getAlpha() / 255.0f);
                    GL11.glVertex3f((float) this.x, this.y + currentHeight, 0.0f);
                    GL11.glVertex3f((float) this.x, this.y + currentHeight, 0.0f);
                }
                currentColor = new Color((Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color);
                GL11.glColor4f(currentColor.getRed() / 255.0f, currentColor.getGreen() / 255.0f, currentColor.getBlue() / 255.0f, currentColor.getAlpha() / 255.0f);
                GL11.glVertex3f(this.x + 0.5f, this.y + this.height + totalItemHeight, 0.0f);
                GL11.glVertex3f(this.x + 0.5f, this.y + this.height + totalItemHeight, 0.0f);
                for (final Item item : this.getItems()) {
                    currentColor = new Color((Colors.getInstance()).rainbow.getValue().booleanValue() ? (((Colors.getInstance()).rainbowModeA.getValue() == Colors.rainbowModeArray.Up) ? ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue(), ClickGui.getInstance().hoverAlpha.getValue()).getRGB()) : this.color);
                    GL11.glColor4f(currentColor.getRed() / 255.0f, currentColor.getGreen() / 255.0f, currentColor.getBlue() / 255.0f, currentColor.getAlpha() / 255.0f);
                    GL11.glVertex3f(this.x + 0.5f, this.y + currentHeight, 0.0f);
                    GL11.glVertex3f(this.x + 0.5f, this.y + currentHeight, 0.0f);
                }
                GL11.glVertex3f(this.x + 0.5f, (float) this.y, 0.0f);
                GL11.glEnd();
                GlStateManager.shadeModel(7424);
                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
            } else {
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.shadeModel(7425);
                GL11.glBegin(2);
                float alpha = (float) (color >> 24 & 0xFF) / 255.0f;
                float red = (float) (color >> 16 & 0xFF) / 255.0f;
                float green = (float) (color >> 8 & 0xFF) / 255.0f;
                float blue = (float) (color & 0xFF) / 255.0f;
                GL11.glColor4f(red, green, blue, alpha);
                GL11.glVertex3f((float) this.x, this.y - 1.4f, 0.0f);
                GL11.glVertex3f(this.x + 0.5f, this.y - 1.4f, 0.0f);
                GL11.glVertex3f(this.x + 0.5f, this.y + this.height + totalItemHeight, 0.0f);
                GL11.glVertex3f((float) this.x, this.y + this.height + totalItemHeight, 0.0f);


                GL11.glVertex3f((float) this.x + this.width, this.y + this.height + totalItemHeight, 0.0f);
                GL11.glVertex3f(this.x + this.width, this.y + this.height + totalItemHeight, 0.0f);
                GL11.glVertex3f(this.x + this.width + 0.5f, this.y , 0.0f);
                GL11.glVertex3f((float) this.x + this.width, this.y , 0.0f);

                GL11.glEnd();
                GlStateManager.shadeModel(7424);
                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
            }*/
        //}
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            InfinityLoopGui.getClickGui().getComponents().forEach(component -> {
                if (component.drag) {
                    component.drag = false;
                }
            });
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }

    public void addButton(Button button) {
        this.items.add(button);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isOpen() {
        return this.open;
    }

    public final ArrayList<Item> getItems() {
        return this.items;
    }

    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    public static boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
    }

    private boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }

    private float getTotalItemHeight() {
        float height = 0.0f;
        for (Item item : this.getItems()) {
            height += (float) item.getHeight() + 1.5f;
        }
        return height;
    }
}

