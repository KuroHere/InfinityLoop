package me.loop.client.modules.impl.client;

import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.managers.Managers;
import me.loop.client.csgui.CSClickGui;
import me.loop.client.modules.Module;
import me.loop.client.modules.Category;
import me.loop.client.modules.settings.Setting;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CSGui
        extends Module {
    private static CSGui INSTANCE = new CSGui();
    public Setting<Boolean> openCloseChange = add(new Setting("Open/Close",true));
    public Setting<Boolean> background= this.add(new Setting("Background", true));
    public Setting<Boolean> blur = this.add(new Setting("Blur", false, v -> this.background.getValue()));
    public Setting<Integer> blurAmount = this.add(new Setting<>("BlurAmount", 2, 0, 20, v -> this.background.getValue()));
    public Setting<Integer> blurSize = this.add(new Setting<>("BlurSize", 0, 0, 20, v -> this.background.getValue()));
    public Setting<Boolean> dark = this.add(new Setting("Dark", false, v -> this.background.getValue()));
    public Setting<Boolean> gradiant = this.add(new Setting("Gradiant", false, v -> this.background.getValue()));
    public Setting<Integer> gradiantHeight = this.add(new Setting("GradiantHeight", 0, 0, 255, v -> this.background.getValue()));
    public Setting<Boolean> customFov = this.add(new Setting<Boolean>("CustomFov", false));
    public Setting<Float> fov = this.add(new Setting<>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f)));
    public Setting<Boolean> colorSync = this.add(new Setting<>("ColorSync", false));
    public Setting<Integer> red = this.add(new Setting<>("Red", 230, 0, 255));
    public Setting<Integer> green = this.add(new Setting<>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.add(new Setting<>("Blue", 0, 0, 255));
    public Setting<Integer> hoverAlpha = this.add(new Setting<>("Alpha", 170, 0, 255));
    public Setting<Integer> topRed = this.add(new Setting<>("SecondRed", 230, 0, 255));
    public Setting<Integer> topGreen = this.add(new Setting<>("SecondGreen", 0, 0, 255));
    public Setting<Integer> topBlue = this.add(new Setting<>("SecondBlue", 0, 0, 255));
    public Setting<Integer> alpha = this.add(new Setting<>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> rainbow = this.add(new Setting<>("Rainbow", false));
    public Setting<rainbowModeArray> rainbowModeA = this.add(new Setting<Object>("ARainbowMode", rainbowModeArray.Static, v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowHue = this.add(new Setting<Object>("Delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue()));
    private CSClickGui click;

    public CSGui() {
        super("CSGui", "Opens the CSGui", Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static CSGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CSGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.customFov.getValue().booleanValue()) {
            CSGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue().floatValue());
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        Managers.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
    }


    @Override
    public void onEnable() {
        mc.displayGuiScreen(new CSClickGui());
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
        if (!(CSGui.mc.currentScreen instanceof CSClickGui)) {
            this.disable();
        }
    }

    public enum rainbowModeArray {
        Static,
        Up

    }
}


