package com.me.infinity.loop.mixin.mixins;

import com.me.infinity.loop.features.modules.client.GameChanger;
import com.me.infinity.loop.features.modules.player.AutoDoSmth;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {
    @Shadow
    public ResourceLocation getEntityTexture(AbstractClientPlayer abstractClientPlayer) {
        return null;
    }

    @Inject(method = "preRenderCallback*", at = @At("HEAD"))
    public void renderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime, CallbackInfo ci) {
        if (GameChanger.getInstance().modelFlip.getValue()) {
            GlStateManager.rotate(0, 1, 0, 0);
        } else if (!GameChanger.getInstance().modelFlip.getValue())
            GlStateManager.rotate(180, 0, 0, 0);

        if (AutoDoSmth.getINSTANCE().clientside.getValue()) {
            float f = 0.9357f;
            float hue = (float) (System.currentTimeMillis() % 22600L) / 5.0f;

            GlStateManager.scale(f, f, f);

            GlStateManager.rotate(hue, 1, 0, hue);
        } else if (!AutoDoSmth.getINSTANCE().clientside.getValue())
            GlStateManager.rotate(180, 1, 0, 0);
    }
}

