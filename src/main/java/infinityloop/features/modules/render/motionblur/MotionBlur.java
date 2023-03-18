package infinityloop.features.modules.render.motionblur;

import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.setting.Setting;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.resources.*;
import net.minecraft.util.*;
import java.lang.reflect.*;
import java.util.Map;

public class MotionBlur
    extends Module {
    private static MotionBlur INSTANCE = new MotionBlur();
    public Setting<Float> blurAmount = this.register(new Setting("blurAmount", 1.0, 0.0, 10.0));
    boolean Setup;
    double old;
    private static Map domainResourceManagers;

    public MotionBlur() {
        super("MotionBlur", "Motion Blur", ModuleCategory.RENDER);
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

    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
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
            MotionBlur.mc.entityRenderer.loadShader(new ResourceLocation("infinityloop", "shaders/motion-blur.json"));
            MotionBlur.mc.entityRenderer.getShaderGroup().createBindFramebuffers(MotionBlur.mc.displayWidth, MotionBlur.mc.displayHeight);
            this.Setup = false;
        }
    }
}