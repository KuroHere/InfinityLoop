package me.loop.feature.modules.impl.player;

import me.loop.api.events.impl.network.MoveEvent;
import me.loop.feature.modules.Module;
import me.loop.feature.modules.Category;
import me.loop.feature.modules.settings.Setting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoDoSmth extends Module {

    private static AutoDoSmth INSTANCE = new AutoDoSmth();
    public Setting<Sprint> autoSprint = this.add(new Setting<>("AutoSprint", Sprint.Legit));
    public Setting<Boolean> autoWalk = this.add(new Setting<>("AutoWalk", false));
    public Setting<Boolean> autoSneak = this.add(new Setting<>("AutoSneak", false));
    public Setting<Boolean> autoJump = this.add(new Setting<>("AutoJump", false));
    public Setting<Boolean> autoSpin = this.add(new Setting<>("AutoSpin", false));
    // yaw
    public Setting<Boolean> yawAnimation = this.add(new Setting("YawAnimation", false , v -> this.autoSpin.getValue()));
    public final Setting<Float> yaw = this.add(new Setting("Yaw",  Float.valueOf(1.0f), Float.valueOf(-10.0f), Float.valueOf(10.0f), v -> this.autoSpin.getValue() && yawAnimation.getValue()));
    // pitch
    public Setting<Boolean> pitchAnimation = this.add(new Setting("PitchAnimation", false , v -> this.autoSpin.getValue()));
    public final Setting<Float> pitch = this.add(new Setting("Pitch", Float.valueOf(1.0f), Float.valueOf(-10.0f), Float.valueOf(10.0f), v -> this.autoSpin.getValue() && pitchAnimation.getValue()));

    public AutoDoSmth() {
        super("AutoDoSmth", "I dont know why I'm made that", Category.PLAYER, true, false, true);
        this.setInstance();
    }

    public static AutoDoSmth getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AutoDoSmth();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSprint(MoveEvent event) {
        if (event.getStage() == 0 && this.autoSprint.getValue() == Sprint.Rage && (mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f)) {
            event.setCanceled(true);
        }
    }

    public void onUpdate() {
        if (!nullCheck()) {
        }
        if (autoSprint.getValue() == Sprint.Rage) {
            if (!mc.gameSettings.keyBindForward.isKeyDown() && !mc.gameSettings.keyBindBack.isKeyDown() && !mc.gameSettings.keyBindLeft.isKeyDown() && !mc.gameSettings.keyBindRight.isKeyDown() || mc.player.isSneaking() || mc.player.collidedHorizontally || (float) mc.player.getFoodStats().getFoodLevel() <= 6.0f)
                mc.player.setSprinting(true);
        }
        if (autoSprint.getValue() == Sprint.Legit) {
            if (!mc.gameSettings.keyBindForward.isKeyDown() || mc.player.isSneaking() || mc.player.isHandActive() || mc.player.collidedHorizontally || (float) mc.player.getFoodStats().getFoodLevel() <= 6.0f || mc.currentScreen != null)
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        }
        if (autoSpin.getValue() && yawAnimation.getValue()) {
            mc.player.rotationYaw = (mc.player.rotationYaw + yaw.getValue());
        }
        if (autoSpin.getValue() && pitchAnimation.getValue()) {
            mc.player.rotationPitch = (mc.player.rotationPitch + pitch.getValue());
        }
        if (autoWalk.getValue()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
        if (autoSneak.getValue()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
        if (autoJump.getValue()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
        }
    }

    public void onDisable() {
        if (!autoSprint.nullCheck()) {
            mc.player.setSprinting(false);
        }
        if (autoWalk.getValue()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        }
        if (autoSneak.getValue()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
        if (autoJump.getValue()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
        }
    }
    public enum Sprint {
        Rage,
        Legit,
        None
    }
}

