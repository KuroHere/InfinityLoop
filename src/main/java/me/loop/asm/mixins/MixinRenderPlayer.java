package me.loop.asm.mixins;

import me.loop.mods.modules.impl.client.GameChanger;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {

    @Inject(method = "preRenderCallback*", at = @At("HEAD"))
    public void renderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime, CallbackInfo ci) {
        if (GameChanger.getInstance().modelFlip.getValue()) {
            GlStateManager.rotate(180, 0, 180, 0);
        }
    }
}

