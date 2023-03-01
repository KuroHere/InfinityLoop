package com.me.infinity.loop.features.modules.client;

import com.me.infinity.loop.event.events.render.PerspectiveEvent;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;
import com.me.infinity.loop.features.setting.Setting;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GameChanger
    extends Module {

    private static GameChanger INSTANCE = new GameChanger();
    public Setting<Boolean> modelFlip = this.register(new Setting<>("ModelFlip", false));
    public Setting<Boolean> aspect = this.register(new Setting<>("Aspect", false));
    public final Setting<Double> aspectValue = this.register(new Setting<>("AspectValue", mc.displayWidth / mc.displayHeight + 0.0, 0.0, 3.0,v -> this.aspect.getValue()));
    public Setting<Boolean> customFov = this.register(new Setting<>("CustomFov", false));
    public final Setting<Float> fov = this.register(new Setting<>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f), v -> customFov.getValue()));
    public Setting<Boolean> customGamma = this.register(new Setting<>("CustomGamma", false));
    public final Setting<Float> gamma = this.register(new Setting<>("Gamma", Float.valueOf(1000.0f), Float.valueOf(1.0f), Float.valueOf(1000.0f), v -> customGamma.getValue()));
    public Setting<Boolean> customAmbience = this.register(new Setting<>("CustomAmbience", false));
    public final Setting<Integer> ambienceRed = this.register(new Setting<>("A-Red",255,0,255, v -> customAmbience.getValue()));
    public final Setting<Integer> ambienceGreen = this.register(new Setting<>("A-Green",255,0,255, v -> customAmbience.getValue()));
    public final Setting<Integer> ambienceBlue = this.register(new Setting<>("A-Blue",255,0,255, v -> customAmbience.getValue()));
    public final Setting<Integer> ambienceAlpha = this.register(new Setting<>("A-Alpha",255,0,255, v -> customAmbience.getValue()));

    public GameChanger() {
        super("GameChanger", "Change Some Minecraft Parameter", ModuleCategory.CLIENT);
        this.setInstance();
    }

    public static GameChanger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameChanger();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.customFov.getValue().booleanValue()) {
            this.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue().floatValue());
        }
        if (this.customGamma.getValue().booleanValue()) {
            this.mc.gameSettings.setOptionFloatValue(GameSettings.Options.GAMMA, this.gamma.getValue().floatValue());
        }
    }

    @Override
    public void onDisable() {
        mc.entityRenderer.stopUseShader();
    }

    @SubscribeEvent
    public void onPerspectiveEvent(final PerspectiveEvent perspectiveEvent) {
        if (GameChanger.getInstance().aspect.getValue()) {
            perspectiveEvent.setAspect(this.aspectValue.getValue().floatValue());
        }
    }

}
