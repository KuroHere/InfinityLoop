package com.me.infinity.loop.features.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.UUID;

import com.me.infinity.loop.Loop;
import com.me.infinity.loop.event.events.Render3DEvent;
import com.me.infinity.loop.util.player.EntityUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.me.infinity.loop.features.command.Command;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.util.worlds.MathUtil;
import com.me.infinity.loop.util.renders.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class PearlRender
        extends Module {
    private final HashMap<EntityPlayer, UUID> list;
    private Entity enderPearl;
    private boolean flag;
    public Setting<Boolean> notify = this.register(new Setting<Boolean>("Notify", false));

    public PearlRender() {
        super("PearlRender", "Renders where pearls will go", Category.RENDER, true, false, false);
        this.list = new HashMap<EntityPlayer, UUID>();
    }

    @Override
    public void onEnable() {
        if (this.notify.getValue()) {
            this.flag = true;
        }
    }

    @Override
    public void onUpdate() {
        if (notify.getValue().booleanValue()) {
            if (PearlRender.mc.world == null || PearlRender.mc.player == null) {
                return;
            }
            this.enderPearl = null;
            for (final Entity e : PearlRender.mc.world.loadedEntityList) {
                if (e instanceof EntityEnderPearl) {
                    this.enderPearl = e;
                    break;
                }
            }
            if (this.enderPearl == null) {
                this.flag = true;
                return;
            }
            EntityPlayer closestPlayer = null;
            for (final EntityPlayer entity : PearlRender.mc.world.playerEntities) {
                if (closestPlayer == null) {
                    closestPlayer = entity;
                } else {
                    if (closestPlayer.getDistance(this.enderPearl) <= entity.getDistance(this.enderPearl)) {
                        continue;
                    }
                    closestPlayer = entity;
                }
            }
            if (closestPlayer == PearlRender.mc.player) {
                this.flag = false;
            }
            if (closestPlayer != null && this.flag) {
                String faceing = this.enderPearl.getHorizontalFacing().toString();
                if (faceing.equals("west")) {
                    faceing = "east";
                } else if (faceing.equals("east")) {
                    faceing = "west";
                }
                Command.sendMessage(Loop.friendManager.isFriend(closestPlayer.getName()) ? (ChatFormatting.AQUA + closestPlayer.getName() + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!") : (ChatFormatting.RED + closestPlayer.getName() + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!"));
                this.flag = false;
            }
        }
    }


    public double interpolate(double now, double then) {
        return then + (now - then) * (double)mc.getRenderPartialTicks();
    }

    public double[] interpolate(Entity entity) {
        double posX = this.interpolate(entity.posX, entity.lastTickPosX) - PearlRender.mc.getRenderManager().renderPosX;
        double posY = this.interpolate(entity.posY, entity.lastTickPosY) - PearlRender.mc.getRenderManager().renderPosY;
        double posZ = this.interpolate(entity.posZ, entity.lastTickPosZ) - PearlRender.mc.getRenderManager().renderPosZ;
        return new double[]{posX, posY, posZ};
    }

    public void drawLineToEntity(Entity e, float red, float green, float blue, float opacity) {
        double[] xyz = this.interpolate(e);
        this.drawLine(xyz[0], xyz[1], xyz[2], red, green, blue, opacity);
    }

    public void drawLine(double posx, double posy, double posz, float red, float green, float blue, float opacity) {
        Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-((float)Math.toRadians(PearlRender.mc.player.rotationPitch))).rotateYaw(-((float)Math.toRadians(PearlRender.mc.player.rotationYaw)));
        this.drawLineFromPosToPos(eyes.x, eyes.y + (double)PearlRender.mc.player.getEyeHeight(), eyes.z, posx, posy, posz, red, green, blue, opacity);
    }

    public void drawLineFromPosToPos(double posx, double posy, double posz, double posx2, double posy2, double posz2, float red, float green, float blue, float opacity) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, opacity);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLoadIdentity();
        boolean bobbing = PearlRender.mc.gameSettings.viewBobbing;
        PearlRender.mc.gameSettings.viewBobbing = false;
        PearlRender.mc.entityRenderer.orientCamera(mc.getRenderPartialTicks());
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glColor3d(1.0, 1.0, 1.0);
        PearlRender.mc.gameSettings.viewBobbing = bobbing;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        int i = 0;
        for (Entity entity : PearlRender.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderPearl) || !(PearlRender.mc.player.getDistanceSq(entity) < 2500.0)) continue;
            Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
            AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(1.0f);
            RenderGlobal.renderFilledBox(bb, 255.0f, 255.0f, 255.0f, 255.0f);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            RenderUtil.drawBlockOutline(bb, new Color(255, 255, 255), 1.0f);
            this.drawLineToEntity(entity, 255.0f, 255.0f, 255.0f, 255.0f);
            BlockPos posEntity = entity.getPosition();
            RenderUtil.drawText(posEntity, "X: " + MathUtil.round(entity.posX, 0) + " Y: " + MathUtil.round(entity.posY, 0) + " Z:" + MathUtil.round(entity.posZ, 2));
            if (++i < 50) continue;
        }
    }
}
