package com.me.infinity.loop.features.gui.components.items.buttons;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.modules.client.ClickGui.ClickGui;
import com.me.infinity.loop.features.setting.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.me.infinity.loop.features.gui.InfinityLoopGui;
import com.me.infinity.loop.features.gui.components.Component;
import com.me.infinity.loop.util.utils.renders.RenderUtil;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class Slider
        extends Button {
    private final Number min;
    private final Number max;
    private final int difference;
    public Setting setting;

    public Slider(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.min = (Number) setting.getMin();
        this.max = (Number) setting.getMax();
        this.difference = this.max.intValue() - this.min.intValue();
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.dragSetting(mouseX, mouseY);
        RenderUtil.drawRect(this.x, this.y + 5.0f, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 1.5f, !this.isHovering(mouseX, mouseY) ? new Color(0xC0282828, true).getRGB() : new Color(0xB51E1E1E, true).getRGB());
        RenderUtil.drawRect(this.x, this.y + 5.0f, ((Number) this.setting.getValue()).floatValue() <= this.min.floatValue() ? this.x : this.x + ((float) this.width + 7.4f) * this.partialMultiplier(), this.y + (float) this.height - 1.5f, !this.isHovering(mouseX, mouseY) ? InfinityLoop.colorManager.getColorWithAlpha(InfinityLoop.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : InfinityLoop.colorManager.getColorWithAlpha(InfinityLoop.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue()));
        InfinityLoop.textManager.drawStringWithShadow(this.getName() + " " + ChatFormatting.GRAY + (this.setting.getValue() instanceof Float ? this.setting.getValue() : Double.valueOf(((Number) this.setting.getValue()).doubleValue())), (x + 4), (float) (y + 4), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            this.setSettingFromX(mouseX);
        }
    }

    @Override
    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : InfinityLoopGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() + 8.0f && (float) mouseY >= this.getY() && (float) mouseY <= this.getY() + (float) this.height;
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void dragSetting(int mouseX, int mouseY) {
        if (this.isHovering(mouseX, mouseY) && Mouse.isButtonDown(0)) {
            this.setSettingFromX(mouseX);
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    private void setSettingFromX(int mouseX) {
        float percent = ((float) mouseX - this.x) / ((float) this.width + 7.4f);
        if (this.setting.getValue() instanceof Double) {
            double result = (Double) this.setting.getMin() + (double) ((float) this.difference * percent);
            this.setting.setValue((double) Math.round(10.0 * result) / 10.0);
        } else if (this.setting.getValue() instanceof Float) {
            float result = ((Float) this.setting.getMin()).floatValue() + (float) this.difference * percent;
            this.setting.setValue(Float.valueOf((float) Math.round(10.0f * result) / 10.0f));
        } else if (this.setting.getValue() instanceof Integer) {
            this.setting.setValue((Integer) this.setting.getMin() + (int) ((float) this.difference * percent));
        }
    }

    private float middle() {
        return this.max.floatValue() - this.min.floatValue();
    }

    private float part() {
        return ((Number) this.setting.getValue()).floatValue() - this.min.floatValue();
    }

    private float partialMultiplier() {
        return this.part() / this.middle();
    }
}
