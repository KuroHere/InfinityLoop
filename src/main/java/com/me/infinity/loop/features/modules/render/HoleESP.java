package com.me.infinity.loop.features.modules.render;

import com.me.infinity.loop.event.events.render.Render3DEvent;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;
import com.me.infinity.loop.features.modules.client.Colors;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.util.utils.HoleUtil;
import com.me.infinity.loop.util.utils.minecraft.BlockUtil;
import com.me.infinity.loop.util.utils.renders.ColorUtil;
import com.me.infinity.loop.util.utils.renders.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.awt.*;

public class HoleESP
        extends Module {
    private static HoleESP INSTANCE = new HoleESP();
    private final Setting<Integer> rangeX = this.register(new Setting<Integer>("RangeX", 5, 0, 30));
    private final Setting<Integer> rangeY = this.register(new Setting<Integer>("RangeY", 3, 0, 30));
    public Setting<Boolean> fov = this.register(new Setting<Boolean>("InFov", false));
    public Setting<Boolean> ownHole = this.register(new Setting<Boolean>("OwnHole", false));
    public Setting<Boolean> box = this.register(new Setting<Boolean>("Box", true));
    public Setting<Boolean> boxs = this.register(new Setting("Double Hole", true));
    public Setting<Boolean> gradientBox = this.register(new Setting<Object>("GradientBox", Boolean.valueOf(false), v -> this.box.getValue()));
    public Setting<Boolean> invertGradientBox = this.register(new Setting<Object>("ReverseGradientBox", Boolean.valueOf(false), v -> this.gradientBox.getValue()));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    public Setting<Boolean> gradientOutline = this.register(new Setting<Object>("GradientOutline", Boolean.valueOf(false), v -> this.outline.getValue()));
    public Setting<Boolean> invertGradientOutline = this.register(new Setting<Object>("ReverseGradientOutline", Boolean.valueOf(false), v -> this.gradientOutline.getValue() && this.outline.getValue()));
    public Setting<Double> height = this.register(new Setting<Double>("Height", 0.0, -2.0, 2.0));
    public Setting<Boolean> safeColor = this.register(new Setting<Boolean>("SafeColor", false));
    public Setting<Boolean> customOutline = this.register(new Setting<Object>("CustomLine", Boolean.valueOf(false), v -> this.outline.getValue()));
    private final Setting<Boolean> rainbow = this.register(new Setting("Rainbow", false));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 0, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue()));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue()));
    private final Setting<Integer> safeRed = this.register(new Setting<Object>("SafeRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.safeColor.getValue()));
    private final Setting<Integer> safeGreen = this.register(new Setting<Object>("SafeGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.safeColor.getValue()));
    private final Setting<Integer> safeBlue = this.register(new Setting<Object>("SafeBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.safeColor.getValue()));
    private final Setting<Integer> safeAlpha = this.register(new Setting<Object>("SafeAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.safeColor.getValue()));
    private final Setting<Integer> cRed = this.register(new Setting<Object>("OL-Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() && this.outline.getValue()));
    private final Setting<Integer> cGreen = this.register(new Setting<Object>("OL-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() && this.outline.getValue()));
    private final Setting<Integer> cBlue = this.register(new Setting<Object>("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() && this.outline.getValue()));
    private final Setting<Integer> cAlpha = this.register(new Setting<Object>("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() && this.outline.getValue()));
    private final Setting<Integer> safecRed = this.register(new Setting<Object>("OL-SafeRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
    private final Setting<Integer> safecGreen = this.register(new Setting<Object>("OL-SafeGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
    private final Setting<Integer> safecBlue = this.register(new Setting<Object>("OL-SafeBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
    private final Setting<Integer> safecAlpha = this.register(new Setting<Object>("OL-SafeAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
    private int currentAlpha = 0;

    public HoleESP() {
        super("HoleESP", "Shows safe spots.", ModuleCategory.RENDER);
        this.setInstance();
    }

    public static HoleESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleESP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        assert (HoleESP.mc.renderViewEntity != null);

        Vec3i playerPos = new Vec3i(HoleESP.mc.renderViewEntity.posX, HoleESP.mc.renderViewEntity.posY, HoleESP.mc.renderViewEntity.posZ);

        for (int x = playerPos.getX() - this.rangeX.getValue(); x < playerPos.getX() + this.rangeX.getValue(); ++x) {
            for (int z = playerPos.getZ() - this.rangeX.getValue(); z < playerPos.getZ() + this.rangeX.getValue(); ++z) {
                for (int y = playerPos.getY() + this.rangeY.getValue(); y > playerPos.getY() - this.rangeY.getValue(); --y) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if ((Boolean) this.boxs.getValue()) {
                        if (HoleUtil.is2securityHole(pos) && (mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock() == Blocks.AIR || mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(), HoleUtil.is2Hole(pos).getY() + 1, HoleUtil.is2Hole(pos).getZ())).getBlock() == Blocks.AIR) && mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(), HoleUtil.is2Hole(pos).getY() - 1, HoleUtil.is2Hole(pos).getZ())).getBlock() == Blocks.BEDROCK) {
                            RenderUtil.drawBoxESP(pos, (Boolean) this.rainbow.getValue() ? ColorUtil.rainbow((Integer) Colors.getInstance().rainbowHue.getValue()) : new Color((Integer) this.safeRed.getValue(), (Integer) this.safeGreen.getValue(), (Integer) this.safeBlue.getValue(), (Integer) this.safeAlpha.getValue()), (Boolean) this.customOutline.getValue(), new Color((Integer) this.safecRed.getValue(), (Integer) this.safecGreen.getValue(), (Integer) this.safecBlue.getValue(), (Integer) this.safecAlpha.getValue()), (Float) this.lineWidth.getValue(), (Boolean) this.outline.getValue(), (Boolean) this.box.getValue(), (Integer) this.boxAlpha.getValue(), true, (Double) this.height.getValue(), (Boolean) this.gradientBox.getValue(), (Boolean) this.gradientOutline.getValue(), (Boolean) this.invertGradientBox.getValue(), (Boolean) this.invertGradientOutline.getValue(), this.currentAlpha);
                            RenderUtil.drawBoxESP(HoleUtil.is2Hole(pos), (Boolean) this.rainbow.getValue() ? ColorUtil.rainbow((Integer) Colors.getInstance().rainbowHue.getValue()) : new Color((Integer) this.safeRed.getValue(), (Integer) this.safeGreen.getValue(), (Integer) this.safeBlue.getValue(), (Integer) this.safeAlpha.getValue()), (Boolean) this.customOutline.getValue(), new Color((Integer) this.safecRed.getValue(), (Integer) this.safecGreen.getValue(), (Integer) this.safecBlue.getValue(), (Integer) this.safecAlpha.getValue()), (Float) this.lineWidth.getValue(), (Boolean) this.outline.getValue(), (Boolean) this.box.getValue(), (Integer) this.boxAlpha.getValue(), true, (Double) this.height.getValue(), (Boolean) this.gradientBox.getValue(), (Boolean) this.gradientOutline.getValue(), (Boolean) this.invertGradientBox.getValue(), (Boolean) this.invertGradientOutline.getValue(), this.currentAlpha);
                            continue;
                        }

                        if (HoleUtil.is2Hole(pos) != null && (mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock() == Blocks.AIR || mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(), HoleUtil.is2Hole(pos).getY() + 1, HoleUtil.is2Hole(pos).getZ())).getBlock() == Blocks.AIR) && (mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock() == Blocks.BEDROCK) && (mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(), HoleUtil.is2Hole(pos).getY() - 1, HoleUtil.is2Hole(pos).getZ())).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(), HoleUtil.is2Hole(pos).getY() - 1, HoleUtil.is2Hole(pos).getZ())).getBlock() == Blocks.OBSIDIAN)) {
                            RenderUtil.drawBoxESP(pos, (Boolean) this.rainbow.getValue() ? ColorUtil.rainbow((Integer) Colors.getInstance().rainbowHue.getValue()) : new Color((Integer) this.red.getValue(), (Integer) this.green.getValue(), (Integer) this.blue.getValue(), (Integer) this.alpha.getValue()), (Boolean) this.customOutline.getValue(), new Color((Integer) this.cRed.getValue(), (Integer) this.cGreen.getValue(), (Integer) this.cBlue.getValue(), (Integer) this.cAlpha.getValue()), (Float) this.lineWidth.getValue(), (Boolean) this.outline.getValue(), (Boolean) this.box.getValue(), (Integer) this.boxAlpha.getValue(), true, (Double) this.height.getValue(), (Boolean) this.gradientBox.getValue(), (Boolean) this.gradientOutline.getValue(), (Boolean) this.invertGradientBox.getValue(), (Boolean) this.invertGradientOutline.getValue(), this.currentAlpha);
                            RenderUtil.drawBoxESP(HoleUtil.is2Hole(pos), (Boolean) this.rainbow.getValue() ? ColorUtil.rainbow((Integer) Colors.getInstance().rainbowHue.getValue()) : new Color((Integer) this.red.getValue(), (Integer) this.green.getValue(), (Integer) this.blue.getValue(), (Integer) this.alpha.getValue()), (Boolean) this.customOutline.getValue(), new Color((Integer) this.cRed.getValue(), (Integer) this.cGreen.getValue(), (Integer) this.cBlue.getValue(), (Integer) this.cAlpha.getValue()), (Float) this.lineWidth.getValue(), (Boolean) this.outline.getValue(), (Boolean) this.box.getValue(), (Integer) this.boxAlpha.getValue(), true, (Double) this.height.getValue(), (Boolean) this.gradientBox.getValue(), (Boolean) this.gradientOutline.getValue(), (Boolean) this.invertGradientBox.getValue(), (Boolean) this.invertGradientOutline.getValue(), this.currentAlpha);
                            continue;
                        }
                    }
                    if (mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && (!pos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)) || (Boolean) this.ownHole.getValue()) && (BlockUtil.isPosInFov(pos) || !(Boolean) this.fov.getValue())) {
                        if (mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                            RenderUtil.drawBoxESP(pos, (Boolean) this.rainbow.getValue() ? ColorUtil.rainbow((Integer) Colors.getInstance().rainbowHue.getValue()) : new Color((Integer) this.safeRed.getValue(), (Integer) this.safeGreen.getValue(), (Integer) this.safeBlue.getValue(), (Integer) this.safeAlpha.getValue()), (Boolean) this.customOutline.getValue(), new Color((Integer) this.safecRed.getValue(), (Integer) this.safecGreen.getValue(), (Integer) this.safecBlue.getValue(), (Integer) this.safecAlpha.getValue()), (Float) this.lineWidth.getValue(), (Boolean) this.outline.getValue(), (Boolean) this.box.getValue(), (Integer) this.boxAlpha.getValue(), true, (Double) this.height.getValue(), (Boolean) this.gradientBox.getValue(), (Boolean) this.gradientOutline.getValue(), (Boolean) this.invertGradientBox.getValue(), (Boolean) this.invertGradientOutline.getValue(), this.currentAlpha);
                        } else if (BlockUtil.isBlockUnSafe(mc.world.getBlockState(pos.down()).getBlock()) && BlockUtil.isBlockUnSafe(mc.world.getBlockState(pos.east()).getBlock()) && BlockUtil.isBlockUnSafe(mc.world.getBlockState(pos.west()).getBlock()) && BlockUtil.isBlockUnSafe(mc.world.getBlockState(pos.south()).getBlock()) && BlockUtil.isBlockUnSafe(mc.world.getBlockState(pos.north()).getBlock())) {
                            RenderUtil.drawBoxESP(pos, (Boolean) this.rainbow.getValue() ? ColorUtil.rainbow((Integer) Colors.getInstance().rainbowHue.getValue()) : new Color((Integer) this.red.getValue(), (Integer) this.green.getValue(), (Integer) this.blue.getValue(), (Integer) this.alpha.getValue()), (Boolean) this.customOutline.getValue(), new Color((Integer) this.cRed.getValue(), (Integer) this.cGreen.getValue(), (Integer) this.cBlue.getValue(), (Integer) this.cAlpha.getValue()), (Float) this.lineWidth.getValue(), (Boolean) this.outline.getValue(), (Boolean) this.box.getValue(), (Integer) this.boxAlpha.getValue(), true, (Double) this.height.getValue(), (Boolean) this.gradientBox.getValue(), (Boolean) this.gradientOutline.getValue(), (Boolean) this.invertGradientBox.getValue(), (Boolean) this.invertGradientOutline.getValue(), this.currentAlpha);
                        }
                    }
                }
            }
        }
    }
}