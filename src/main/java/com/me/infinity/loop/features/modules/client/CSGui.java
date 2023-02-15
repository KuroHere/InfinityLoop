package com.me.infinity.loop.features.modules.client;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.event.events.ClientEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.me.infinity.loop.features.command.Command;
import com.me.infinity.loop.features.csgui.CSClickGui;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.setting.Setting;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CSGui
        extends Module {
    private static CSGui INSTANCE = new CSGui();
    public Setting<String> prefix = this.register(new Setting<>("Prefix", "."));
    public Setting<Boolean> description= this.register(new Setting("Description", true));
    public Setting<Boolean> bindText = register(new Setting("ShowBind",false));
    public Setting<Boolean> openCloseChange = register(new Setting("Open/Close",true));
    public Setting<String> moduleButton = register(new Setting("Buttons: ", " ", v -> !this.openCloseChange.getValue()).setRenderName(true));
    public Setting<String> open = register(new Setting("Open: ", "+", v -> this.openCloseChange.getValue()).setRenderName(true));
    public Setting<String> close = register(new Setting("Close: ", "-", v -> this.openCloseChange.getValue()).setRenderName(true));
    public Setting<Boolean> background= this.register(new Setting("Background", true));
    public Setting<Boolean> blur = this.register(new Setting("Blur", false, v -> this.background.getValue()));
    public Setting<Integer> blurAmount = this.register(new Setting<>("BlurAmount", 2, 0, 20, v -> this.background.getValue()));
    public Setting<Integer> blurSize = this.register(new Setting<>("BlurSize", 0, 0, 20, v -> this.background.getValue()));
    public Setting<Boolean> dark = this.register(new Setting("Dark", false, v -> this.background.getValue()));
    public Setting<Boolean> gradiant = this.register(new Setting("Gradiant", false, v -> this.background.getValue()));
    public Setting<Integer> gradiantHeight = this.register(new Setting("GradiantHeight", 0, 0, 255, v -> this.background.getValue()));
    public Setting<Boolean> customFov = this.register(new Setting<Boolean>("CustomFov", false));
    public Setting<Float> fov = this.register(new Setting<>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f)));
    public Setting<Boolean> colorSync = this.register(new Setting<>("ColorSync", false));
    public Setting<Integer> red = this.register(new Setting<>("Red", 230, 0, 255));
    public Setting<Integer> green = this.register(new Setting<>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<>("Blue", 0, 0, 255));
    public Setting<Integer> hoverAlpha = this.register(new Setting<>("Alpha", 170, 0, 255));
    public Setting<Integer> topRed = this.register(new Setting<>("SecondRed", 230, 0, 255));
    public Setting<Integer> topGreen = this.register(new Setting<>("SecondGreen", 0, 0, 255));
    public Setting<Integer> topBlue = this.register(new Setting<>("SecondBlue", 0, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> rainbow = this.register(new Setting<>("Rainbow", false));
    public Setting<rainbowMode> rainbowModeHud = this.register(new Setting<Object>("HRainbowMode", rainbowMode.Static, v -> this.rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = this.register(new Setting<Object>("ARainbowMode", rainbowModeArray.Static, v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowHue = this.register(new Setting<Object>("Delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue()));
    public Setting<Float> rainbowBrightness = this.register(new Setting<Object>("Brightness ", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue()));
    public Setting<Float> rainbowSaturation = this.register(new Setting<Object>("Saturation", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue()));
    private CSClickGui click;

    public CSGui() {
        super("CSGui", "Opens the CSGui", Module.Category.CLIENT, true, false, false);
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
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                InfinityLoop.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + InfinityLoop.commandManager.getPrefix());
            }
            InfinityLoop.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new CSClickGui());
    }

    @Override
    public void onLoad() {
        if (this.colorSync.getValue()) {
            InfinityLoop.colorManager.setColor(Colors.getInstance().getCurrentColor().getRed(), Colors.getInstance().getCurrentColor().getGreen(), Colors.getInstance().getCurrentColor().getBlue(), this.alpha.getValue());
        } else {
            InfinityLoop.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        }
        InfinityLoop.commandManager.setPrefix(this.prefix.getValue());
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

    public enum rainbowMode {
        Static,
        Sideway

    }
}


