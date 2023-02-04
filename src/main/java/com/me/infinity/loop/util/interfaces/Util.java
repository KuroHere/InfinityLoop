package com.me.infinity.loop.util.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;

import java.util.Objects;

public interface Util {
    Minecraft mc = Objects.requireNonNull(Minecraft.getMinecraft());
    FontRenderer fr = mc.fontRenderer;
    RenderManager rendermgr = mc.getRenderManager();
}


