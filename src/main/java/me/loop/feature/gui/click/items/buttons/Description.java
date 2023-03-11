package me.loop.feature.gui.click.items.buttons;

import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.feature.gui.click.items.Item;

public class Description extends Item {
    private String description;
    private boolean draw;

    public Description(String description, float x, float y) {
        super("Description");
        this.description = description;
        this.setLocation(x, y);
        this.width = Managers.textManager.getStringWidth(this.description) + 4;
        this.height = Managers.textManager.getFontHeight() + 4;
        this.draw = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.width = Managers.textManager.getStringWidth(this.description) + 4;
        this.height = Managers.textManager.getFontHeight() + 4;
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height, -704643072);
        Managers.textManager.drawString(this.description, this.x + 2.0f, this.y + 2.0f, 0xFFFFFF, true);
    }

    public boolean shouldDraw() {
        return this.draw;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}
