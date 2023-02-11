package com.me.infinity.loop.features.clickGui.screen.components.items.buttons.elements;

import com.me.infinity.loop.features.clickGui.font.FontRender;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.features.setting.SubBind;
import com.mojang.realmsclient.gui.ChatFormatting;

public class SubBindElement extends AbstractElement {
    public SubBindElement(Setting setting) {
        super(setting);
    }

    public boolean isListening;




    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
        if (this.isListening) {
            FontRender.drawString5("...", (float) (x + 3), (float) (y + height / 2 - (FontRender.getFontHeight5() / 2f)), -1);
        } else {
            FontRender.drawString5("SubBind " + ChatFormatting.GRAY + this.setting.getValue().toString().toUpperCase(), (float) (x + 3), (float) (y + height / 2 - (FontRender.getFontHeight5() / 2f)), -1);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovered && button == 0) {
            isListening = !isListening;
        }
    }

    @Override
    public void keyTyped(char chr, int keyCode) {
        if (this.isListening) {
            SubBind subBindbind = new SubBind(keyCode);
            if (subBindbind.toString().equalsIgnoreCase("Escape")) {
                return;
            }
            if (subBindbind.toString().equalsIgnoreCase("Delete")) {
                subBindbind = new SubBind(-1);
            }
            this.setting.setValue(subBindbind);
            isListening = !isListening;
        }
    }
}
