package me.loop;

import me.loop.features.gui.font.CFontRenderer;
import me.loop.manager.*;
import me.loop.util.impl.IconUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Objects;

import static me.loop.util.impl.Util.mc;

@Mod(modid = "infinityloop", name = "InfinityLoop", version = "0.0.3")
public class InfinityLoop {
    public static final String
            MODID = "infinityloop",
            MODNAME = "InfinityLoop",
            MODVER = "0.0.3";
    public static final Logger LOGGER = LogManager.getLogger("InfinityLoop");

    @Mod.Instance
    public static InfinityLoop INSTANCE;
    private static boolean unloaded;
    public static long initTime;
    public static java.util.List<String> alts = new ArrayList<>();

    static {
        unloaded = false;
    }

    /*-----------------    MANAGERS  ---------------------*/

    public static FpsManager fpsManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;

    /*-----------------    Fonts  ---------------------*/

    public static CFontRenderer fontRenderer;
    public static CFontRenderer fontRenderer2;
    public static CFontRenderer fontRenderer3;
    public static CFontRenderer fontRenderer4;
    public static CFontRenderer fontRenderer5;
    public static CFontRenderer fontRenderer6;
    public static CFontRenderer fontRenderer7;
    public static CFontRenderer fontRenderer8;


    /*---------------------    LOAD  -------------------------*/

    public static void load() {

        LOGGER.info("Loading InfinityLoop...");
        unloaded = false;

        ConfigManager.loadAlts();
        ConfigManager.init();

        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }

        /*------------    FONTS    ------------ */

        try {
            fontRenderer = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/ThunderFont2.ttf"))).deriveFont(24.f), true, true);
            fontRenderer2 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/ThunderFont3.ttf"))).deriveFont(36.f), true, true);
            fontRenderer3 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/ThunderFont2.ttf"))).deriveFont(18.f), true, true);
            fontRenderer4 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/ThunderFont2.ttf"))).deriveFont(50.f), true, true);
            fontRenderer5 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/Monsterrat.ttf"))).deriveFont(12.f), true, true);
            fontRenderer6 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/Monsterrat.ttf"))).deriveFont(14.f), true, true);
            fontRenderer7 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/Monsterrat.ttf"))).deriveFont(10.f), true, true);
            fontRenderer8 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/ThunderFont3.ttf"))).deriveFont(62.f), true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*------------------------------------- */

        fpsManager = new FpsManager();
        textManager = new TextManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        packetManager = new PacketManager();
        eventManager = new EventManager();
        speedManager = new SpeedManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        serverManager = new ServerManager();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        holeManager = new HoleManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        LOGGER.info(MODNAME + "successfully loaded!\n");

        if (mc.session != null && !alts.contains(mc.session.getUsername())) {
            alts.add(mc.session.getUsername());
        }
    }

    public static void unload(boolean unload) {
        Display.setTitle("Minecraft 1.12.2");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }

        ConfigManager.saveAlts();

        InfinityLoop.onUnload();

        fpsManager = null;
        eventManager = null;
        fontRenderer = null;
        friendManager = null;
        speedManager = null;
        holeManager = null;
        positionManager = null;
        rotationManager = null;
        configManager = null;
        commandManager = null;
        colorManager = null;
        serverManager = null;
        potionManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        LOGGER.info("InfinityLoop unloaded!\n");
    }

    public static void reload() {
        InfinityLoop.unload(false);
        InfinityLoop.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            ConfigManager.save(ConfigManager.getCurrentConfig());
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }

    public static void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            try (final InputStream inputStream16x = Minecraft.class.getResourceAsStream("/assets/minecraft/textures/icon16x.png");
                 final InputStream inputStream32x = Minecraft.class.getResourceAsStream("/assets/minecraft/textures/icon32x.png")) {
                final ByteBuffer[] icons = { IconUtil.INSTANCE.readImageToBuffer(inputStream16x), IconUtil.INSTANCE.readImageToBuffer(inputStream32x) };
                Display.setIcon(icons);
            }
            catch (Exception e) {
                InfinityLoop.LOGGER.error("Couldn't set Windows Icon", e);
            }
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Display.setTitle(MODNAME + ": Loading...");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        setWindowIcon();
        Display.setTitle(MODNAME + " | " + MODVER);
        InfinityLoop.load();
    }
}

