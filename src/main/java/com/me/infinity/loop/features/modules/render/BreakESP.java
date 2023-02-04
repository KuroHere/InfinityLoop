package com.me.infinity.loop.features.modules.render;

/*import com.me.alpha432.oyvey.event.events.DrawBlockDamageEvent;
import com.me.alpha432.oyvey.event.events.PacketEvent;
import com.me.alpha432.oyvey.event.events.Render3DEvent;
import com.me.alpha432.oyvey.features.modules.Module;
import com.me.alpha432.oyvey.features.modules.client.Colors;
import com.me.alpha432.oyvey.features.setting.Setting;
import com.me.alpha432.oyvey.util.minecraft.BlockUtil;
import com.me.alpha432.oyvey.util.renders.ColorUtil;
import com.me.alpha432.oyvey.util.renders.RenderUtil;
import com.me.alpha432.oyvey.util.worlds.GeometryMasks;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BreakESP extends Module {

    private static BreakESP INSTANCE = new BreakESP();
    public Setting<RenderType> renderType = this.register(new Setting<Object>("Render", RenderType.Both));
    public Setting<Integer> range = this.register(new Setting("Range", 100, 1, 200));
    public Setting<Boolean> showPacket = this.register(new Setting("showPacketMine", false));
    public Setting<Integer> tickPacket = this.register(new Setting("TickPacket", 50, 0, 200, v -> this.showPacket.getValue()));
    public Setting<Integer> stillRender = this.register(new Setting("StillRender", 20, 0, 500, v -> this.showPacket.getValue()));
    public Setting<Boolean> cancelAnimation = this.register(new Setting("CancelAnimation", false));
    public Setting<Boolean> showColor = this.register(new Setting("ShowColor", true));
    //CNC
    public Setting<Boolean> outline = this.register(new Setting("Outline", true, v-> showColor.getValue() && (BreakESP.getInstance().renderType.getValue() == RenderType.Both || BreakESP.getInstance().renderType.getValue() == RenderType.Outline)));
    public Setting<Float> lineWidth = this.register(new Setting("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v-> showColor.getValue() && (BreakESP.getInstance().renderType.getValue() == RenderType.Both || BreakESP.getInstance().renderType.getValue() == RenderType.Outline)));
    public Setting<Integer> outlineRed = this.register(new Setting("OutlineRed", 0, 0, 255, v -> this.showColor.getValue() && this.outline.getValue()));
    public Setting<Integer> outlineGreen = this.register(new Setting("OutlineGreen", 255, 0, 255, v -> this.showColor.getValue() && this.outline.getValue()));
    public Setting<Integer> outlineBlue = this.register(new Setting("OutlineBlue", 0, 0, 255, v -> this.showColor.getValue() && this.outline.getValue()));
    public Setting<Integer> outlineAlpha = this.register(new Setting("OutlineAlpha", 150, 0, 255, v -> this.showColor.getValue() && this.outline.getValue()));
    //CCR
    public Setting<Boolean> fillColor = this.register(new Setting("FillColor", true, v-> showColor.getValue() && (BreakESP.getInstance().renderType.getValue() == RenderType.Both || BreakESP.getInstance().renderType.getValue() == RenderType.Fill)));
    public Setting<Integer> fillRed = this.register(new Setting("FillRed", 0, 0, 255, v -> this.showColor.getValue() && this.fillColor.getValue()));
    public Setting<Integer> fillGreen = this.register(new Setting("FillGreen", 255, 0, 255, v -> this.showColor.getValue() && this.fillColor.getValue()));
    public Setting<Integer> fillBlue = this.register(new Setting("FillBlue", 05, 0, 255, v -> this.showColor.getValue() && this.fillColor.getValue()));
    public Setting<Integer> fillAlpha = this.register(new Setting("FillAlpha", 150, 0, 255, v -> this.showColor.getValue() && this.fillColor.getValue()));

    //TextColor
    public Setting<Boolean> showPercentage = this.register(new Setting("ShowPercentage", false));
    public Setting<Boolean> textColor = this.register(new Setting("TextColor", true, v-> showPercentage.getValue()));
    public Setting<Boolean> rainbow = this.register(new Setting("TextRainbow", true, v-> showPercentage.getValue() && this.textColor.getValue()));
    public Setting<Integer> textColorRed = this.register(new Setting("TextColorRed", 255, 0, 255, v -> this.showPercentage.getValue() && this.textColor.getValue()));
    public Setting<Integer> textColorGreen = this.register(new Setting("TextColorGreen", 255, 0, 255, v -> this.showPercentage.getValue() && this.textColor.getValue()));
    public Setting<Integer> textColorBlue = this.register(new Setting("TextColorBlue", 255, 0, 255, v -> this.showPercentage.getValue() && this.textColor.getValue()));
    public Setting<Integer> textColorAlpha = this.register(new Setting("TextColorAlpha", 255, 0, 255, v -> this.showPercentage.getValue() && this.textColor.getValue()));
    ArrayList<ArrayList<Object>> possiblePacket = new ArrayList<>();
    private final RenderUtil renderUtil = new RenderUtil();

    public BreakESP() {
        super("BreakESP", "Highlights the block u break", Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static BreakESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BreakESP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    // Fast Reset, this is on by default since well, it has no cons
    @EventHandler
    private final Listener<PacketEvent.Receive> packetReceiveListener = new Listener<>(event -> {
        if (!showPacket.getValue())
            return;
        // If packet digging
        if (event.getPacket() instanceof SPacketBlockBreakAnim) {
            // Get it
            SPacketBlockBreakAnim pack = (SPacketBlockBreakAnim) event.getPacket();
            // If we dont have it
            if (!havePos(pack.getPosition()))
                possiblePacket.add(new ArrayList<Object>() {{
                    add(pack.getPosition());
                    add(0);
                }});


        }
    });

    boolean havePos(BlockPos pos) {
        for (ArrayList<Object> part : possiblePacket) {
            // If we already have it
            BlockPos temp = (BlockPos) part.get(0);
            if (temp.getX() == pos.getX() && temp.getY() == pos.getY() && temp.getZ() == pos.getZ()) {
                // Remove
                return true;
            }
        }
        return false;
    }

    public void onRender3D(Render3DEvent event) {
        ArrayList<BlockPos> displayed = new ArrayList<>();
        mc.renderGlobal.damagedBlocks.forEach((integer, destroyBlockProgress) -> {
            if (destroyBlockProgress != null) {
                BlockPos blockPos = destroyBlockProgress.getPosition();
                if (mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR) return;
                if (blockPos.getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) <= range.getValue()) {

                    displayed.add(blockPos);

                    int progress = destroyBlockProgress.getPartialBlockDamage();
                    AxisAlignedBB axisAlignedBB = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);
                    if (showColor.getValue())
                        renderESP(axisAlignedBB, progress, progress >= 8 ? rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(fillRed.getValue(), fillGreen.getValue(), outlineBlue.getValue(), fillAlpha.getValue()) : rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(outlineRed.getValue(), outlineGreen.getValue(), outlineBlue.getValue(), outlineAlpha.getValue()),2);
                    float temp;
                    if (showPercentage.getValue())
                        showPercentage(blockPos, new String[]{String.format("%.02f%%", (temp = (float) progress / 2 * 25) >= 100 ? 100 : temp)});
                }
            }
        });

        if (showPacket.getValue()) {

            for (int i = 0; i < possiblePacket.size(); i++) {

                BlockPos temp = (BlockPos) possiblePacket.get(i).get(0);
                int tick = (int) possiblePacket.get(i).get(1);

                if (BlockUtil.getBlock(temp) instanceof BlockAir) {
                    possiblePacket.remove(i);
                    i--;
                    continue;
                }

                if (!displayed.contains(temp)) {
                    if (temp.getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) <= range.getValue()) {
                        AxisAlignedBB axisAlignedBB = mc.world.getBlockState(temp).getSelectedBoundingBox(mc.world, temp);
                        renderESP(axisAlignedBB, tick >= tickPacket.getValue() ? tickPacket.getValue() : tick,
                                tick > tickPacket.getValue() ? rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(this.fillRed.getValue(), this.fillGreen.getValue(), this.fillBlue.getValue(), this.fillAlpha.getValue()) : new Color(outlineRed.getValue(), outlineGreen.getValue(), outlineBlue.getValue(), outlineAlpha.getValue()), tickPacket.getValue());
                        if (showPercentage.getValue())
                            showPercentage(temp, new String[]{String.format("%.02f%%", (float) (tick >= tickPacket.getValue() ? tickPacket.getValue() : tick) / tickPacket.getValue() * 100)});
                    }
                } else possiblePacket.get(i).set(1, ++tick);
                if (++tick > tickPacket.getValue() + stillRender.getValue()) {
                    possiblePacket.remove(i);
                    i--;
                } else possiblePacket.get(i).set(1, tick);
            }
        }


    }

    public void showPercentage(BlockPos pos, String[] perc) {
        if (this.showPercentage.getValue()) {
            renderUtil.drawText(pos, Arrays.toString(perc), rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(textColorRed.getValue(), textColorGreen.getValue(), textColorBlue.getValue(), textColorAlpha.getValue()));
        }
    }

    private void renderESP(AxisAlignedBB axisAlignedBB, int progress, Color color, int max) {

        double centerX = axisAlignedBB.minX + ((axisAlignedBB.maxX - axisAlignedBB.minX) / 2);
        double centerY = axisAlignedBB.minY + ((axisAlignedBB.maxY - axisAlignedBB.minY) / 2);
        double centerZ = axisAlignedBB.minZ + ((axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2);
        double progressValX = progress * ((axisAlignedBB.maxX - centerX) / max);
        double progressValY = progress * ((axisAlignedBB.maxY - centerY) / max);
        double progressValZ = progress * ((axisAlignedBB.maxZ - centerZ) / max);

        AxisAlignedBB bb = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
        if ((BreakESP.getInstance()).renderType.getValue() == RenderType.Both) {
            RenderUtil.drawBox(bb, true, 0, Colors.getInstance().rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(fillRed.getValue(), fillGreen.getValue(), outlineBlue.getValue(), fillAlpha.getValue()), GeometryMasks.Quad.ALL);
            //RenderUtil.drawBoundingBox(bb, lineWidth.getValue(), new Color(255, 255, 255), outlineColor.getAlpha());
        }
        if ((BreakESP.getInstance()).renderType.getValue() == RenderType.Fill) {
            RenderUtil.drawBox(bb, true, 0, Colors.getInstance().rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(fillRed.getValue(), fillGreen.getValue(), outlineBlue.getValue(), fillAlpha.getValue()), GeometryMasks.Quad.ALL);
        }
        if ((BreakESP.getInstance()).renderType.getValue() == RenderType.Outline) {
            //RenderUtil.drawBoundingBox(bb, lineWidth.getValue(), new Color(255, 255, 255), outlineColor.getAlpha());
        }
        if ((BreakESP.getInstance()).renderType.getValue() == RenderType.None) {
        }

    }

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<DrawBlockDamageEvent> drawBlockDamageEventListener = new Listener<>(event -> {
        if (cancelAnimation.getValue()) {
            event.isCanceled();
        }
    });

    public enum RenderType {
        Both,
        Outline,
        Fill,
        None

    }
}*/

