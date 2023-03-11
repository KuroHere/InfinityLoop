package me.loop.feature.modules.impl.client.ClickGui;

import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.managers.Managers;
import me.loop.feature.gui.InfinityLoopGui;
import me.loop.feature.gui.screen.anchor.AnchorPoint;
import me.loop.feature.modules.Category;
import me.loop.feature.modules.Module;
import me.loop.feature.modules.impl.client.Colors;
import me.loop.feature.modules.settings.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGui
        extends Module {
    private static ClickGui INSTANCE = new ClickGui();

    public Setting<Enum> setting = this.add(new Setting("Page", ClickEnum.Settings.Main));

    public Setting<Boolean> moduleiconmode = add(new Setting("ModuleIcon",true, v -> this.setting.getValue() == ClickEnum.Settings.Main));
    public Setting<Enum> butonIcon = add(new Setting("ButtonIcon", ClickEnum.Icon.OpenColse, v -> this.setting.getValue() == ClickEnum.Settings.Main));
    public Setting<String> open = add(new Setting("Open: ", "+", v -> this.setting.getValue() == ClickEnum.Settings.Main && this.butonIcon.getValue() == ClickEnum.Icon.OpenColse).setRenderName(true));
    public Setting<String> close = add(new Setting("Close: ", "-", v -> this.setting.getValue() == ClickEnum.Settings.Main && this.butonIcon.getValue() == ClickEnum.Icon.OpenColse).setRenderName(true));

    // MISC
    public Setting<Enum> description = this.add(new Setting("Description", ClickEnum.Mode.Frame, v -> this.setting.getValue() == ClickEnum.Settings.Misc));
    public Setting<Boolean> categoryTextCenter = this.add(new Setting<Boolean>("CategoryTextCenter", true, v -> this.setting.getValue() == ClickEnum.Settings.Misc));
    public Setting<Boolean> buttonTextCenter = this.add(new Setting<Boolean>("ButtonTextCenter", false, v -> this.setting.getValue() == ClickEnum.Settings.Misc));

    // COLORS
    public Setting<Boolean> sideSettings = this.add(new Setting<>("SideSettings", false, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> sideLineC = this.add(new Setting<>("SideLine", new Color(0xFFFFFF), v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> moduleMainC = this.add(new Setting<Color>("ModuleMainColor", new Color(0, 255, 102, 255), v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> moduleEnableC = this.add(new Setting<Color>("ModuleEnableColor", new Color(0, 255, 60, 77), v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Enum> sliderType = this.add(new Setting<>("SliderType", ClickEnum.SliderType.Line, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> sliderC = add(new Setting<>("SliderColor", new Color(0, 110, 42, 255), v -> this.setting.getValue() == ClickEnum.Settings.Color));

    public Setting<Integer> hoverAlpha = this.add(new Setting<>("Alpha", 170, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Boolean> colorSync = this.add(new Setting<>("ColorSync", false, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Boolean> outline = this.add(new Setting<>("Outline", false, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Boolean> shader = this.add(new Setting<Boolean>("Shader", true, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Integer> shaderRadius = this.add(new Setting<Object>("ShaderRadius", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(10), v -> this.setting.getValue() == ClickEnum.Settings.Color && this.shader.getValue()));
    public Setting<Color> shaderC = add(new Setting<Color>("ShaderColor", new Color(0, 229, 100, 255), v -> this.setting.getValue() == ClickEnum.Settings.Color && this.shader.getValue()));
    public Setting<Float> olWidth = this.add(new Setting("Outline Width", 0.0f, 1f, 5f, v -> this.setting.getValue() == ClickEnum.Settings.Main && outline.getValue()));
    public Setting<Integer> alpha = this.add(new Setting<>("HoverAlpha", 240, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.Main));

    // BACKGROUND
    public Setting<Boolean> particles = this.add(new Setting("Particles", true, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Integer> particleLength = this.add(new Setting<>("ParticlesLength", 80, 0, 300, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.particles.getValue()));
    public Setting<Boolean> blur = this.add(new Setting("Blur", false, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Integer> blurAmount = this.add(new Setting<>("BlurAmount", 2, 0, 20, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.blur.getValue()));
    public Setting<Integer> blurSize = this.add(new Setting<>("BlurSize", 0, 0, 20, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.blur.getValue()));
    public Setting<Boolean> dirt = this.add(new Setting<Boolean>("Dirt", false, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Boolean> dark = this.add(new Setting("Dark", true, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Boolean> gradiant = this.add(new Setting("BG-Gradiant", false, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Integer> gradiantHeight = this.add(new Setting("GradiantHeight", 0, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.gradiant.getValue()));
    public Setting<Integer> gradiantAlpha = this.add(new Setting<>("GradiantAlpha", 230, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.gradiant.getValue()));
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
        Managers.colorManager.setColor(this.moduleMainC.getPlannedValue().getRed(), this.moduleMainC.getPlannedValue().getGreen(), this.moduleMainC.getPlannedValue().getBlue(), this.moduleMainC.getPlannedValue().getAlpha());
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(InfinityLoopGui.getClickGui());
    }

    @Override
    public void onLoad() {
        if (this.colorSync.getValue()) {
            Managers.colorManager.setColor(Colors.getInstance().getCurrentColor().getRed(), Colors.getInstance().getCurrentColor().getGreen(), Colors.getInstance().getCurrentColor().getBlue(), this.alpha.getValue());
        } else {
            Managers.colorManager.setColor(this.moduleMainC.getPlannedValue().getRed(), this.moduleMainC.getPlannedValue().getGreen(), this.moduleMainC.getPlannedValue().getBlue(), this.moduleMainC.getPlannedValue().getAlpha());
        }
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof InfinityLoopGui)) {
            this.disable();
        }
    }

    public float getOutlineWidth() {
        if (outline.getValue()) {
            return olWidth.getValue();
        }
        return 0;
    }

    public List<AnchorPoint> getAnchorPoints() {
        return anchorPoints;
    }

}

