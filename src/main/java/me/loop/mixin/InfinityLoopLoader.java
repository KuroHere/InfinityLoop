package me.loop.mixin;

import me.loop.InfinityLoop;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class InfinityLoopLoader
        implements IFMLLoadingPlugin {
    private static boolean isObfuscatedEnvironment = false;

    public InfinityLoopLoader() {
        InfinityLoop.LOGGER.info("\n\nLoading mixins by KuroHere");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.infinityloop.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        InfinityLoop.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    public String getAccessTransformerClass() {
        return null;
    }
}

