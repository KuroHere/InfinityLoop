package me.loop.feature.modules.impl.client;

import me.loop.api.utils.impl.Util;
import me.loop.feature.modules.Category;
import me.loop.feature.modules.Module;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;

public class BlurExtends extends Module implements Util {

    public BlurExtends() {
        super("BlurExtends", "Sussy", Category.CLIENT, true, false, false);
    }

    public void onDisable() {
        if (BlurExtends.mc.world != null) {
            BlurExtends.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }

    }

    public void onUpdate() {
        if (BlurExtends.mc.world != null) {
            if (!(BlurExtends.mc.currentScreen instanceof GuiContainer) && !(BlurExtends.mc.currentScreen instanceof GuiChat) && !(BlurExtends.mc.currentScreen instanceof GuiConfirmOpenLink) && !(BlurExtends.mc.currentScreen instanceof GuiEditSign) && !(BlurExtends.mc.currentScreen instanceof GuiGameOver) && !(BlurExtends.mc.currentScreen instanceof GuiOptions) && !(BlurExtends.mc.currentScreen instanceof GuiIngameMenu) && !(BlurExtends.mc.currentScreen instanceof GuiVideoSettings) && !(BlurExtends.mc.currentScreen instanceof GuiScreenOptionsSounds) && !(BlurExtends.mc.currentScreen instanceof GuiControls) && !(BlurExtends.mc.currentScreen instanceof GuiCustomizeSkin) && !(BlurExtends.mc.currentScreen instanceof GuiModList)) {
                if (BlurExtends.mc.entityRenderer.getShaderGroup() != null) {
                    BlurExtends.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            } else if (OpenGlHelper.shadersSupported && BlurExtends.mc.getRenderViewEntity() instanceof EntityPlayer) {
                if (BlurExtends.mc.entityRenderer.getShaderGroup() != null) {
                    BlurExtends.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }

                try {
                    BlurExtends.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else if (BlurExtends.mc.entityRenderer.getShaderGroup() != null && BlurExtends.mc.currentScreen == null) {
                BlurExtends.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }

    }
}
