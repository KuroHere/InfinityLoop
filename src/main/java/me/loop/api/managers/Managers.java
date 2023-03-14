package me.loop.api.managers;

import me.loop.InfinityLoop;
import me.loop.api.managers.impl.*;
import me.loop.api.utils.impl.renders.helper.ffp.NetworkHandler;
import me.loop.mods.gui.font.CFontRenderer;
import me.loop.api.managers.impl.ModuleManager;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.util.Objects;

public class Managers extends InfinityLoop {

    private static boolean loaded = true;

    /*-----------------    MANAGERS  ---------------------*/

    public static TotemPopManager totemPopManager;
    public static NotificationManager notificationManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static FpsManagement fpsManagement;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static NetworkHandler networkHandler;
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
        ConfigManager.loadSearch();
        LOGGER.info("\n\nLoading InfinityLoop by KuroHere");

        loaded = true;

        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }


        /*------------    FONTS    ------------ */
        try {
            fontRenderer = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/ThunderFont2.ttf"))).deriveFont( 24.f ), true, true );
            fontRenderer2 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/ThunderFont3.ttf"))).deriveFont( 36.f ), true, true );
            fontRenderer3 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/ThunderFont2.ttf"))).deriveFont( 18.f ), true, true );
            fontRenderer4 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/ThunderFont2.ttf"))).deriveFont( 50.f ), true, true );
            fontRenderer5 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/Monsterrat.ttf"))).deriveFont( 12.f ), true, true );
            fontRenderer6 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/Monsterrat.ttf"))).deriveFont( 14.f ), true, true );
            fontRenderer7 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/Monsterrat.ttf"))).deriveFont( 10.f ), true, true );
            fontRenderer8 = new CFontRenderer( Font.createFont( Font.TRUETYPE_FONT, Objects.requireNonNull(InfinityLoop.class.getResourceAsStream("/fonts/ThunderFont3.ttf"))).deriveFont( 62.f ), true, true );
        } catch ( Exception e ) {
            e.printStackTrace( );
        }

        /*------------------------------------- */

        totemPopManager = new TotemPopManager();
        notificationManager = new NotificationManager();
        networkHandler = new NetworkHandler();
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

        ConfigManager.init();
        ConfigManager.load(ConfigManager.getCurrentConfig());

        Managers.moduleManager.init();
        LOGGER.info("Modules loaded.");

        eventManager.init();
        LOGGER.info("EventManager loaded.");

        textManager.init(true);

        totemPopManager.init();
        LOGGER.info("TotemPopManager loaded.");

        FriendManager.loadFriends();
        moduleManager.onUnloadPre();

        //if(mc.session != null && !alts.contains(mc.session.getUsername())) {
            //alts.add(mc.session.getUsername());
        //}
    }

    /*--------------------    UNLOAD  ------------------------*/

    public static void unload(boolean unload) {

        ConfigManager.saveAlts();
        ConfigManager.saveSearch();
        FriendManager.saveFriends();

        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }

        onUnload();
        Display.setTitle("Minecraft 1.12.2");

        totemPopManager = null;
        notificationManager = null;
        networkHandler = null;
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
    }

    public static void onUnload() {

        if (isLoaded()) {
            eventManager.onUnload();
            moduleManager.onUnloadPre();
            ConfigManager.save(ConfigManager.getCurrentConfig());
            moduleManager.onUnloadPost();

            loaded = false;
        }
    }

    //Getters

    public static boolean isLoaded() {
        return loaded;
    }
}
