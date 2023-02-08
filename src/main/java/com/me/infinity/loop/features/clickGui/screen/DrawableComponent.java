package com.me.infinity.loop.features.clickGui.screen;

import com.me.infinity.loop.util.interfaces.Util;
import net.minecraft.inventory.ClickType;

public abstract  class DrawableComponent implements Util {

    /**
     * Draws the feature
     */
    public abstract void drawComponent();

    /**
     * Runs when the feature is clicked
     * @param in The type of click
     */
    public abstract void onClick(ClickType in);

    /**
     * Runs when a key on the keyboard is typed
     * @param in The key that was typed
     */
    public abstract void onType(int in);

    /**
     * Runs when the mouse wheel is scrolled
     * @param in The scroll length
     */
    public abstract void onScroll(int in);
}