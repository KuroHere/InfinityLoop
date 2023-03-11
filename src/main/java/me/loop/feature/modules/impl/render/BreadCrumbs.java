package me.loop.feature.modules.impl.render;

import me.loop.api.events.impl.render.Render3DEvent;
import me.loop.api.managers.Managers;
import me.loop.api.managers.impl.AccessorRenderManager;
import me.loop.api.utils.impl.maths.MathUtil;
import me.loop.api.utils.impl.worlds.Timer;
import me.loop.feature.modules.Module;
import me.loop.feature.modules.Category;
import me.loop.feature.modules.settings.Setting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class BreadCrumbs extends Module {

    private static BreadCrumbs INSTANCE = new BreadCrumbs();
    ArrayList<BreadCrumb> bcs = new ArrayList<>();

    public Setting<Mode> mode = this.add(new Setting<>("Mode", Mode.DEFAULT));
    public Setting<Float> lineWidht = add(new Setting<>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.mode.getValue() == Mode.DEFAULT));
    public Setting<Integer> tickClear = add(new Setting<>("TickClear",  25, 1, 45));
    public Setting<Float> fadeSpeed = add(new Setting<>("FadeSpeed", Float.valueOf(0.4f), Float.valueOf(0.1f), Float.valueOf(2.0f)));
    public Setting<Color> color  = add(new Setting<>("Alpha", new Color(-1), v -> this.mode.getValue() == Mode.DEFAULT));

    public BreadCrumbs() {
        super("BreadCumbs", "Draw Line behind u(Will fix crash on enable soon)", Category.RENDER, true, false, false);
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
    public void toggle() {
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
                GL11.glColor4f(color.getValue().getRed() / 255.0F, color.getValue().getGreen() / 255.0f, color.getValue().getBlue() / 255.0f, ( float ) (crumb.getAlpha() / 255f));
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
            this.alpha = color.getValue().getAlpha();
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
            this.alpha -= color.getValue().getAlpha() / fadeSpeed.getValue() * Managers.getFpsManagement().getFrametime();
        }

    }

}
