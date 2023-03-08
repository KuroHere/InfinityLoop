package me.loop.client.modules.impl.client.ClickGui;

import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.managers.Managers;
import me.loop.client.gui.InfinityLoopGui;
import me.loop.client.gui.screen.anchor.AnchorPoint;
import me.loop.client.modules.Module;
import me.loop.client.modules.Category;
import me.loop.client.modules.impl.client.Colors;
import me.loop.client.modules.settings.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGui
        extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public Setting<Enum> description = this.add(new Setting("Description", ClickEnum.Mode.Frame));
    public Setting<Boolean> moduleiconmode = add(new Setting("ModuleIcon",true));
    //public Setting<Boolean> guiGlow= this.add(new Setting("Glow", true));
    //public Setting<Integer> glowOffset = this.add(new Setting<>("GlowOffset", 2, 0, 20, v -> this.guiGlow.getValue()));
    public Setting<Enum> butonIcon = add(new Setting("ButtonIcon", ClickEnum.Icon.OpenColse));
    public Setting<String> open = add(new Setting("Open: ", "+", v -> this.butonIcon.getValue() == ClickEnum.Icon.OpenColse).setRenderName(true));
    public Setting<String> close = add(new Setting("Close: ", "-", v -> this.butonIcon.getValue() == ClickEnum.Icon.OpenColse).setRenderName(true));
    public Setting<Boolean> background= this.add(new Setting("Background", true));
    public Setting<Boolean> particles = this.add(new Setting("Particles", true, v -> this.background.getValue()));
    public Setting<Integer> particleLength = this.add(new Setting<>("ParticlesLength", 80, 0, 300, v -> this.background.getValue() && this.particles.getValue()));
    //public Setting<BGShader> shader = this.add(new Setting("ShaderBG", BGShader.none, v -> this.background.getValue()));
    public Setting<Boolean> blur = this.add(new Setting("Blur", false, v -> this.background.getValue()));
    public Setting<Integer> blurAmount = this.add(new Setting<>("BlurAmount", 2, 0, 20, v -> this.background.getValue() && this.blur.getValue()));
    public Setting<Integer> blurSize = this.add(new Setting<>("BlurSize", 0, 0, 20, v -> this.background.getValue() && this.blur.getValue()));
    public Setting<Boolean> dirt = this.add(new Setting<Boolean>("Dirt", false));
    public Setting<Boolean> dark = this.add(new Setting("Dark", true, v -> this.background.getValue()));
    public Setting<Boolean> gradiant = this.add(new Setting("BG-Gradiant", false, v -> this.background.getValue()));
    public Setting<Integer> gradiantHeight = this.add(new Setting("GradiantHeight", 0, 0, 255, v -> this.background.getValue() && this.gradiant.getValue()));
    public Setting<Integer> gradiantAlpha = this.add(new Setting<>("GradiantAlpha", 230, 0, 255, v -> this.background.getValue() && this.gradiant.getValue()));
    public Setting<Boolean> grd = this.add(new Setting<>("ButtonGradiant", false));
    public Setting<Integer> red = this.add(new Setting<>("Red", 160, 0, 255));
    public Setting<Integer> green = this.add(new Setting<>("Green", 10, 0, 255));
    public Setting<Integer> blue = this.add(new Setting<>("Blue", 255, 0, 255));
    public Setting<Integer> hoverAlpha = this.add(new Setting<>("Alpha", 170, 0, 255));
    public Setting<Boolean> colorSync = this.add(new Setting<>("ColorSync", false));
    public Setting<Integer> sizeWidth = this.add(new Setting("Width", 20, 0, 50 ));
    public Setting<Float> olWidth = this.add(new Setting("Outline Width", 0.0f, 1f, 5f));
    public Setting<Integer> alpha = this.add(new Setting<>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> sideSettings = this.add(new Setting<>("SideSettings", false));
    public Setting<Color> sideLineC = this.add(new Setting<>("SideLine", new Color(0xFFFFFF)));

    private List<AnchorPoint> anchorPoints = new ArrayList<>();
    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Category.CLIENT, true, false, false);
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
        Managers.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new InfinityLoopGui());
    }

    @Override
    public void onLoad() {
        if (this.colorSync.getValue()) {
            Managers.colorManager.setColor(Colors.getInstance().getCurrentColor().getRed(), Colors.getInstance().getCurrentColor().getGreen(), Colors.getInstance().getCurrentColor().getBlue(), this.alpha.getValue());
        } else {
            Managers.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
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

