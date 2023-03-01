package com.me.infinity.loop.features.modules.render;

import com.me.infinity.loop.event.events.render.Render3DEvent;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;
import com.me.infinity.loop.features.modules.client.Colors;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.util.utils.renders.ColorUtil;
import com.me.infinity.loop.util.utils.renders.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;


public class BlockHighlight
        extends Module {
    private static BlockHighlight INSTANCE = new BlockHighlight();
    public Setting<RenderMode> mode = register(new Setting<>("Mode", RenderMode.Defaut));

    public Setting<Boolean> gradient = this.register(new Setting<>("Gradient", Boolean.valueOf(true), v -> this.mode.getValue() == RenderMode.Gradient));
    public Setting<Float> gradientHeight = this.register(new Setting<>("GHeight", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(1.0f), v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gbRed = register(new Setting<>("BottomG-Red", 255, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gbGreen = register(new Setting<>("BottomG-Green", 0, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gbBlue = register(new Setting<>("BottomG-Blue", 0, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gbAlpha = register(new Setting<>("BottomG-Alpha", 60, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gtRed = register(new Setting<>("TopG-Red", 0, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gtGreen = register(new Setting<>("TopG-Green", 0, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gtBlue = register(new Setting<>("TopG-Blue", 255, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));
    private final Setting<Integer> gtAlpha = register(new Setting<>("TopG-Alpha", 50, 0, 255, v -> this.mode.getValue() == RenderMode.Gradient && this.gradient.getValue()));

    public Setting<Boolean> defaut = this.register(new Setting<>("Defaut", Boolean.valueOf(true), v -> this.mode.getValue() == RenderMode.Defaut));
    public Setting<Boolean> box = this.register(new Setting<>("Box", false, v -> this.mode.getValue() == RenderMode.Defaut));
    private final Setting<Integer> boxRed = register(new Setting<>("B-Red", 0, 0, 255, v -> this.box.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> boxGreen = register(new Setting<>("B-reen", 255, 0, 255, v -> this.box.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> boxBlue = register(new Setting<>("B-Blue", 0, 0, 255, v -> this.box.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> boxAlpha = register(new Setting<>("B-Alpha", 20, 0, 255, v -> this.box.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    public Setting<Boolean> outline = this.register(new Setting<>("OutLine", false, v -> this.mode.getValue() == RenderMode.Defaut));
    public Setting<Float> lineWidth = register(new Setting<>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> ored = register(new Setting<>("OL-Red", 0, 0, 255, v -> this.outline.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> ogreen = register(new Setting<>("OL-Green", 255, 0, 255, v -> this.outline.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> oblue = register(new Setting<>("OL-Blue", 0, 0, 255, v -> this.outline.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    private final Setting<Integer> oAlpha = register(new Setting<>("OL-Alpha", 255, 0, 255, v -> this.outline.getValue() && this.mode.getValue() == RenderMode.Defaut && this.defaut.getValue()));
    public Setting<Boolean> sync = register(new Setting<>("RainbowSync", false));
    private int color;

    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block u look at.", ModuleCategory.RENDER);
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














