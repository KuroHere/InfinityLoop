package me.loop.client.modules.impl.test;

import me.loop.client.commands.Command;
import me.loop.client.modules.Module;
import me.loop.client.modules.Category;
import me.loop.client.modules.settings.Setting;

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
        super("Test", "description", Category.RENDER, true, false, false);
    }

    @Override
    public void onEnable() {
        Command.sendMessage("Just A TestPlace!!");
        this.disable();
    }
    public enum enumtesting {
        Test,
        Test1,
        Test2

    }
}


