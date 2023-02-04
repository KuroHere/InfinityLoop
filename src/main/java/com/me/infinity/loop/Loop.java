package com.me.infinity.loop;

import com.me.infinity.loop.features.gui.font.CFontRenderer;
import com.me.infinity.loop.util.interfaces.Util;
import com.me.infinity.loop.manager.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

@Mod(modid = "loop", name = "InfinityLoop", version = "0.0.3")

public class Loop {
    public static final String MODID = "loop", MODNAME = "InfinityLoop", MODVER = "0.0.3";

    @Mod.Instance
    public static Loop INSTANCE;
    public static long initTime;
    public static java.util.List<String> alts = new ArrayList<>();

    /*--------------------    LOGGER  ------------------------*/

    public static final Logger LOGGER;
    static {
        LOGGER = LogManager.getLogger("InfinityLoop");
        unloaded = false;
    }

    public static Logger getLogger()
    {
        return LOGGER;
    }


    /*-----------------    MANAGERS  ---------------------*/

    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static FpsManagement fpsManagement;
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
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    public static TimerManager timerManager;
    public static FpsManagement getFpsManagement() {
        return fpsManagement;
    }


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
        ConfigManager.loadAlts();
        LOGGER.info("\n\nLoading InfinityLoop by KuroHere");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }

        /*------------    FONTS    ------------ */
        try {
            fontRenderer = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(Loop.class.getResourceAsStream("/fonts/ThunderFont2.ttf"))).deriveFont( 24.f ), true, true );
            fontRenderer2 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(Loop.class.getResourceAsStream("/fonts/ThunderFont3.ttf"))).deriveFont( 36.f ), true, true );
            fontRenderer3 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(Loop.class.getResourceAsStream("/fonts/ThunderFont2.ttf"))).deriveFont( 18.f ), true, true );
            fontRenderer4 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(Loop.class.getResourceAsStream("/fonts/ThunderFont2.ttf"))).deriveFont( 50.f ), true, true );
            fontRenderer5 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(Loop.class.getResourceAsStream("/fonts/Monsterrat.ttf"))).deriveFont( 12.f ), true, true );
            fontRenderer6 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(Loop.class.getResourceAsStream("/fonts/Monsterrat.ttf"))).deriveFont( 14.f ), true, true );
            fontRenderer7 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(Loop.class.getResourceAsStream("/fonts/Monsterrat.ttf"))).deriveFont( 10.f ), true, true );
            fontRenderer8 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(Loop.class.getResourceAsStream("/fonts/ThunderFont3.ttf"))).deriveFont( 62.f ), true, true );
        } catch ( Exception e ) {
            e.printStackTrace( );
        }
        /*------------------------------------- */

        textManager = new TextManager();
        fpsManagement = new FpsManagement();
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
        fileManager = new FileManager();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        holeManager = new HoleManager();
        timerManager = new TimerManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        LOGGER.info("InfinityLoop successfully loaded!\n");

        if(Util.mc.session != null && !alts.contains(Util.mc.session.getUsername())) {
            alts.add(Util.mc.session.getUsername());
        }
    }

    /*--------------------    UNLOAD  ------------------------*/

    private static boolean unloaded;
    public static void unload(boolean unload) {
        Display.setTitle("Minecraft 1.12.2");
        LOGGER.info("\n\nUnloading InfinityLoop by KuroHere");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : "-");
        }
        ConfigManager.saveAlts();
        Loop.onUnload();
        fontRenderer = null;
        eventManager = null;
        friendManager = null;
        speedManager = null;
        holeManager = null;
        positionManager = null;
        rotationManager = null;
        configManager = null;
        commandManager = null;
        colorManager = null;
        serverManager = null;
        fileManager = null;
        potionManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        timerManager = null;
        LOGGER.info("InfinityLoop unloaded!\n");
    }

    /*--------------------    RELOAD  ------------------------*/

    public static void reload() {
        Loop.unload(false);
        Loop.load();
    }

    /*--------------------    ONLOAD  ------------------------*/

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(Loop.configManager.config.replaceFirst("loop/", ""));
            moduleManager.onUnloadPost();
            timerManager.unload();
            unloaded = true;
        }
    }

    /*--------------------------------------------------------*/


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("KuroHere!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle("InfinityLoop Client");
        initTime = System.currentTimeMillis();
        Loop.load();
    }

    static {
        unloaded = false;
    }
}

