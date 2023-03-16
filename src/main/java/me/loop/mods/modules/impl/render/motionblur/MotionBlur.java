package me.loop.mods.modules.impl.render.motionblur;

import me.loop.mods.modules.Module;
import me.loop.mods.modules.Category;
import me.loop.mods.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.util.Map;

public class MotionBlur
    extends Module {
    private static MotionBlur INSTANCE = new MotionBlur();
    public Setting<Float> blurAmount = this.add(new Setting("blurAmount", 5.0, 0.0, 10.0));
    boolean Setup;
    double old;
    private static Map domainResourceManagers;

    public MotionBlur() {
        super("MotionBlur", "Motion Blur", Category.RENDER, true, false);
        this.Setup = true;
        this.old = 0.0;
        this.setInstance();
    }

    public static MotionBlur getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MotionBlur();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.Setup = true;
        MotionBlur.domainResourceManagers = null;
    }

    @Override
    public void onDisable() {
        MotionBlur.mc.entityRenderer.getShaderGroup();
        MotionBlur.domainResourceManagers = null;
    }
    
    static boolean isFastRenderEnabled() {
        try {
            Field fastRender = GameSettings.class.getDeclaredField("ofFastRender");
            return fastRender.getBoolean(Minecraft.getMinecraft().gameSettings);
        } catch (Exception var1) {
            return false;
        }
    }

    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (MotionBlur.getInstance().isOn() && !Minecraft.getMinecraft().entityRenderer.isShaderActive() && mc.world != null && !isFastRenderEnabled()) {
            applyShader();
        }
        if (this.old != blurAmount.getValue()) {
            this.old = blurAmount.getValue();
            this.Setup = true;
            MotionBlur.domainResourceManagers = null;
            return;
        }
        if (MotionBlur.domainResourceManagers == null) {
            try {
                final Field[] declaredFields;
                final Field[] var2 = declaredFields = SimpleReloadableResourceManager.class.getDeclaredFields();
                for (final Field field : declaredFields) {
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        MotionBlur.domainResourceManagers = (Map)field.get(MotionBlur.mc.getResourceManager());
                        break;
                    }
                }
            }
            catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        }
        assert MotionBlur.domainResourceManagers != null;
        if (!MotionBlur.domainResourceManagers.containsKey("motionblur")) {
            MotionBlur.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }
        if (this.Setup) {
            MotionBlur.mc.entityRenderer.loadShader(new ResourceLocation("minecraft:shaders/program/lunar_motionblur.json"));
            MotionBlur.mc.entityRenderer.getShaderGroup().createBindFramebuffers(MotionBlur.mc.displayWidth, MotionBlur.mc.displayHeight);
            this.Setup = false;
        }
    }

    static void applyShader() {
        MotionBlur.mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
    }
}
