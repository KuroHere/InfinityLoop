package com.me.infinity.loop.mixin.mixins;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.gui.mainmenu.InfinityLoopMenu;
import com.me.infinity.loop.features.modules.client.MainSettings;
import com.me.infinity.loop.features.modules.player.MultiTask;
import com.me.infinity.loop.util.utils.minecraft.IMinecraft;
import com.me.infinity.loop.util.utils.renders.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.crash.CrashReport;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static com.me.infinity.loop.util.utils.Util.mc;

@Mixin(value = {Minecraft.class})
public abstract class MixinMinecraft implements IMinecraft {
    @Shadow
    @Nullable
    public GuiScreen currentScreen;

    private long lastFrame = this.getTime();

    public long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    @Shadow
    public abstract void displayGuiScreen(@Nullable GuiScreen var1);

    @Inject(method = {"shutdownMinecraftApplet"}, at = {@At(value = "HEAD")})
    private void stopClient(CallbackInfo callbackInfo) {
        this.unload();
    }

    @Redirect(method = {"run"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReport(Minecraft minecraft, CrashReport crashReport) {
        this.unload();
    }

    private void unload() {
        InfinityLoop.LOGGER.info("Initiated client shutdown!");
        InfinityLoop.unload(false);
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

    @Inject(method={"runGameLoop"}, at={@At(value="HEAD")})
    private void onRunGameLoopPre(CallbackInfo ci) {
        long currentTime = this.getTime();
        int deltaTime = (int)(currentTime - this.lastFrame);
        this.lastFrame = currentTime;
        RenderUtil.deltaTime = deltaTime;
    }

    @Inject(method={"displayGuiScreen"}, at={@At(value="HEAD")})
    private void displayGuiScreenHook(GuiScreen screen, CallbackInfo ci) {
        if (screen instanceof GuiMainMenu && InfinityLoop.moduleManager != null && InfinityLoop.moduleManager.getModuleByClass(MainSettings.class).mainMenu.getValue()) {
            mc.displayGuiScreen(new InfinityLoopMenu());
        }
    }

    @Redirect(method={"createDisplay"}, at=@At(value="INVOKE", target="Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V"))
    private void createDisplay(String title) {
        Display.setTitle((String)"InfinityLoop | There is no infinity here");
    }

}

