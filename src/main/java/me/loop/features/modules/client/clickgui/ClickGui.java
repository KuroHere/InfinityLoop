package me.loop.features.modules.client.clickgui;

import me.loop.InfinityLoop;
import me.loop.event.events.ClientEvent;
import me.loop.features.gui.InfinityLoopGui;
import me.loop.features.gui.screen.anchor.AnchorPoint;
import me.loop.features.modules.Module;
import me.loop.features.modules.client.Colors;
import me.loop.features.setting.Setting;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGui
        extends Module {

    private static ClickGui INSTANCE = new ClickGui();

    public Setting<Enum> setting = this.register(new Setting("Page", ClickEnum.Settings.Main));

    public Setting<Boolean> moduleiconmode = register(new Setting("ModuleIcon",true, v -> this.setting.getValue() == ClickEnum.Settings.Main));
    public Setting<Enum> butonIcon = register(new Setting("ButtonIcon", ClickEnum.Style.OpenColse, v -> this.setting.getValue() == ClickEnum.Settings.Main));
    public Setting<String> open = register(new Setting("Open: ", "+", v -> this.setting.getValue() == ClickEnum.Settings.Main && this.butonIcon.getValue() == ClickEnum.Style.OpenColse).setRenderName(true));
    public Setting<String> close = register(new Setting("Close: ", "-", v -> this.setting.getValue() == ClickEnum.Settings.Main && this.butonIcon.getValue() == ClickEnum.Style.OpenColse).setRenderName(true));

    // MISC
    public Setting<Boolean> scroll = this.register(new Setting<>("Scroll", true, v -> this.setting.getValue() == ClickEnum.Settings.Misc));
    public Setting<Integer> scrollval = this.register(new Setting<>("Scroll Speed", 10, 1, 30, v -> this.setting.getValue() == ClickEnum.Settings.Misc && this.scroll.getValue()));
    public Setting<Enum> description = this.register(new Setting("Description", ClickEnum.Mode.Frame, v -> this.setting.getValue() == ClickEnum.Settings.Misc));
    public Setting<Boolean> categoryTextCenter = this.register(new Setting<>("CategoryTextCenter", true, v -> this.setting.getValue() == ClickEnum.Settings.Misc));
    public Setting<Boolean> buttonTextCenter = this.register(new Setting<>("ButtonTextCenter", false, v -> this.setting.getValue() == ClickEnum.Settings.Misc));

    // COLORS
    public Setting<Boolean> sideSettings = this.register(new Setting<>("SideSettings", false, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> sideLineC = this.register(new Setting<>("SideLine", new Color(0xFFFFFF), v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> moduleMainC = this.register(new Setting<>("ModuleMainColor", new Color(0, 255, 102, 255), v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> moduleEnableC = this.register(new Setting<>("ModuleEnableColor", new Color(0, 255, 60, 77), v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Enum> sliderType = this.register(new Setting<>("SliderType", ClickEnum.SliderType.Line, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> sliderC = register(new Setting<>("SliderColor", new Color(0, 110, 42, 255), v -> this.setting.getValue() == ClickEnum.Settings.Color));

    public Setting<Integer> hoverAlpha = this.register(new Setting<>("Alpha", 170, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Boolean> colorSync = this.register(new Setting<>("ColorSync", false, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Boolean> outline = this.register(new Setting<>("Outline", false, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Boolean> shader = this.register(new Setting<>("Shader", false, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Integer> shaderRadius = this.register(new Setting<Object>("ShaderRadius", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(10), v -> this.setting.getValue() == ClickEnum.Settings.Color && this.shader.getValue()));
    public Setting<Color> shaderC = register(new Setting<>("ShaderColor", new Color(0, 229, 100, 255), v -> this.setting.getValue() == ClickEnum.Settings.Color && this.shader.getValue()));
    public Setting<Float> olWidth = this.register(new Setting("Outline Width", 0.0f, 1f, 5f, v -> this.setting.getValue() == ClickEnum.Settings.Main && outline.getValue()));
    public Setting<Integer> alpha = this.register(new Setting<>("HoverAlpha", 240, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.Main));

    // BACKGROUND
    public Setting<Boolean> particles = this.register(new Setting("Particles", false, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Integer> particleLength = this.register(new Setting<>("ParticlesLength", 80, 0, 300, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.particles.getValue()));
    public Setting<Boolean> blur = this.register(new Setting("Blur", false, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Integer> blurAmount = this.register(new Setting<>("BlurAmount", 2, 0, 20, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.blur.getValue()));
    public Setting<Integer> blurSize = this.register(new Setting<>("BlurSize", 0, 0, 20, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.blur.getValue()));
    public Setting<Boolean> dirt = this.register(new Setting<>("Dirt", false, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Boolean> dark = this.register(new Setting("Dark", true, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Boolean> gradiant = this.register(new Setting("Gradiant", false, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Integer> gradiantHeight = this.register(new Setting("GradiantHeight", 0, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.gradiant.getValue()));
    public Setting<Integer> gradiantAlpha = this.register(new Setting<>("GradiantAlpha", 230, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.gradiant.getValue()));
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
        InfinityLoop.colorManager.setColor(this.moduleMainC.getPlannedValue().getRed(), this.moduleMainC.getPlannedValue().getGreen(), this.moduleMainC.getPlannedValue().getBlue(), this.moduleMainC.getPlannedValue().getAlpha());
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(InfinityLoopGui.getClickGui());
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen instanceof InfinityLoopGui) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    public void onLoad() {
        if (this.colorSync.getValue()) {
            InfinityLoop.colorManager.setColor(Colors.getInstance().getCurrentColor().getRed(), Colors.getInstance().getCurrentColor().getGreen(), Colors.getInstance().getCurrentColor().getBlue(), this.alpha.getValue());
        } else {
            InfinityLoop.colorManager.setColor(this.moduleMainC.getPlannedValue().getRed(), this.moduleMainC.getPlannedValue().getGreen(), this.moduleMainC.getPlannedValue().getBlue(), this.moduleMainC.getPlannedValue().getAlpha());
        }
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof InfinityLoopGui) && !(mc.currentScreen instanceof GuiMainMenu)) {
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

