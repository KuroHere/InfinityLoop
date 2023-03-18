package infinityloop.features.modules.render;

import infinityloop.event.events.player.TransformFirstPersonEvent;
import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.setting.Setting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.*;
import net.minecraft.client.renderer.*;

import java.util.function.Predicate;

public class ViewModel extends Module {
    public final Setting<Boolean> leftPosition = this.register(new Setting<>("LeftPosition", true));
    public Setting<Float> leftX = this.register(new Setting<Float>("LeftX", Float.valueOf(2.0f), Float.valueOf(0.0f), Float.valueOf(2.0f), v -> this.leftPosition.getValue()));
    public Setting<Float> leftY = this.register(new Setting<Float>("LeftY", Float.valueOf(2.0f), Float.valueOf(0.0f), Float.valueOf(2.0f), v -> this.leftPosition.getValue()));
    public Setting<Float> leftZ = this.register(new Setting<Float>("LeftZ", Float.valueOf(2.0f), Float.valueOf(0.0f), Float.valueOf(2.0f), v -> this.leftPosition.getValue()));
    public final Setting<Boolean> rightPosition = this.register(new Setting<>("RightPosition", true));
    public final Setting<Float> rightX = this.register(new Setting<Float>("rightX", Float.valueOf(2.0f), Float.valueOf(0.0f), Float.valueOf(2.0f), v -> this.rightPosition.getValue()));
    public final Setting<Float> rightY = this.register(new Setting<Float>("rightY", Float.valueOf(2.0f), Float.valueOf(0.0f), Float.valueOf(2.0f), v -> this.rightPosition.getValue()));
    public final Setting<Float> rightZ = this.register(new Setting<Float>("RightZ", Float.valueOf(2.0f), Float.valueOf(0.0f), Float.valueOf(2.0f), v -> this.rightPosition.getValue()));
    public final Setting<Boolean> leftRotation = this.register(new Setting("LeftRotation", true));
    public final Setting<Float> leftYaw = this.register(new Setting("Yaw", Float.valueOf(-100.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), v -> this.leftRotation.getValue()));
    public final Setting<Float> leftPitch = this.register(new Setting("Pitch", Float.valueOf(-100.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), v -> this.leftRotation.getValue()));
    public final Setting<Float> leftRoll = this.register(new Setting("Roll", Float.valueOf(-100.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), v -> this.leftRotation.getValue()));
    public final Setting<Boolean> rightRotation = this.register(new Setting("RightRotation", true));
    public final Setting<Float> rightYaw = this.register(new Setting("Yaw", Float.valueOf(-100.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), v -> this.rightRotation.getValue()));
    public final Setting<Float> rightPitch = this.register(new Setting("Pitch", Float.valueOf(-100.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), v -> this.rightRotation.getValue()));
    public final Setting<Float> rightRoll = this.register(new Setting("Roll", Float.valueOf(-100.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), v -> this.rightRotation.getValue()));
    public final Setting<Boolean> Animation = this.register(new Setting("Animation", true));
    public final Setting<Boolean> leftAnimation = this.register(new Setting("LeftAnimation", true, v -> this.Animation.getValue()));
    public final Setting<Float> leftAnimationSpeed = this.register(new Setting("AnimationSpeed", Float.valueOf(1.0f), Float.valueOf(5.0f), Float.valueOf(10.0f), v -> this.Animation.getValue() && this.leftAnimation.getValue()));
    public final Setting<Boolean> rightAnimation = this.register(new Setting("RightAnimation", true, v -> this.Animation.getValue()));
    public final Setting<Float> rightAnimationSpeed = this.register(new Setting("AnimationSpeed", Float.valueOf(1.0f), Float.valueOf(5.0f), Float.valueOf(10.0f), v -> this.Animation.getValue() && this.rightAnimation.getValue()));
    public final Setting<Boolean> customfov = this.register(new Setting("CustomFOV", true));
    public final Setting<Float> fov = this.register(new Setting("FOV", Float.valueOf(110.0f), Float.valueOf(130.0f), Float.valueOf(170.0f), v -> this.customfov.getValue()));
    public final Setting<Boolean> cancelEating = this.register(new Setting("CancelEating", true));

    private static ViewModel INSTANCE = new ViewModel();
    @EventHandler
    private Listener<TransformFirstPersonEvent> listener;
    @EventHandler
    private Listener<TransformFirstPersonEvent> listener2;

    public ViewModel() {
        super("Viewmodel", "Changes to the size and positions of your hands.", ModuleCategory.RENDER);
        this.setInstance();

    }
    public void onRender3D(Listener l) {
        this.listener = new Listener<TransformFirstPersonEvent>(event -> {
            if (nullCheck()) {
            } else {
                if (ViewModel.getINSTANCE().leftPosition.getValue() && event.getEnumHandSide() == EnumHandSide.LEFT) {
                    GlStateManager.translate(ViewModel.getINSTANCE().leftX.getValue(), ViewModel.getINSTANCE().leftY.getValue(), ViewModel.getINSTANCE().leftZ.getValue());
                }
                if (ViewModel.getINSTANCE().rightPosition.getValue() && event.getEnumHandSide() == EnumHandSide.RIGHT) {
                    GlStateManager.translate(ViewModel.getINSTANCE().rightX.getValue(), ViewModel.getINSTANCE().rightY.getValue(), ViewModel.getINSTANCE().rightZ.getValue());
                }
            }
        }, new Predicate[0]);
        this.listener2 = new Listener<TransformFirstPersonEvent>(event -> {
            if (!nullCheck()) {
                if (ViewModel.getINSTANCE().leftRotation.getValue() && event.getEnumHandSide() == EnumHandSide.LEFT) {
                    GlStateManager.rotate(ViewModel.getINSTANCE().leftYaw.getValue(), 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(ViewModel.getINSTANCE().leftPitch.getValue(), 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(ViewModel.getINSTANCE().leftRoll.getValue(), 0.0f, 0.0f, 1.0f);
                }
                if (ViewModel.getINSTANCE().rightRotation.getValue() && event.getEnumHandSide() == EnumHandSide.RIGHT) {
                    GlStateManager.rotate(ViewModel.getINSTANCE().rightYaw.getValue(), 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(ViewModel.getINSTANCE().rightPitch.getValue(), 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(ViewModel.getINSTANCE().rightRoll.getValue(), 0.0f, 0.0f, 1.0f);
                }
            }
        }, new Predicate[0]);
    }


    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (ViewModel.getINSTANCE().leftAnimation.getValue()) {
            ViewModel.getINSTANCE().leftYaw.setValue((float) (ViewModel.getINSTANCE().leftYaw.getValue() + ViewModel.getINSTANCE().leftAnimationSpeed.getValue() % 360.0));
        }
        if (ViewModel.getINSTANCE().rightAnimation.getValue()) {
            ViewModel.getINSTANCE().rightYaw.setValue((float) (ViewModel.getINSTANCE().rightYaw.getValue() + ViewModel.getINSTANCE().rightAnimationSpeed.getValue() % 360.0));
        }
        if (ViewModel.getINSTANCE().customfov.getValue().booleanValue()) {
            this.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue().floatValue());
        }
    }


    private void setInstance() {
        INSTANCE = this;
    }

    public static ViewModel getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ViewModel();
        }
        return INSTANCE;
    }
}
