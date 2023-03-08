package me.loop.client.csgui.components;

import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.client.Client;
import me.loop.client.csgui.CSClickGui;
import me.loop.client.gui.click.items.Item;
import me.loop.client.gui.click.items.buttons.Button;
import me.loop.client.modules.impl.client.CSGui;
import me.loop.client.modules.impl.client.Colors;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;

import java.awt.*;
import java.util.ArrayList;

public class CSComponent 
        extends Client {
        public static int[] counter1 = new int[]{1};
        private final ArrayList<Item> items = new ArrayList();
        public boolean drag;
        private int x;
        private int y;
        private int x2;
        private int y2;
        private int width;
        private int height;
        private boolean open;
        private boolean hidden = false;


    public CSComponent(String name, int x, int y, boolean open) {
            super(name);
            this.x = x;
            this.y = y;
            this.width = 88;
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
            int color = ColorUtil.toARGB(CSGui.getInstance().topRed.getValue(), CSGui.getInstance().topGreen.getValue(), CSGui.getInstance().topBlue.getValue(), 255);
            Gui.drawRect(this.x, this.y + height + 2, this.x + this.width, this.y + this.height - 6, new Color(0xFFFFFF).getRGB());
            Gui.drawRect(this.x, this.y - 1, this.x + this.width, this.y + this.height - 7, Colors.getInstance().rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()).getRGB() : color);
            Gui.drawRect(this.x, this.y + height - 5, this.x + this.width, this.y + this.height - 6, new Color(0xFFFFFF).getRGB());
            Managers.textManager.drawStringWithShadow(this.getName(), (float) this.x + 3.0f, (float) this.y - 4.0f - (float) CSClickGui.getCSGui().getTextOffset(), -1);
            if (this.open) {
                RenderUtil.drawRect(this.x, (float) this.y + 12.5f, this.x + this.width, (float) (this.y + this.height) + totalItemHeight, 0x77000000);
                float y = (float) (this.getY() + this.getHeight()) - 3.0f;
                for (Item item : this.getItems()) {
                    CSComponent.counter1[0] = counter1[0] + 1;
                    if (item.isHidden()) continue;
                    item.setLocation((float) this.x + 2.0f, y);
                    item.setWidth(this.getWidth() - 4);
                    item.drawScreen(mouseX, mouseY, partialTicks);
                    y += (float) item.getHeight() + 1.5f;
                }
            }
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
                this.x2 = this.x - mouseX;
                this.y2 = this.y - mouseY;
                CSClickGui.getCSGui().getComponents().forEach(component -> {
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
