package infinityloop.features.modules.test;

import infinityloop.features.command.Command;
import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.setting.Setting;

import java.awt.*;

public class Test extends Module {

    public Setting<String> stringTest = this.register(new Setting<>("String", " "));
    public Setting<Boolean> booleanTest = this.register(new Setting<>("Boolean", true));
    public Setting<Boolean> sliderTest = this.register(new Setting<>("Slider", true));
    public Setting<Integer> intSliderTest = this.register(new Setting<>("IntSliderTest", 150, 0, 255, v -> this.sliderTest.getValue()));
    public Setting<Float> floatSliderTest = this.register(new Setting<>("FloatSlider", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.sliderTest.getValue()));
    public Setting<enumtesting> enumTest = this.register(new Setting<Object>("EnumTest", enumtesting.Test));
    public Setting<Color> color = this.register(new Setting<>("Color", new Color(0x8800FF00)));
    public Test() {
        super("Test", "description", ModuleCategory.RENDER);
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


