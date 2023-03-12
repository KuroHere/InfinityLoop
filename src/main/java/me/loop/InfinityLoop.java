package me.loop;

import me.loop.api.managers.Managers;
import me.loop.api.managers.impl.ConfigManager;
import me.loop.api.utils.impl.IconUtils;
import me.loop.api.utils.impl.phobos.GlobalExecutor;
import me.loop.api.utils.impl.phobos.Sphere;
import me.loop.api.utils.impl.renders.helper.dism.EntityGib;
import me.loop.api.utils.impl.renders.helper.dism.RenderGib;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static me.loop.api.managers.Managers.*;

@Mod(modid = "infinityloop", name = "InfinityLoop", version = "0.0.3")

public class InfinityLoop {

    @Mod.Instance
    public static InfinityLoop INSTANCE;
    public static final String MODID = "infinityloop", MODNAME = "InfinityLoop", MODVER = "0.0.3";
    public static final Logger LOGGER = LogManager.getLogger("InfinityLoop");
    public static java.util.List<String> alts = new ArrayList<>();
    public static long initTime;


    public static void load() {
        LOGGER.info("Loading InfinityLoop...");

        Managers.load();
        LOGGER.info("Managers loaded.");

        moduleManager.init();
        LOGGER.info("Modules loaded.");

        ConfigManager.load(ConfigManager.getCurrentConfig());

        eventManager.init();
        LOGGER.info("EventManager loaded.");

        textManager.init(true);

        totemPopManager.init();
        LOGGER.info("TotemPopManager loaded.");

        moduleManager.onLoad();
        LOGGER.info("Initialising BetterChat (made by llamalad7)");

        LOGGER.info(MODNAME + "successfully loaded!\n");

    }

    public static void unload(boolean force) {
        LOGGER.info("Unloading InfinityLoop...");

        Managers.unload(force);

        LOGGER.info("InfinityLoop unloaded!\n");
    }

    public static void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {

            try (InputStream inputStream16x = Minecraft.class.getResourceAsStream("loop/imgs/icon16x.png"); InputStream inputStream32x = Minecraft.class.getResourceAsStream("/assets/minecraft/textures/mio/constant/icon32x.png")) {

                ByteBuffer[] icons = new ByteBuffer[]{IconUtils.INSTANCE.readImageToBuffer(inputStream16x), IconUtils.INSTANCE.readImageToBuffer(inputStream32x)};
                Display.setIcon(icons);

            } catch (Exception e) {
                LOGGER.error("Loop couldn't set the window icon!", e);
            }
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityGib.class, RenderGib::new);
        GlobalExecutor.EXECUTOR.submit(() -> Sphere.cacheSphere());
        Display.setTitle(MODNAME + ": Loading...");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        this.setWindowIcon();
        Display.setTitle(MODNAME);
        InfinityLoop.load();
        initTime = System.currentTimeMillis();
        MinecraftForge.EVENT_BUS.register(networkHandler);
    }
}

