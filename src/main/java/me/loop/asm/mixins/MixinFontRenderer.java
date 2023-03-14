package me.loop.asm.mixins;

import me.loop.api.managers.Managers;
import me.loop.mods.modules.impl.client.FontMod;
import me.loop.mods.modules.impl.client.HUD;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {FontRenderer.class})
public abstract class MixinFontRenderer {
    @Shadow
    protected abstract int renderString(String var1, float var2, float var3, int var4, boolean var5);

    @Inject(method = {"drawString(Ljava/lang/String;FFIZ)I"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderStringHook(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> info) {

        if (FontMod.INSTANCE == null) {
            FontMod.INSTANCE = new FontMod();
        }

        FontMod fontMod = FontMod.INSTANCE;

        if (fontMod.isOn() && fontMod.full.getValue() && Managers.textManager != null) {
            float result = Managers.textManager.drawString(text, x, y, color, dropShadow);
            info.setReturnValue((int) result);
        }
    }

    @Redirect(method = {"drawString(Ljava/lang/String;FFIZ)I"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I"))
    public int renderStringHook(FontRenderer fontrenderer, String text, float x, float y, int color, boolean dropShadow) {

        if (Managers.moduleManager != null && HUD.getInstance().shadow.getValue() && dropShadow) {
            return this.renderString(text, x - 0.5f, y - 0.5f, color, true);
        }
        return this.renderString(text, x, y, color, dropShadow);
    }
}