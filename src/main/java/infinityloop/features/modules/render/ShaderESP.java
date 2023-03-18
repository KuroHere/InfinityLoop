package infinityloop.features.modules.render;

import infinityloop.event.events.render.Render2DEvent;
import infinityloop.event.events.render.Render3DEvent;
import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.setting.Setting;
import infinityloop.util.utils.maths.MathUtil;
import infinityloop.util.utils.openGL.ShaderUtility;
import infinityloop.util.utils.renders.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class ShaderESP extends Module {

    public static Framebuffer framebuffer;

    public static Framebuffer outlineFrameBuffer;

    private final Setting<Integer> radius = this.register(new Setting("Radius", 10.0F, 3.0F, 50.0F));

    private final List<Entity> entities = new ArrayList<>();

    public static boolean renderNameTags = true;

    private final Setting<Integer> alpha = this.register(new Setting("Alpha", 2.0F, 1.0F, 3.0F));

    private final Setting<ListSetting> colorMode = this.register(new Setting("Color Mode", ListSetting.Client));

    private final ShaderUtility glowShader = new ShaderUtility("infinityloop/shaders/glow.frag");

    private final Frustum frustum = new Frustum();

    private final ShaderUtility outlineShader = new ShaderUtility("infinityloop/shaders/outline.frag");

    public static Framebuffer glowFrameBuffer;
    private final Setting<Color> glowColor = this.register(new Setting("Glow Color", new Color(0xFF00FFFF), v -> colorMode.getValue() == ListSetting.Custom));

    public ShaderESP() {
        super("ShaderESP","Shader ESP", ModuleCategory.RENDER);
    }

    public enum ListSetting {
        Client,
        Custom
    }

    public static void createFrameBuffers() {
        try {
            framebuffer = RenderUtil.createFrameBuffer(framebuffer);
            outlineFrameBuffer = RenderUtil.createFrameBuffer(outlineFrameBuffer);
            glowFrameBuffer = RenderUtil.createFrameBuffer(glowFrameBuffer);
        } catch (Exception ex) {
        }
    }

    public void setupOutlineUniforms(float paramFloat1, float paramFloat2) {
        Color color = getColor();
        this.outlineShader.setUniformi("texture", 0);
        this.outlineShader.setUniformf("radius", this.radius.getValue() / 1.5F);
        this.outlineShader.setUniformf("texelSize", 1.0F / mc.displayWidth, 1.0F / mc.displayHeight);
        this.outlineShader.setUniformf("direction", paramFloat1, paramFloat2);
        this.outlineShader.setUniformf("color", color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
    }

    @SubscribeEvent
    public void render(Render3DEvent e) {
        createFrameBuffers();
        GlStateManager.pushMatrix();
        collectEntities();
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        renderEntities(e.getPartialTicks());
        framebuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }

    public void renderEntities(float paramFloat) {
        entities.forEach(entity -> {
            renderNameTags = false;
            mc.getRenderManager().renderEntityStatic(entity, paramFloat, false);

            renderNameTags = true;
        });
    }

    public void setupGlowUniforms(float paramFloat1, float paramFloat2) {
        Color color = getColor();
        this.glowShader.setUniformi("texture", 0);
        this.glowShader.setUniformf("radius", this.radius.getValue());
        this.glowShader.setUniformf("texelSize", 1.0F / mc.displayWidth, 1.0F / mc.displayHeight);
        this.glowShader.setUniformf("direction", paramFloat1, paramFloat2);
        this.glowShader.setUniformf("color", color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
        this.glowShader.setUniformf("exposure", this.alpha.getValue());
        this.glowShader.setUniformi("avoidTexture", 0);
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 1; i <= this.radius.getValue(); i++) {
            floatBuffer.put(MathUtil.calculateGaussianValue(i, this.radius.getValue() / 2.0F));
            floatBuffer.rewind();
            GL20.glUniform1(this.glowShader.getUniform("weights"), floatBuffer);
        }
    }

    public void collectEntities() {
        this.entities.clear();
        for (Entity entity : mc.world.loadedEntityList) {
            if (isInViewFrustum(entity) && (entity != mc.player || mc.gameSettings.thirdPersonView != 0) && entity instanceof net.minecraft.entity.player.EntityPlayer)
                this.entities.add(entity);
        }
    }

    public boolean isInViewFrustum(Entity entity) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        this.frustum.setPosition(current.posX, current.posY, current.posZ);
        return this.frustum.isBoundingBoxInFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }


    public void onDisable() {
        mc.gameSettings.entityShadows = true;
        super.onDisable();
    }

    public void onEnable() {
        mc.gameSettings.entityShadows = false;
        super.onEnable();
    }

    @SubscribeEvent
    public void render(Render2DEvent event) {
        if (framebuffer != null && outlineFrameBuffer != null && this.entities.size() > 0) {
            GL11.glPushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.0F);
            GlStateManager.enableBlend();
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            outlineFrameBuffer.framebufferClear();
            outlineFrameBuffer.bindFramebuffer(true);
            this.outlineShader.init();
            setupOutlineUniforms(0.0F, 1.0F);
            GlStateManager.bindTexture(framebuffer.framebufferTexture);
            ShaderUtility.drawQuads();
            this.outlineShader.init();
            setupOutlineUniforms(1.0F, 0.0F);
            GlStateManager.bindTexture(framebuffer.framebufferTexture);
            ShaderUtility.drawQuads();
            this.outlineShader.unload();
            outlineFrameBuffer.unbindFramebuffer();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            glowFrameBuffer.framebufferClear();
            glowFrameBuffer.bindFramebuffer(true);
            this.glowShader.init();
            setupGlowUniforms(1.0F, 0.0F);
            GlStateManager.bindTexture(outlineFrameBuffer.framebufferTexture);
            ShaderUtility.drawQuads();
            this.glowShader.unload();
            glowFrameBuffer.unbindFramebuffer();
            mc.getFramebuffer().bindFramebuffer(true);
            this.glowShader.init();
            setupGlowUniforms(0.0F, 1.0F);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GlStateManager.bindTexture(glowFrameBuffer.framebufferTexture);
            ShaderUtility.drawQuads();
            this.glowShader.unload();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.resetColor();
            GL11.glPopMatrix();
        }
    }
    private Color getColor() {
        return new Color(glowColor.getValue().getRGB());
    }

    private static void renderEntities1(float paramFloat, Entity paramEntity) {
        renderNameTags = false;
        mc.getRenderManager().renderEntityStatic(paramEntity, paramFloat, false);
        renderNameTags = true;
    }
}
