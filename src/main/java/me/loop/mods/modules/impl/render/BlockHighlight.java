package me.loop.mods.modules.impl.render;

import me.loop.api.events.impl.render.Render3DEvent;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.mods.modules.Module;
import me.loop.mods.modules.Category;
import me.loop.mods.modules.impl.client.Colors;
import me.loop.mods.settings.Setting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;


public class BlockHighlight
        extends Module {
    private static BlockHighlight INSTANCE = new BlockHighlight();
    public Setting<RenderMode> mode = add(new Setting<>("Mode", RenderMode.Defaut));

    public Setting<Boolean> gradient = this.add(new Setting<>("Gradient", Boolean.valueOf(true), v -> this.mode.getValue() == RenderMode.Gradient));
    public Setting<Float> gradientHeight = this.add(new Setting<>("GHeight", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(1.0f), v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gbRed = add(new Setting<>("BottomG-Red", 255, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gbGreen = add(new Setting<>("BottomG-Green", 0, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gbBlue = add(new Setting<>("BottomG-Blue", 0, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gbAlpha = add(new Setting<>("BottomG-Alpha", 60, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gtRed = add(new Setting<>("TopG-Red", 0, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gtGreen = add(new Setting<>("TopG-Green", 0, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gtBlue = add(new Setting<>("TopG-Blue", 255, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gtAlpha = add(new Setting<>("TopG-Alpha", 50, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));

    public Setting<Boolean> defaut = this.add(new Setting<>("Defaut", Boolean.valueOf(true), v -> this.mode.getValue() == RenderMode.Defaut));
    public Setting<Boolean> box = this.add(new Setting<>("Box", false, v -> this.mode.getValue() == RenderMode.Defaut));
    private final Setting<Integer> boxRed = add(new Setting<>("B-Red", 0, 0, 255, v -> this.box.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> boxGreen = add(new Setting<>("B-reen", 255, 0, 255, v -> this.box.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> boxBlue = add(new Setting<>("B-Blue", 0, 0, 255, v -> this.box.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> boxAlpha = add(new Setting<>("B-Alpha", 20, 0, 255, v -> this.box.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    public Setting<Boolean> outline = this.add(new Setting<>("OutLine", false, v -> this.mode.getValue() == RenderMode.Defaut));
    public Setting<Float> lineWidth = add(new Setting<>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> ored = add(new Setting<>("OL-Red", 0, 0, 255, v -> this.outline.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> ogreen = add(new Setting<>("OL-Green", 255, 0, 255, v -> this.outline.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> oblue = add(new Setting<>("OL-Blue", 0, 0, 255, v -> this.outline.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> oAlpha = add(new Setting<>("OL-Alpha", 255, 0, 255, v -> this.outline.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    public Setting<Boolean> sync = add(new Setting<>("RainbowSync", false));
    private int color;

    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block u look at.", Category.RENDER, true, false);
        this.setInstance();
    }

    public static BlockHighlight getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlockHighlight();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();
            if ((BlockHighlight.getInstance()).mode.getValue() == RenderMode.Defaut) {
                RenderUtil.drawBoxESP(blockpos, sync.getValue() && Colors.getInstance().rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(this.boxRed.getValue(), this.boxGreen.getValue(), this.boxBlue.getValue(), this.boxAlpha.getValue()), this.outline.getValue(), sync.getValue() && Colors.getInstance().rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(this.ored.getValue(), this.ogreen.getValue(), this.oblue.getValue(), this.oAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
            }
            if ((BlockHighlight.getInstance()).mode.getValue() == RenderMode.Gradient) {
                RenderUtil.drawSelectionGlowFilledBox(blockpos, this.gradientHeight.getValue() - 1, new Color(this.gbRed.getValue(), this.gbGreen.getValue(), this.gbBlue.getValue(), this.gbAlpha.getValue().intValue()), new Color(this.gtRed.getValue(), this.gtGreen.getValue(), this.gtBlue.getValue(), this.gtAlpha.getValue()));
            }
        }
    }

    public enum RenderMode {
        Defaut,
        Gradient,
    }
}














