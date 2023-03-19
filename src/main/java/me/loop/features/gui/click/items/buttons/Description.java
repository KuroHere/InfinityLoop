package me.loop.features.gui.click.items.buttons;

import me.loop.InfinityLoop;
import me.loop.features.gui.click.items.Item;
import me.loop.util.impl.RenderUtil;

public class Description extends Item {
    private String description;
    private boolean draw;

    public Description(String description, float x, float y) {
        super("Description");
        this.description = description;
        this.setLocation(x, y);
        this.width = InfinityLoop.textManager.getStringWidth(this.description) + 4;
        this.height = InfinityLoop.textManager.getFontHeight() + 4;
        this.draw = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.width = InfinityLoop.textManager.getStringWidth(this.description) + 4;
        this.height = InfinityLoop.textManager.getFontHeight() + 4;
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height, -704643072);
        InfinityLoop.textManager.drawString(this.description, this.x + 2.0f, this.y + 2.0f, 0xFFFFFF, true);
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
