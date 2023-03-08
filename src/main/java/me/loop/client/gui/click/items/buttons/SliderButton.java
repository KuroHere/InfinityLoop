package me.loop.client.gui.click.items.buttons;

import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.maths.MathUtil;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.client.gui.InfinityLoopGui;
import me.loop.client.gui.click.Component;
import me.loop.client.modules.impl.client.ClickGui.ClickEnum;
import me.loop.client.modules.impl.client.ClickGui.ClickGui;
import me.loop.client.modules.impl.client.Colors;
import me.loop.client.modules.impl.client.HUD;
import me.loop.client.modules.settings.Setting;
import org.lwjgl.input.Mouse;

public class SliderButton
        extends Button {
    private final Number min;
    private final Number max;
    private final int difference;
    public Setting setting;

    public SliderButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.min = (Number)setting.getMin();
        this.max = (Number)setting.getMax();
        this.difference = this.max.intValue() - this.min.intValue();
        this.width = 40;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.dragSetting(mouseX, mouseY);
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, !this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515);
        if (ClickGui.getInstance().sideSettings.getValue().booleanValue()) {
            int sideColor = ColorUtil.toRGBA(ClickGui.getInstance().sideLineC.getValue().getRed(), ClickGui.getInstance().sideLineC.getValue().getGreen(), ClickGui.getInstance().sideLineC.getValue().getBlue(), ClickGui.getInstance().sideLineC.getValue().getAlpha());
            RenderUtil.drawRect(this.x, this.y, this.x + 1.0f, this.y + (float) this.height + 1.0f, sideColor);
        }
        if (Colors.getInstance().rainbow.getValue().booleanValue()) {
            int color = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y, 0, this.renderer.scaledHeight)), Managers.moduleManager.getModuleByClass(ClickGui.class).moduleEnableC.getValue().getAlpha());
            int color1 = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y + this.height, 0, this.renderer.scaledHeight)), Managers.moduleManager.getModuleByClass(ClickGui.class).moduleEnableC.getValue().getAlpha());
            RenderUtil.drawGradientRect(this.x, this.y, ((Number) this.setting.getValue()).floatValue() <= this.min.floatValue() ? 0.0f : ((float) this.width + 7.4f) * this.partialMultiplier(), (float) this.height - 0.5f, !this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y, 0, this.renderer.scaledHeight)) : color, !this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y, 0, this.renderer.scaledHeight)) : color1);
        } else {
            if (ClickGui.getInstance().sliderType.getValue() == ClickEnum.SliderType.Line) {
                int sliderColor = ColorUtil.toRGBA(ClickGui.getInstance().sliderC.getValue().getRed(), ClickGui.getInstance().sliderC.getValue().getGreen(), ClickGui.getInstance().sliderC.getValue().getBlue(), ClickGui.getInstance().sliderC.getValue().getAlpha());
                //todo
                // int sliderColorHovering = ColorUtil.toRGBA(ClickGui.getInstance().sliderRed.getValue(), ClickGui.getInstance().sliderGreen.getValue(), ClickGui.getInstance().sliderBlue.getValue(), ClickGui.getInstance().moduleMainC.getValue().getAlpha());
                RenderUtil.drawRect(this.x, this.y + (float) this.height - 2.0f, ((Number) this.setting.getValue()).floatValue() <= this.min.floatValue() ? this.x : this.x + ((float) this.width + 7.4f) * this.partialMultiplier(), this.y + (float) this.height - 0.5f, sliderColor);
            } else if (ClickGui.getInstance().sliderType.getValue() == ClickEnum.SliderType.Fill) {
                int sliderColor = ColorUtil.toRGBA(ClickGui.getInstance().sliderC.getValue().getRed(), ClickGui.getInstance().sliderC.getValue().getGreen(), ClickGui.getInstance().sliderC.getValue().getBlue(), ClickGui.getInstance().sliderC.getValue().getAlpha());
                //todo
                //  int sliderColorHovering = ColorUtil.toRGBA(ClickGui.getInstance().sliderRed.getValue(), ClickGui.getInstance().sliderGreen.getValue(), ClickGui.getInstance().sliderBlue.getValue(), ClickGui.getInstance().moduleMainC.getValue().getAlpha());
                RenderUtil.drawRect(this.x, this.y, ((Number) this.setting.getValue()).floatValue() <= this.min.floatValue() ? this.x : this.x + ((float) this.width + 7.4f) * this.partialMultiplier(), this.y + (float) this.height - 0.5f, sliderColor);
            }
        }
        Managers.textManager.drawStringWithShadow(this.getName() + " \u00a77" + (this.setting.getValue() instanceof Float ? (Number)((Number)this.setting.getValue()) : (Number)((Number)this.setting.getValue()).doubleValue()), this.x + 2.3f, this.y - 1.7f - (float)InfinityLoopGui.getClickGui().getTextOffset(), -1);
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
        return (float)mouseX >= this.getX() && (float)mouseX <= this.getX() + (float)this.getWidth() + 8.0f && (float)mouseY >= this.getY() && (float)mouseY <= this.getY() + (float)this.height;
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void dragSetting(int mouseX, int mouseY) {
        if (this.isHovering(mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
            this.setSettingFromX(mouseX);
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    private void setSettingFromX(int mouseX) {
        float percent = ((float)mouseX - this.x) / ((float)this.width + 7.4f);
        if (this.setting.getValue() instanceof Double) {
            double result = (Double)this.setting.getMin() + (double)((float)this.difference * percent);
            this.setting.setValue((double)Math.round(10.0 * result) / 10.0);
        } else if (this.setting.getValue() instanceof Float) {
            float result = ((Float)this.setting.getMin()).floatValue() + (float)this.difference * percent;
            this.setting.setValue(Float.valueOf((float)Math.round(10.0f * result) / 10.0f));
        } else if (this.setting.getValue() instanceof Integer) {
            this.setting.setValue((Integer)this.setting.getMin() + (int)((float)this.difference * percent));
        }
    }

    private float middle() {
        return this.max.floatValue() - this.min.floatValue();
    }

    private float part() {
        return ((Number)this.setting.getValue()).floatValue() - this.min.floatValue();
    }

    private float partialMultiplier() {
        return this.part() / this.middle();
    }
}

