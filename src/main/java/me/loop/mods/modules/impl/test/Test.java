package me.loop.mods.modules.impl.test;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.mods.commands.Command;
import me.loop.mods.modules.Module;
import me.loop.mods.modules.Category;
import me.loop.mods.settings.Setting;

import java.awt.*;

public class Test extends Module {

    public Setting<String> stringTest = this.add(new Setting<>("String", " "));
    public Setting<Boolean> booleanTest = this.add(new Setting<>("Boolean", true));
    public Setting<Boolean> sliderTest = this.add(new Setting<>("Slider", true));
    public Setting<Integer> intSliderTest = this.add(new Setting<>("IntSliderTest", 150, 0, 255, v -> this.sliderTest.getValue()));
    public Setting<Float> floatSliderTest = this.add(new Setting<>("FloatSlider", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.sliderTest.getValue()));
    public Setting<enumtesting> enumTest = this.add(new Setting<Object>("EnumTest", enumtesting.Test));
    public Setting<Color> color = this.add(new Setting<>("Color", new Color(0x8800FF00)));
    public Test() {
        super("Test", "description", Category.TEST, true, false);
    }

    @Override
    public void onEnable() {
        Command.sendMessage(ChatFormatting.AQUA + "Just A TestPlace!!");
        this.disable();
    }
    public enum enumtesting {
        Test,
        Test1,
        Test2

    }
}


