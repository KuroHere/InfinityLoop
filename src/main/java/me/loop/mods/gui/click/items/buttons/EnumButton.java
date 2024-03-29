package me.loop.mods.gui.click.items.buttons;

import me.loop.InfinityLoop;
import me.loop.api.utils.impl.maths.MathUtil;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.mods.gui.InfinityLoopGui;
import me.loop.mods.modules.impl.client.ClickGui.ClickGui;
import me.loop.mods.modules.impl.client.Colors;
import me.loop.mods.modules.impl.client.HUD;
import me.loop.mods.settings.Setting;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class EnumButton
        extends Button {
    public Setting setting;

    public EnumButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 40;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (ClickGui.INSTANCE.sideSettings.getValue().booleanValue()) {
            int sideColor = ColorUtil.toRGBA(ClickGui.INSTANCE.sideLineC.getValue().getRed(), ClickGui.INSTANCE.sideLineC.getValue().getGreen(), ClickGui.INSTANCE.sideLineC.getValue().getBlue(), ClickGui.INSTANCE.sideLineC.getValue().getAlpha());
            RenderUtil.drawRect(this.x, this.y, this.x + 1.0f, this.y + (float)this.height + 1.0f, sideColor);
        }
        if (Colors.getInstance().rainbow.getValue() && ClickGui.INSTANCE.colorSync.getValue().booleanValue()) {
            int color = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)), InfinityLoop.moduleManager.getModuleByClass(ClickGui.class).moduleMainC.getValue().getAlpha());
            int color1 = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)), InfinityLoop.moduleManager.getModuleByClass(ClickGui.class).moduleMainC.getValue().getAlpha());
            RenderUtil.drawGradientRect(this.x, this.y, (float)this.width + 7.4f, (float)this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)) : color) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515), this.getState() ? (!this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)) : color1) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        } else {
            RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? InfinityLoop.colorManager.getColorWithAlpha(InfinityLoop.moduleManager.getModuleByClass(ClickGui.class).moduleMainC.getValue().getAlpha()) : InfinityLoop.colorManager.getColorWithAlpha(InfinityLoop.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        }
        InfinityLoop.textManager.drawStringWithShadow(this.setting.getName() + " \u00a77" + this.setting.currentEnumName(), this.x + 2.3f, this.y - 1.7f - (float) InfinityLoopGui.INSTANCE.getTextOffset(), this.getState() ? -1 : -5592406);
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.setting.increaseEnum();
    }

    @Override
    public boolean getState() {
        return true;
    }
}