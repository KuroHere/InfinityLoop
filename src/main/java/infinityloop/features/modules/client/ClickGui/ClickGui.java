package infinityloop.features.modules.client.ClickGui;

import infinityloop.InfinityLoop;
import infinityloop.event.events.client.ClientEvent;
import infinityloop.features.gui.InfinityLoopGui;
import infinityloop.features.gui.screen.anchor.AnchorPoint;
import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.modules.client.Colors;
import infinityloop.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGui
        extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public Setting<Enum> description = this.register(new Setting("Description", ClickEnum.Mode.Frame));
    public Setting<Boolean> moduleiconmode = register(new Setting("ModuleIcon",true));
    //public Setting<Boolean> guiGlow= this.register(new Setting("Glow", true));
    //public Setting<Integer> glowOffset = this.register(new Setting<>("GlowOffset", 2, 0, 20, v -> this.guiGlow.getValue()));
    public Setting<Enum> butonIcon = register(new Setting("ButtonIcon", ClickEnum.Icon.OpenColse));
    public Setting<String> open = register(new Setting("Open: ", "+", v -> this.butonIcon.getValue() == ClickEnum.Icon.OpenColse).setRenderName(true));
    public Setting<String> close = register(new Setting("Close: ", "-", v -> this.butonIcon.getValue() == ClickEnum.Icon.OpenColse).setRenderName(true));
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
    public Setting<Integer> sizeWidth = this.register(new Setting("Width", 20, 0, 50 ));
    public Setting<Float> olWidth = this.register(new Setting("Outline Width", 0.0f, 1f, 5f));
    public Setting<Integer> alpha = this.register(new Setting<>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> sideSettings = this.register(new Setting<>("SideSettings", false));
    public Setting<Color> sideLineC = this.register(new Setting<>("SideLine", new Color(0xFFFFFF)));

    private List<AnchorPoint> anchorPoints = new ArrayList<>();
    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", ModuleCategory.CLIENT);
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


    public float getOutlineWidth() {
        return olWidth.getValue();
    }

    public float getSizeWidth() {
        return sizeWidth.getValue();
    }


    /*@Override
    public void onUpdate() {
        if (ClickGui.mc.world == null) return;
        if (!(ClickGui.getInstance().blur.getValue() || ClickGui.mc.currentScreen instanceof GuiContainer || ClickGui.mc.currentScreen instanceof GuiChat || ClickGui.mc.currentScreen instanceof GuiConfirmOpenLink)) {
            if (!(ClickGui.mc.currentScreen instanceof GuiEditSign)) {
                if (!(ClickGui.mc.currentScreen instanceof GuiGameOver)) {
                    if (!(ClickGui.mc.currentScreen instanceof GuiOptions)) {
                        if (!(ClickGui.mc.currentScreen instanceof GuiIngameMenu)) {
                            if (!(ClickGui.mc.currentScreen instanceof GuiVideoSettings)) {
                                if (!(ClickGui.mc.currentScreen instanceof GuiScreenOptionsSounds)) {
                                    if (!(ClickGui.mc.currentScreen instanceof GuiControls)) {
                                        if (!(ClickGui.mc.currentScreen instanceof GuiCustomizeSkin)) {
                                            if (!(ClickGui.mc.currentScreen instanceof GuiModList)) {
                                                if (ClickGui.mc.entityRenderer.getShaderGroup() == null) return;
                                                ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (ClickGui.mc.entityRenderer.getShaderGroup() != null) {
                ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            try {
                ClickGui.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                return;
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (ClickGui.mc.entityRenderer.getShaderGroup() == null) return;
        if (ClickGui.mc.entityRenderer != null) return;
        ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
    }

    @Override
    public void onDisable() {
        if (ClickGui.mc.entityRenderer.getShaderGroup() != null) {
            ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override
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

}

