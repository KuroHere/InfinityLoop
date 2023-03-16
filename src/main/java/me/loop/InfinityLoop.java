package me.loop;

import me.loop.api.managers.*;
import me.loop.api.utils.impl.IconUtil;
import me.loop.api.utils.impl.phobos.Sphere;
import me.loop.api.utils.impl.renders.helper.dism.EntityGib;
import me.loop.api.utils.impl.renders.helper.dism.RenderGib;
import me.loop.api.utils.impl.renders.helper.ffp.NetworkHandler;
import me.loop.asm.accessors.GlobalExecutor;
import me.loop.mods.gui.font.CFontRenderer;
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

import java.awt.*;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Objects;

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

    public static NotificationManager notificationManager;
    public static TotemPopManager totemPopManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static NetworkHandler networkHandler;
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
    public static TimerManager timerManager;

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
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }

        ConfigManager.loadAlts();
        ConfigManager.loadSearch();
        ConfigManager.init();

        if (InfinityLoop.INSTANCE == null) {
            InfinityLoop.INSTANCE = new InfinityLoop();
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

        notificationManager = new NotificationManager();
        textManager = new TextManager();
        totemPopManager = new TotemPopManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        networkHandler = new NetworkHandler();
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
        timerManager = new TimerManager();

        moduleManager.init();
        LOGGER.info("Modules loaded.");
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        LOGGER.info("TextManager loaded.");
        FriendManager.loadFriends();
        ConfigManager.load(ConfigManager.getCurrentConfig());

        moduleManager.onLoad();
        LOGGER.info(MODNAME + "successfully loaded!\n");

        /*if(mc.session != null && !alts.contains(mc.session.getUsername())) {
            alts.add(mc.session.getUsername());
        }*/
    }

    /*--------------------    UNLOAD  ------------------------*/

    public static void unload(boolean initReloadManager) {
        Display.setTitle("Minecraft 1.12.2");
        if (initReloadManager) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }

        ConfigManager.saveAlts();
        ConfigManager.saveSearch();
        FriendManager.saveFriends();

        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnloadPre();
            ConfigManager.save(ConfigManager.getCurrentConfig());
            moduleManager.onUnloadPost();
            unloaded = true;
        }

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
        potionManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        timerManager = null;
        LOGGER.info("InfinityLoop unloaded!\n");
    }

    /*--------------------    RE-UNLOAD  ------------------------*/

    public static void reload() {
        InfinityLoop.unload(false);
        InfinityLoop.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnloadPre();
            ConfigManager.save(ConfigManager.getCurrentConfig());
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }


    /*--------------------------------------------------------*/

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

    //IDK why I'm doing that
    private void setWindowsIcon() {
        InfinityLoop.setWindowIcon();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityGib.class, RenderGib::new);
        GlobalExecutor.EXECUTOR.submit(() -> Sphere.cacheSphere());
        Display.setTitle(MODNAME + ": Loading...");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle(MODNAME + " | " + MODVER);
        setWindowsIcon();

        InfinityLoop.load();
        initTime = System.currentTimeMillis();
        MinecraftForge.EVENT_BUS.register(networkHandler);
    }
}
