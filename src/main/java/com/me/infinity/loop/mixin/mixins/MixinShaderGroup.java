package com.me.infinity.loop.mixin.mixins;

import com.me.infinity.loop.util.utils.interfaces.IShaderGroup;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin( {ShaderGroup.class} )
public abstract class MixinShaderGroup implements IShaderGroup {

    @Accessor(value = "listFramebuffers")

    public abstract List<Framebuffer> getListFramebuffers();

    @Accessor(value = "listShaders")
    public abstract List<Shader> getListShaders();
}
