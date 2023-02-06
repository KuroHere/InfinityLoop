package com.me.infinity.loop.features.modules.render;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.event.events.Render3DEvent;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.manager.AccessorRenderManager;
import com.me.infinity.loop.util.worlds.MathUtil;
import com.me.infinity.loop.util.worlds.Timer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class BreadCrumbs extends Module {

    private static BreadCrumbs INSTANCE = new BreadCrumbs();
    ArrayList<BreadCrumb> bcs = new ArrayList<>();

    public Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.DEFAULT));
    public Setting<Integer> red = register(new Setting<>("Red", 255, 0, 255, v -> this.mode.getValue() == Mode.DEFAULT));
    public Setting<Integer> green = register(new Setting<>("Green", 255, 0, 255, v -> this.mode.getValue() == Mode.DEFAULT));
    public Setting<Integer> blue = register(new Setting<>("Blue", 255, 0, 255, v -> this.mode.getValue() == Mode.DEFAULT));
    public Setting<Integer> lAlpha = register(new Setting<>("Alpha", 255, 0, 255, v -> this.mode.getValue() == Mode.DEFAULT));
    public Setting<Float> lineWidht = register(new Setting<>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.mode.getValue() == Mode.DEFAULT));
    public Setting<Integer> tickClear = register(new Setting<>("TickClear",  25, 1, 45));
    public Setting<Float> fadeSpeed = register(new Setting<>("FadeSpeed", Float.valueOf(0.4f), Float.valueOf(0.1f), Float.valueOf(2.0f)));

    public BreadCrumbs() {
        super("BreadCumbs", "Draw Line behind u", Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static BreadCrumbs getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BreadCrumbs();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public enum Mode {
        DEFAULT,
        PARTICLE
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (BreadCrumbs.getInstance().mode.getValue() == Mode.PARTICLE)
            mc.world.spawnParticle(EnumParticleTypes.DRIP_WATER, mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.motionX, mc.player.motionY, mc.player.motionZ, 2);
    }

    public static void putVertex3d(Vec3d vec) {
        GL11.glVertex3d(vec.x, vec.y, vec.z);
    }

    public Vec3d getRenderPos(double x, double y, double z) {
        AccessorRenderManager renderManager = (AccessorRenderManager) mc.getRenderManager( );
        x = x - renderManager.getRenderPosX();
        y = y - renderManager.getRenderPosY();
        z = z - renderManager.getRenderPosZ();
        return new Vec3d(x, y, z);
    }

    @Override
    public void onToggle() {
        bcs.clear();
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (BreadCrumbs.getInstance().mode.getValue() == Mode.DEFAULT) {
            double interpolatedX = MathUtil.interpolate(mc.player.lastTickPosX, mc.player.posX, mc.getRenderPartialTicks());
            double interpolatedY = MathUtil.interpolate(mc.player.lastTickPosY, mc.player.posY, mc.getRenderPartialTicks());
            double interpolatedZ = MathUtil.interpolate(mc.player.lastTickPosZ, mc.player.posZ, mc.getRenderPartialTicks());
            bcs.add(new BreadCrumb(new Vec3d(interpolatedX, interpolatedY, interpolatedZ)));
            int time = this.tickClear.getValue() * 50;
            GL11.glPushAttrib(1048575);
            GL11.glPushMatrix();
            GL11.glDisable(3008);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glEnable(2884);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4353);
            GL11.glDisable(2896);
            GL11.glLineWidth(this.lineWidht.getValue());
            GL11.glBegin(3);
            for (int i = 0; i < bcs.size(); i++) {
                BreadCrumb crumb = bcs.get(i);
                if (crumb.getTimer().passed(time)) {
                    crumb.update(bcs);
                }
                GL11.glColor4f(this.red.getValue() / 255.0F, this.green.getValue() / 255.0f, this.blue.getValue() / 255.0f, ( float ) (this.lAlpha.getValue() / 255f));
                putVertex3d(getRenderPos(crumb.getVector().x, crumb.getVector().y + 0.3, crumb.getVector().z));
            }
            GL11.glEnd();
            GL11.glEnable(2896);
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glEnable(3008);
            GL11.glDepthMask(true);
            GL11.glCullFace(1029);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    public class BreadCrumb {

        Vec3d vector;
        Timer timer;
        double alpha;

        public BreadCrumb(Vec3d vector) {
            timer = new Timer();
            timer.reset();
            this.vector = vector;
            this.alpha = lAlpha.getValue();
        }

        public Timer getTimer() {
            return timer;
        }

        public Vec3d getVector() {
            return vector;
        }

        public double getAlpha() {
            return alpha;
        }

        public void update(ArrayList arrayList) {
            if (alpha <= 0) {
                alpha = 0;
                arrayList.remove(this);
            }
            this.alpha -= lAlpha.getValue() / fadeSpeed.getValue() * InfinityLoop.getFpsManagement().getFrametime();
        }

    }

}
