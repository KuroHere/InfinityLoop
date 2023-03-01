package com.me.infinity.loop.mixin.mixins;

import com.me.infinity.loop.util.utils.renders.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GuiInventory.class)
public class MixinGuiInventory extends MixinGuiScreen {

    private static float prevRotationYaw = 0.0F;
    private static float prevRotationPitch = 0.0F;
    private static float prevRenderYawOffset = 0.0F;

    private static FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    private static void drawString(String text, int x, int y, int color) {
        fr.drawString(text, x, y, color, true);
    }

    private static int getStringWidth(String text) {
        return fr.getStringWidth(text);
    }

    private static int getFontHeight() {
        return fr.FONT_HEIGHT;
    }

    @Inject(method={"initGui"}, at={@At(value="RETURN")})
    private void initGui(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(998, this.width - 150, 6, 25, 25, ChatFormatting.BOLD + "S"));
    }

    @Inject(method={"drawScreen"}, at={@At(value="RETURN")})
    private void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        drawString("Profile", this.width - 150, 30, -1);
    }

    @Inject(method = "drawEntityOnScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntity(Lnet/minecraft/entity/Entity;DDDFFZ)V", shift = At.Shift.BEFORE))
    private static void onDrawEntityOnScreenInvokeRenderEntityPre(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase entity, CallbackInfo ci) {
        prevRotationYaw = entity.prevRotationYaw;
        prevRotationPitch = entity.prevRotationPitch;
        prevRenderYawOffset = entity.prevRenderYawOffset;

        entity.prevRotationYaw = entity.rotationYaw;
        entity.prevRotationPitch = entity.rotationPitch;
        entity.prevRenderYawOffset = entity.renderYawOffset;
    }

    @ModifyArg(method = "drawEntityOnScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntity(Lnet/minecraft/entity/Entity;DDDFFZ)V"), index = 5)
    private static float onDrawEntityOnScreenInvokeRenderEntityPartialTicks(float partialTicks) {
        return RenderUtil.mc.getRenderPartialTicks();
    }

    @Inject(method = "drawEntityOnScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntity(Lnet/minecraft/entity/Entity;DDDFFZ)V", shift = At.Shift.AFTER))
    private static void onRenderEntityOnScreenPost(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase entity, CallbackInfo ci) {
        entity.prevRotationYaw = prevRotationYaw;
        entity.prevRotationPitch = prevRotationPitch;
        entity.prevRenderYawOffset = prevRenderYawOffset;
    }


    /*@Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiInventory;drawDefaultBackground()V"))
    public void drawDefaultBackground(GuiInventory guiInventory) {

    }*/
}

