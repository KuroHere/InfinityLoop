package me.loop.mods.modules.impl.client.ClickGui;

import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.managers.Managers;
import me.loop.mods.gui.InfinityLoopGui;
import me.loop.mods.gui.screen.anchor.AnchorPoint;
import me.loop.mods.modules.Category;
import me.loop.mods.modules.Module;
import me.loop.mods.modules.impl.client.Colors;
import me.loop.mods.modules.impl.movement.NoSlow;
import me.loop.mods.settings.Setting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static me.loop.mods.modules.impl.movement.NoSlow.keys;

public class ClickGui
        extends Module {

    public static ClickGui INSTANCE;

    public Setting<Enum> setting = this.add(new Setting("Page", ClickEnum.Settings.Main));

    public Setting<Boolean> moduleiconmode = add(new Setting("ModuleIcon",true, v -> this.setting.getValue() == ClickEnum.Settings.Main));
    public Setting<Enum> butonIcon = add(new Setting("ButtonIcon", ClickEnum.Style.OpenColse, v -> this.setting.getValue() == ClickEnum.Settings.Main));
    public Setting<String> open = add(new Setting("Open: ", "+", v -> this.setting.getValue() == ClickEnum.Settings.Main && this.butonIcon.getValue() == ClickEnum.Style.OpenColse).setRenderName(true));
    public Setting<String> close = add(new Setting("Close: ", "-", v -> this.setting.getValue() == ClickEnum.Settings.Main && this.butonIcon.getValue() == ClickEnum.Style.OpenColse).setRenderName(true));

    // MISC
    public Setting<Boolean> scroll = this.add(new Setting<>("Scroll", true, v -> this.setting.getValue() == ClickEnum.Settings.Misc));
    public Setting<Integer> scrollval = this.add(new Setting<>("Scroll Speed", 10, 1, 30, v -> this.setting.getValue() == ClickEnum.Settings.Misc && this.scroll.getValue()));
    public Setting<Enum> description = this.add(new Setting("Description", ClickEnum.Mode.Frame, v -> this.setting.getValue() == ClickEnum.Settings.Misc));
    public Setting<Boolean> categoryTextCenter = this.add(new Setting<>("CategoryTextCenter", true, v -> this.setting.getValue() == ClickEnum.Settings.Misc));
    public Setting<Boolean> buttonTextCenter = this.add(new Setting<>("ButtonTextCenter", false, v -> this.setting.getValue() == ClickEnum.Settings.Misc));

    // COLORS
    public Setting<Boolean> sideSettings = this.add(new Setting<>("SideSettings", false, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> sideLineC = this.add(new Setting<>("SideLine", new Color(0xFFFFFF), v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> moduleMainC = this.add(new Setting<>("ModuleMainColor", new Color(0, 255, 102, 255), v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> moduleEnableC = this.add(new Setting<>("ModuleEnableColor", new Color(0, 255, 60, 77), v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Enum> sliderType = this.add(new Setting<>("SliderType", ClickEnum.SliderType.Line, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Color> sliderC = add(new Setting<>("SliderColor", new Color(0, 110, 42, 255), v -> this.setting.getValue() == ClickEnum.Settings.Color));

    public Setting<Integer> hoverAlpha = this.add(new Setting<>("Alpha", 170, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Boolean> colorSync = this.add(new Setting<>("ColorSync", false, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Boolean> outline = this.add(new Setting<>("Outline", false, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Boolean> shader = this.add(new Setting<>("Shader", true, v -> this.setting.getValue() == ClickEnum.Settings.Color));
    public Setting<Integer> shaderRadius = this.add(new Setting<Object>("ShaderRadius", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(10), v -> this.setting.getValue() == ClickEnum.Settings.Color && this.shader.getValue()));
    public Setting<Color> shaderC = add(new Setting<>("ShaderColor", new Color(0, 229, 100, 255), v -> this.setting.getValue() == ClickEnum.Settings.Color && this.shader.getValue()));
    public Setting<Float> olWidth = this.add(new Setting("Outline Width", 0.0f, 1f, 5f, v -> this.setting.getValue() == ClickEnum.Settings.Main && outline.getValue()));
    public Setting<Integer> alpha = this.add(new Setting<>("HoverAlpha", 240, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.Main));

    // BACKGROUND
    public Setting<Boolean> particles = this.add(new Setting("Particles", false, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Integer> particleLength = this.add(new Setting<>("ParticlesLength", 80, 0, 300, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.particles.getValue()));
    public Setting<Boolean> blur = this.add(new Setting("Blur", false, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Integer> blurAmount = this.add(new Setting<>("BlurAmount", 2, 0, 20, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.blur.getValue()));
    public Setting<Integer> blurSize = this.add(new Setting<>("BlurSize", 0, 0, 20, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.blur.getValue()));
    public Setting<Boolean> dirt = this.add(new Setting<>("Dirt", false, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Boolean> dark = this.add(new Setting("Dark", true, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Boolean> gradiant = this.add(new Setting("Gradiant", false, v -> this.setting.getValue() == ClickEnum.Settings.BackGround));
    public Setting<Integer> gradiantHeight = this.add(new Setting("GradiantHeight", 0, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.gradiant.getValue()));
    public Setting<Integer> gradiantAlpha = this.add(new Setting<>("GradiantAlpha", 230, 0, 255, v -> this.setting.getValue() == ClickEnum.Settings.BackGround && this.gradiant.getValue()));
    private List<AnchorPoint> anchorPoints = new ArrayList<>();
    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Category.CLIENT);
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        Managers.colorManager.setColor(this.moduleMainC.getPlannedValue().getRed(), this.moduleMainC.getPlannedValue().getGreen(), this.moduleMainC.getPlannedValue().getBlue(), this.moduleMainC.getPlannedValue().getAlpha());
    }

    @Override
    public void onEnable() {
        if (mc.world != null) {
            mc.displayGuiScreen(InfinityLoopGui.INSTANCE);
        }
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
    public void onUpdate() {
        if (NoSlow.getInstance().guiMove.getValue()&& !(mc.currentScreen instanceof GuiChat)) {

            for (KeyBinding key : keys) {
                KeyBinding.setKeyBindState(key.getKeyCode(), Keyboard.isKeyDown(key.getKeyCode()));
            }

        } else {

            for (KeyBinding key : keys) {

                if (!Keyboard.isKeyDown(key.getKeyCode())) {
                    KeyBinding.setKeyBindState(key.getKeyCode(), false);
                }
            }
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

