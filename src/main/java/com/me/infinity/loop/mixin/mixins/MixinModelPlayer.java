package com.me.infinity.loop.mixin.mixins;

import com.me.infinity.loop.features.modules.render.Animation;
import com.me.infinity.loop.features.modules.render.Skeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ModelPlayer.class})
public class MixinModelPlayer {
    @Inject(method = {"setRotationAngles"}, at = {@At("RETURN")})
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo callbackInfo) {
        if ((Minecraft.getMinecraft()).world != null && (Minecraft.getMinecraft()).player != null && entityIn instanceof EntityPlayer)
            Skeleton.addEntity((EntityPlayer) entityIn, ModelPlayer.class.cast(this));
    }
}

