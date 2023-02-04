package com.me.infinity.loop.features.modules.test;

import com.me.infinity.loop.features.command.Command;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.setting.ColorSetting;
import com.me.infinity.loop.features.setting.Setting;

public class Test extends Module {

    public Setting<String> stringTest = this.register(new Setting<>("String", " "));
    public Setting<Boolean> booleanTest = this.register(new Setting<>("Boolean", true));
    public Setting<Boolean> sliderTest = this.register(new Setting<>("Slider", true));
    public Setting<Integer> intSliderTest = this.register(new Setting<>("IntSliderTest", 150, 0, 255, v -> this.sliderTest.getValue()));
    public Setting<Float> floatSliderTest = this.register(new Setting<>("FloatSlider", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.sliderTest.getValue()));
    public Setting<enumtesting> enumTest = this.register(new Setting<Object>("EnumTest", enumtesting.Test));
    public Setting<ColorSetting> color = this.register(new Setting<>("Color", new ColorSetting(0x8800FF00)));
    public Test() {
        super("Test", "description", Category.TEST, false, false, false);
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


