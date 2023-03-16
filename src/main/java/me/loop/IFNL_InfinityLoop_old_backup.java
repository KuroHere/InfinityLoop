package me.loop;

import me.loop.api.managers.impl.*;
import me.loop.api.utils.impl.IconUtils;
import me.loop.api.utils.impl.phobos.Sphere;
import me.loop.api.utils.impl.renders.helper.dism.EntityGib;
import me.loop.api.utils.impl.renders.helper.dism.RenderGib;
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

import static me.loop.api.managers.Managers.networkHandler;
import static me.loop.api.utils.impl.Util.mc;


// OLD BACKUP FOR TESTING
// U NEEDS TO REMOVE MANAGERS.CLASS

@Mod(modid = "infinityloop", name = "InfinityLoop", version = "0.0.3")

public class IFNL_InfinityLoop_old_backup {
    public static final String MODID = "infinityloop", MODNAME = "InfinityLoop", MODVER = "0.0.3";

    @Mod.Instance
    public static IFNL_InfinityLoop_old_backup INSTANCE;
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
        ConfigManager.loadAlts();
        ConfigManager.loadSearch();
        LOGGER.info("\n\nLoading InfinityLoop by KuroHere");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }

        ConfigManager.init();

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
        timerManager = new TimerManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        ConfigManager.load(ConfigManager.getCurrentConfig());
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        LOGGER.info("InfinityLoop successfully loaded!\n");

        if(mc.session != null && !alts.contains(mc.session.getUsername())) {
            alts.add(mc.session.getUsername());
        }
    }

    /*--------------------    UNLOAD  ------------------------*/

    private static boolean unloaded;

    public static void unload(boolean unload) {
        Display.setTitle("Minecraft 1.12.2");
        LOGGER.info("\n\nUnloading InfinityLoop by KuroHere");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }
        ConfigManager.saveAlts();
        ConfigManager.saveSearch();
        if (!unloaded) {
            eventManager.onUnload();

            moduleManager.onUnloadPre();
            ConfigManager.save(ConfigManager.getCurrentConfig());
            moduleManager.onUnloadPost();
            unloaded = true;
        }

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

    /*--------------------    RELOAD  ------------------------*/

    public static void reload() {
        InfinityLoop.unload(false);
        InfinityLoop.load();
    }


    /*--------------------------------------------------------*/

    public static void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            try (final InputStream inputStream16x = Minecraft.class.getResourceAsStream("loop/imgs/icon16x.png");
                 final InputStream inputStream32x = Minecraft.class.getResourceAsStream("loop/imgs/icon32x.png")) {
                final ByteBuffer[] icons = { IconUtils.INSTANCE.readImageToBuffer(inputStream16x), IconUtils.INSTANCE.readImageToBuffer(inputStream32x) };
                Display.setIcon((ByteBuffer[])icons);
            }
            catch (Exception e) {
                InfinityLoop.LOGGER.error("Couldn't set Windows Icon", (Throwable)e);
            }
        }
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityGib.class, RenderGib::new);
        GlobalExecutor.EXECUTOR.submit(() -> Sphere.cacheSphere());
        LOGGER.info("KuroHere!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        this.setWindowIcon();
        Display.setTitle("InfinityLoop");
        initTime = System.currentTimeMillis();
        InfinityLoop.load();
        MinecraftForge.EVENT_BUS.register(networkHandler);
    }

    static {
        unloaded = false;
    }
}
