package com.me.infinity.loop.features.modules.client;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.event.events.ClientEvent;
import com.me.infinity.loop.features.ui.InfinityLoopGui;
import com.me.infinity.loop.features.ui.screen.anchor.AnchorPoint;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.setting.ColorSetting;
import com.me.infinity.loop.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ClickGui
        extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public Setting<Locate> topText = this.register(new Setting("TopText", Locate.Right));
    public Setting<Mode> description = this.register(new Setting("Description", Mode.Frame));
    //public Setting<Boolean> guiGlow= this.register(new Setting("Glow", true));
    //public Setting<Integer> glowOffset = this.register(new Setting<>("GlowOffset", 2, 0, 20, v -> this.guiGlow.getValue()));
    public Setting<Boolean> showBind = register(new Setting("ShowBind",true));
    public Setting<Boolean> openCloseChange = register(new Setting("Open/Close",false));
    public Setting<String> open = register(new Setting("Open:", "+", v -> this.openCloseChange.getValue()).setRenderName(true));
    public Setting<String> close = register(new Setting("Close:", "-", v -> this.openCloseChange.getValue()).setRenderName(true));
    public Setting<Boolean> background= this.register(new Setting("Background", true));
    public Setting<Boolean> particles = this.register(new Setting("Particles", true, v -> this.background.getValue()));
    public Setting<Integer> particleLength = this.register(new Setting<>("ParticlesLength", 80, 0, 300, v -> this.background.getValue() && this.particles.getValue()));
    //public Setting<BGShader> shader = this.register(new Setting("ShaderBG", BGShader.none, v -> this.background.getValue()));
    public Setting<Boolean> blur = this.register(new Setting("Blur", false, v -> this.background.getValue()));
    public Setting<Integer> blurAmount = this.register(new Setting<>("BlurAmount", 2, 0, 20, v -> this.background.getValue() && this.blur.getValue()));
    public Setting<Integer> blurSize = this.register(new Setting<>("BlurSize", 0, 0, 20, v -> this.background.getValue() && this.blur.getValue()));
    public Setting<Boolean> dark = this.register(new Setting("Dark", true, v -> this.background.getValue()));
    public Setting<Boolean> gradiant = this.register(new Setting("Gradiant", false, v -> this.background.getValue()));
    public Setting<Integer> gradiantHeight = this.register(new Setting("GradiantHeight", 0, 0, 255, v -> this.background.getValue() && this.gradiant.getValue()));
    public Setting<Integer> gradiantAlpha = this.register(new Setting<>("GradiantAlpha", 230, 0, 255, v -> this.background.getValue() && this.gradiant.getValue()));
    public Setting<Integer> red = this.register(new Setting<>("Red", 160, 0, 255));
    public Setting<Integer> green = this.register(new Setting<>("Green", 10, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<>("Blue", 255, 0, 255));
    public Setting<Integer> hoverAlpha = this.register(new Setting<>("Alpha", 170, 0, 255));
    public Setting<Boolean> colorSync = this.register(new Setting<>("ColorSync", false));
    public Setting<Integer> topRed = this.register(new Setting<>("SecondRed", 0, 0, 255));
    public Setting<Integer> topGreen = this.register(new Setting<>("SecondGreen", 0, 0, 255));
    public Setting<Integer> topBlue = this.register(new Setting<>("SecondBlue", 0, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<>("HoverAlpha", 240, 0, 255));
    public Setting<ColorSetting> buttsColor = this.register(new Setting<>("buttsColor", new ColorSetting(0x8800FF00)));
    private InfinityLoopGui click;
    private List<AnchorPoint> anchorPoints = new ArrayList<>();
    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        InfinityLoop.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new InfinityLoopGui());
    }

    @Override
    public void onLoad() {
        if (this.colorSync.getValue()) {
            InfinityLoop.colorManager.setColor(Colors.getInstance().getCurrentColor().getRed(), Colors.getInstance().getCurrentColor().getGreen(), Colors.getInstance().getCurrentColor().getBlue(), this.alpha.getValue());
        } else {
            InfinityLoop.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        }
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof InfinityLoopGui)) {
            this.disable();
        }
    }

    /*@Override
    public void onUpdate() {
        if(fullNullCheck()) return;
        if(shader.getValue() != BGShader.none) {

            if (OpenGlHelper.shadersSupported && ClickGui.mc.getRenderViewEntity() instanceof EntityPlayer) {
                if (ClickGui.mc.entityRenderer.getShaderGroup() != null) {
                    ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
                try {
                    ClickGui.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/" + this.shader.getValue() + ".json"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ClickGui.mc.entityRenderer.getShaderGroup() != null && ClickGui.mc.currentScreen == null) {
                ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
    }*/

    public List<AnchorPoint> getAnchorPoints() {
        return anchorPoints;
    }



    public enum Mode {
        Frame,
        Folow,
        None
    }

    public enum Locate {
        Left,
        Right,
        Middle
    }

    public enum BGShader {
        none,
        notch,
        antialias,
        art,
        bits,
        blobs,
        blobs2,
        blur,
        bumpy,
        color_convolve,
        creeper,
        deconverge,
        desaturate,
        flip,
        fxaa,
        green,
        invert,
        ntsc,
        pencil,
        phosphor,
        sobel,
        spider,
        wobble;
    }

}

