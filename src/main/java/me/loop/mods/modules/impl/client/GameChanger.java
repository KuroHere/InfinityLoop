package me.loop.mods.modules.impl.client;

import me.loop.api.events.impl.render.PerspectiveEvent;
import me.loop.mods.modules.Module;
import me.loop.mods.modules.Category;
import me.loop.mods.settings.Setting;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GameChanger
    extends Module {

    private static GameChanger INSTANCE = new GameChanger();
    public Setting<Boolean> modelFlip = this.add(new Setting<>("ModelFlip", false));
    public Setting<Boolean> aspect = this.add(new Setting<>("Aspect", false));
    public final Setting<Double> aspectValue = this.add(new Setting<>("AspectValue", mc.displayWidth / mc.displayHeight + 0.0, 0.0, 3.0,v -> this.aspect.getValue()));
    public Setting<Boolean> customFov = this.add(new Setting<>("CustomFov", false));
    public final Setting<Float> fov = this.add(new Setting<>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f), v -> customFov.getValue()));
    public Setting<Boolean> customGamma = this.add(new Setting<>("CustomGamma", false));
    public final Setting<Float> gamma = this.add(new Setting<>("Gamma", Float.valueOf(1000.0f), Float.valueOf(1.0f), Float.valueOf(1000.0f), v -> customGamma.getValue()));
    public Setting<Boolean> customAmbience = this.add(new Setting<>("CustomAmbience", false));
    public final Setting<Integer> ambienceRed = this.add(new Setting<>("A-Red",255,0,255, v -> customAmbience.getValue()));
    public final Setting<Integer> ambienceGreen = this.add(new Setting<>("A-Green",255,0,255, v -> customAmbience.getValue()));
    public final Setting<Integer> ambienceBlue = this.add(new Setting<>("A-Blue",255,0,255, v -> customAmbience.getValue()));
    public final Setting<Integer> ambienceAlpha = this.add(new Setting<>("A-Alpha",255,0,255, v -> customAmbience.getValue()));

    public GameChanger() {
        super("GameChanger", "Change Some Minecraft Parameter", Category.CLIENT, true, false);
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
