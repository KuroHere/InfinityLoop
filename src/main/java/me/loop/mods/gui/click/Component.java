package me.loop.mods.gui.click;

import me.loop.InfinityLoop;
import me.loop.api.utils.impl.maths.ImageUtil;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.mods.Mod;
import me.loop.mods.gui.InfinityLoopGui;
import me.loop.mods.gui.click.items.Item;
import me.loop.mods.gui.click.items.buttons.Button;
import me.loop.mods.modules.impl.client.ClickGui.ClickGui;
import me.loop.mods.modules.impl.client.Colors;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public abstract class Component
        extends Mod {
    public static int[] counter1 = new int[]{1};
    private final ArrayList<Item> items = new ArrayList();
    private final ResourceLocation arrow = new ResourceLocation("textures/arrow.png");
    private ResourceLocation client = new ResourceLocation("textures/client.png");
    private ResourceLocation combat = new ResourceLocation("textures/combat.png");
    private ResourceLocation misc = new ResourceLocation("textures/misc.png");
    private ResourceLocation movement = new ResourceLocation("textures/movement.png");
    private ResourceLocation player = new ResourceLocation("textures/player.png");
    private ResourceLocation render = new ResourceLocation("textures/render.png");
    public boolean drag;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private int angle;
    private boolean open;
    private boolean visible;

    public Component(String name, int x, int y, boolean open) {
        super(name);
        this.x = x;
        this.y = y;
        this.width = 96;
        this.height = 18;
        this.angle = 180;
        this.open = open;
        this.setupItems();
    }

    public Component() {
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
        counter1 = new int[]{1};
        float totalItemHeight = this.open ? this.getTotalItemHeight() - 2.0f : 0.0f;

        boolean future = ClickGui.INSTANCE.moduleiconmode.getValue();

        if (ClickGui.INSTANCE.isOn()); {
            RenderUtil.drawGradientSideways(this.x, (float)this.y - 1.5f, this.x + this.width, this.y + this.height - 6, new Color(0,0,0,255).getRGB(), new Color(ClickGui.INSTANCE.moduleMainC.getValue().getRed(), ClickGui.INSTANCE.moduleMainC.getValue().getGreen(), ClickGui.INSTANCE.moduleMainC.getValue().getBlue(), 255).getRGB());
            RenderUtil.drawBorderedRect(this.x + 0.4, (float) this.y + 12.5f + 0.1 - 13, this.x + this.width + 0.1, (float) (this.y + this.height) + totalItemHeight + 0.5, ClickGui.INSTANCE.getOutlineWidth() - ClickGui.INSTANCE.getOutlineWidth() * 2, new Color(0, 0, 0, 0).getRGB(), new Color(ClickGui.INSTANCE.moduleMainC.getValue().getRed(), ClickGui.INSTANCE.moduleMainC.getValue().getGreen(), ClickGui.INSTANCE.moduleMainC.getValue().getBlue(), ClickGui.INSTANCE.moduleMainC.getValue().getAlpha()).getRGB());
        }
        if (!this.open)  {
            RenderUtil.drawBorderedRect(this.x, (float)this.y - 1.5f, this.x + this.width, this.y + this.height - 6, ClickGui.INSTANCE.getOutlineWidth() - ClickGui.INSTANCE.getOutlineWidth()*2, new Color(0,0,0,0).getRGB(), new Color(ClickGui.INSTANCE.moduleMainC.getValue().getRed(), ClickGui.INSTANCE.moduleMainC.getValue().getGreen(), ClickGui.INSTANCE.moduleMainC.getValue().getBlue(), ClickGui.INSTANCE.moduleMainC.getValue().getAlpha()).getRGB());
        }

        Gui.drawRect(this.x, this.y - 2, this.x + this.width, this.y + this.height - 6, new Color(0xCD232323, true).getRGB());
        RenderUtil.drawOutlineRect(this.x, this.y - 2, this.x + this.width, this.y + this.height - 6, (Colors.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue())) : new Color(ClickGui.INSTANCE.moduleMainC.getValue().getRed(), ClickGui.INSTANCE.moduleMainC.getValue().getGreen(), ClickGui.INSTANCE.moduleMainC.getValue().getBlue(), 240), 0.2f);
        Gui.drawRect(this.x, this.y + height - 5, this.x + this.width, this.y + this.height - 6, new Color(255, 255, 255, 176).getRGB());
        RenderUtil.drawRect(this.x, (float) this.y + 12.5f, this.x + this.width, (float) (this.y + this.height) + totalItemHeight + 5, 0x77000000);

        if (ClickGui.INSTANCE.shader.getValue().booleanValue()) {
            RenderUtil.drawColorShader(this.x, (int)((double)this.y - 1.5), this.x + this.width, this.y + this.height + (int)totalItemHeight, ColorUtil.toRGBA(new Color(ClickGui.INSTANCE.shaderC.getValue().getRed(), ClickGui.INSTANCE.shaderC.getValue().getGreen(), ClickGui.INSTANCE.shaderC.getValue().getBlue(), ClickGui.INSTANCE.shaderC.getValue().getAlpha())), ClickGui.INSTANCE.shaderRadius.getValue());
        }
        if (ClickGui.INSTANCE.categoryTextCenter.getValue().booleanValue()) {
            InfinityLoop.textManager.drawStringWithShadow(this.getName(), (float)this.x + (float)(this.width / 2) - (float)(this.renderer.getStringWidth(this.getName()) / 2), (float)this.y - 4.0f - (float)InfinityLoopGui.INSTANCE.getTextOffset(), -1);
        } else {
            InfinityLoop.textManager.drawStringWithShadow(this.getName(), (float)this.x + 3.0f, (float)this.y - 4.0f - (float)InfinityLoopGui.INSTANCE.getTextOffset(), -1);
        }

        if (future) {
            if (!open) {
                if (angle > 0) {
                    angle -= 6;
                }
            } else if (angle < 180) {
                angle += 6;
            }
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            ImageUtil.glColor(new Color(255, 255, 255, 255));
            mc.getTextureManager().bindTexture(this.arrow);
            GlStateManager.translate((float)(this.getX() + this.getWidth() - 7), (float)(this.getY() + 6) - 0.3F, 0.0F);
            GlStateManager.rotate(ImageUtil.calculateRotation((float)this.angle), 0.0F, 0.0F, 1.0F);
            ImageUtil.drawModalRect(-5, -5, 0.0F, 0.0F, 10, 10, 10, 10, 10.0F, 10.0F);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        /* // MISC
        Wrapper.getMinecraft().renderEngine.bindTexture(misc);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture( (float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);
        // PLAYER
        Wrapper.getMinecraft().renderEngine.bindTexture(player);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture((float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);
        // COMBAT
        Wrapper.getMinecraft().renderEngine.bindTexture(combat);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture( (float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);
        // RENDER
        Wrapper.getMinecraft().renderEngine.bindTexture(render);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture((float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);
        // MOVEMENT
        Wrapper.getMinecraft().renderEngine.bindTexture(movement);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture((float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);
        // CLIENT
        Wrapper.getMinecraft().renderEngine.bindTexture(client);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture((float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);*/

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
            GL11.glPopMatrix();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            InfinityLoopGui.INSTANCE.getComponents().forEach(component -> {
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public final ArrayList<Item> getItems() {
        return this.items;
    }

    private boolean isHovering(int mouseX, int mouseY) {
        if (mouseX < this.getX()) return false;
        if (mouseX > this.getX() + this.getWidth()) return false;
        if (mouseY < this.getY()) return false;
        if (mouseY > this.getY() + this.getHeight() - (this.open ? 2 : 0)) return false;
        return true;
    }

    private float getTotalItemHeight() {
        float height = 0.0f;
        for (Item item : this.getItems()) {
            height += (float) item.getHeight() + 1.5f;
        }
        return height;
    }

}

