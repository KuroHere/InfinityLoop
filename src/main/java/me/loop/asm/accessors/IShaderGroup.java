package me.loop.asm.accessors;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;

import java.util.List;

public interface IShaderGroup {

    List<Framebuffer> getListFramebuffers();

    List<Shader> getListShaders();
}
