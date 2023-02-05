package com.me.infinity.loop.mixin.mixins;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.event.events.KeyEvent;
import com.me.infinity.loop.features.clickGui.mainmenu.InfinityLoopMenu;
import com.me.infinity.loop.features.modules.client.MainSettings;
import com.me.infinity.loop.features.modules.player.MultiTask;
import com.me.infinity.loop.util.minecraft.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static com.me.infinity.loop.util.interfaces.Util.mc;

@Mixin(value = {Minecraft.class})
public abstract class MixinMinecraft implements IMinecraft {
    @Shadow
    @Nullable
    public GuiScreen currentScreen;
    @Inject(method = {"shutdownMinecraftApplet"}, at = {@At(value = "HEAD")})
    private void stopClient(CallbackInfo callbackInfo) {
        this.unload();
    }

    @Redirect(method = {"run"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReport(Minecraft minecraft, CrashReport crashReport) {
        this.unload();
    }

    @Inject(method = {"runTickKeyboard"}, at = {@At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 0, shift = At.Shift.BEFORE)})
    private void onKeyboard(CallbackInfo callbackInfo) {
        int i;
        int n = i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
        if (Keyboard.getEventKeyState()) {
            KeyEvent event = new KeyEvent(i);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    private void unload() {
        InfinityLoop.LOGGER.info("Initiated client shutdown!");
        InfinityLoop.onUnload();
        InfinityLoop.LOGGER.info("Finished client shutdown!");
    }

    @Redirect(method = {"sendClickBlockToController"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
    private boolean isHandActiveWrapper(EntityPlayerSP playerSP) {
        return !MultiTask.getInstance().isOn() && playerSP.isHandActive();
    }

    @Redirect(method = {"rightClickMouse"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z", ordinal = 0))
    private boolean isHittingBlockHook(PlayerControllerMP playerControllerMP) {
        return !MultiTask.getInstance().isOn() && playerControllerMP.getIsHittingBlock();
    }
    @Inject(method={"runTick()V"}, at={@At(value="RETURN")})
    private void runTick(CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && InfinityLoop.moduleManager != null && InfinityLoop.moduleManager.getModuleByClass(MainSettings.class).mainMenu.getValue()) {
            Minecraft.getMinecraft().displayGuiScreen(new InfinityLoopMenu());
        }
    }

    @Inject(method={"displayGuiScreen"}, at={@At(value="HEAD")})
    private void displayGuiScreenHook(GuiScreen screen, CallbackInfo ci) {
        if (screen instanceof GuiMainMenu && InfinityLoop.moduleManager != null && InfinityLoop.moduleManager.getModuleByClass(MainSettings.class).mainMenu.getValue()) {
            mc.displayGuiScreen(new InfinityLoopMenu());
        }
    }
}

