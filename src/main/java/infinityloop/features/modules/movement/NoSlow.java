package infinityloop.features.modules.movement;

import infinityloop.event.events.player.EventKey;
import infinityloop.features.gui.InfinityLoopGui;
import infinityloop.features.modules.Module;
import infinityloop.features.modules.ModuleCategory;
import infinityloop.features.setting.Setting;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module {
    public Setting<Boolean> guiMove = this.register(new Setting("GuiMove", true));
    public  Setting<Boolean> noSlow = this.register(new Setting("NoSlow", true));
    public boolean sneaking = false;
    public static KeyBinding[] keys = new KeyBinding[]{NoSlow.mc.gameSettings.keyBindForward, NoSlow.mc.gameSettings.keyBindBack, NoSlow.mc.gameSettings.keyBindLeft, NoSlow.mc.gameSettings.keyBindRight, NoSlow.mc.gameSettings.keyBindJump, NoSlow.mc.gameSettings.keyBindSprint};

    public NoSlow() {
        super("NoSlow", "No Slow", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onMotionUpdate() {
        if (guiMove.getValue()) {
            if (NoSlow.mc.currentScreen instanceof GuiOptions || NoSlow.mc.currentScreen instanceof GuiVideoSettings || NoSlow.mc.currentScreen instanceof GuiScreenOptionsSounds || NoSlow.mc.currentScreen instanceof GuiContainer || NoSlow.mc.currentScreen instanceof GuiIngameMenu || NoSlow.mc.currentScreen instanceof InfinityLoopGui) {
                for (KeyBinding bind : keys) {
                    KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean)Keyboard.isKeyDown((int)bind.getKeyCode()));
                }
            } else if (NoSlow.mc.currentScreen == null) {
                for (KeyBinding bind : keys) {
                    if (Keyboard.isKeyDown((int)bind.getKeyCode())) continue;
                    KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean)false);
                }
            }
        }
        Item item = NoSlow.mc.player.getActiveItemStack().getItem();
    }

    @SubscribeEvent
    public void onInput(InputUpdateEvent inputUpdateEvent) {
        if (noSlow.getValue() && NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
            inputUpdateEvent.getMovementInput().moveStrafe *= Float.intBitsToFloat(Float.floatToIntBits(1.5738807f) ^ 0x7F6974EC);
            inputUpdateEvent.getMovementInput().moveForward *= Float.intBitsToFloat(Float.floatToIntBits(0.3323823f) ^ 0x7E0A2E03);
        }
    }

    /*
     * WARNING - void declaration
     */
    @SubscribeEvent
    public void onKeyEvent(EventKey eventKey) {
        if (guiMove.getValue() && !(NoSlow.mc.currentScreen instanceof GuiChat)) {
            eventKey.info = eventKey.pressed;
        }
    }
}