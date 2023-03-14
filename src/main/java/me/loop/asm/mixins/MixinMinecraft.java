package me.loop.asm.mixins;

import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.renders.MotionBlur;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.mods.gui.other.mainmenu.InfinityLoopMenu;
import me.loop.mods.modules.impl.client.MainSettings;
import me.loop.mods.modules.impl.player.MultiTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.crash.CrashReport;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

import static me.loop.api.utils.impl.Util.mc;

@Mixin(value = {Minecraft.class})
public abstract class MixinMinecraft {
    @Shadow
    @Nullable
    public GuiScreen currentScreen;

    private long lastFrame = this.getTime();

    public long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    @Inject(method = {"runTickKeyboard"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;", ordinal = 0)}, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRunTickKeyboard(CallbackInfo ci, int i) {
        if (Keyboard.getEventKeyState() && Managers.moduleManager != null) {
            Managers.moduleManager.onKeyInput(i);
        }
    }

    @Inject(method = {"shutdownMinecraftApplet"}, at = {@At(value = "HEAD")})
    private void stopClient(CallbackInfo callbackInfo) {
        this.unload();
    }

    @Redirect(method = {"run"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReportHook(Minecraft minecraft, CrashReport crashReport) {
        this.unload();
    }

    @Inject(method = {"shutdown"}, at = {@At(value = "HEAD")})
    public void shutdownHook(CallbackInfo info) {
        this.unload();
    }

    private void unload() {
        Managers.LOGGER.info("Initiated client shutdown!");
        Managers.onUnload();
        Managers.LOGGER.info("Finished client shutdown!");
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
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && Managers.moduleManager != null && Managers.moduleManager.getModuleByClass(MainSettings.class).mainMenu.getValue()) {
            Minecraft.getMinecraft().displayGuiScreen(new InfinityLoopMenu());
        }
    }

    @Inject(method={"displayGuiScreen"}, at={@At(value="HEAD")})
    private void displayGuiScreenHook(GuiScreen screen, CallbackInfo ci) {
        if (screen instanceof GuiMainMenu && Managers.moduleManager != null && Managers.moduleManager.getModuleByClass(MainSettings.class).mainMenu.getValue()) {
            mc.displayGuiScreen(new InfinityLoopMenu());
        }
    }

    @Inject(method={"runGameLoop"}, at={@At(value="HEAD")})
    private void onRunGameLoopPre(CallbackInfo ci) {
        MotionBlur.createAccumulation();
        long currentTime = this.getTime();
        int deltaTime = (int)(currentTime - this.lastFrame);
        this.lastFrame = currentTime;
        RenderUtil.deltaTime = deltaTime;
    }
}

