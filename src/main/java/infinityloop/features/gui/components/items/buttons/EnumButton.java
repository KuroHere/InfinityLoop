package infinityloop.features.gui.components.items.buttons;

import infinityloop.InfinityLoop;
import infinityloop.features.modules.client.ClickGui.ClickGui;
import infinityloop.features.setting.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import infinityloop.features.gui.InfinityLoopGui;
import infinityloop.util.utils.renders.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class EnumButton
        extends Button {
    public Setting setting;

    public EnumButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? InfinityLoop.colorManager.getColorWithAlpha(InfinityLoop.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : InfinityLoop.colorManager.getColorWithAlpha(InfinityLoop.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        InfinityLoop.textManager.drawStringWithShadow(this.setting.getName() + " " + ChatFormatting.GRAY + (this.setting.currentEnumName().equalsIgnoreCase("ABC") ? "ABC" : this.setting.currentEnumName()), this.x + 2.3f, this.y - 1.7f - (float) InfinityLoopGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        //RenderUtil.drawRect(this.x, this.y, this.x - 2.0f, this.y + (float) this.height - 0.5f, -1);
     }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
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

