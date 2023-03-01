package com.me.infinity.loop.features.gui.components.items.buttons;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.modules.client.ClickGui.ClickGui;
import com.me.infinity.loop.features.gui.InfinityLoopGui;
import com.me.infinity.loop.features.gui.components.Component;
import com.me.infinity.loop.features.gui.components.items.Item;
import com.me.infinity.loop.util.utils.renders.Drawable;
import com.me.infinity.loop.util.utils.renders.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.awt.*;

public class Button
        extends Item {
    private boolean state;

    public Button(String name) {
        super(name);
        this.height = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width, this.y + (float) this.height - 1.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? InfinityLoop.colorManager.getColorWithAlpha(InfinityLoop.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : InfinityLoop.colorManager.getColorWithAlpha(InfinityLoop.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? new Color(0x273B3B3B, true).getRGB() : new Color(0x4D3B3B3B, true).getRGB()));
        InfinityLoop.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 2.0f - (float) InfinityLoopGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        Drawable.drawBlurredShadow((int) x, (int) (y + height), (int) width, 1, 9, new Color(0, 0, 0, 192));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.onMouseClick();
        }
    }

    public void onMouseClick() {
        this.state = !this.state;
        this.toggle();
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : InfinityLoopGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() && (float) mouseY >= this.getY() && (float) mouseY <= this.getY() + (float) this.height;
    }
}
