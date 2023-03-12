package me.loop.client.modules.impl.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.loop.api.events.impl.client.ClientEvent;
import me.loop.api.events.impl.network.EventPacket;
import me.loop.api.managers.Managers;
import me.loop.api.managers.impl.FileManager;
import me.loop.api.utils.impl.renders.TextUtil;
import me.loop.api.utils.impl.worlds.Timer;
import me.loop.client.commands.Command;
import me.loop.client.modules.Category;
import me.loop.api.modules.Module;
import me.loop.client.modules.impl.client.ModuleTools;
import me.loop.client.modules.settings.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Notifications
        extends Module {
    private static final String fileName = "loop/util/ModuleMessage_List.txt";
    private static final List<String> modules = new ArrayList<>();
    private static Notifications INSTANCE = new Notifications();
    private final Timer timer = new Timer();
    public Setting<Boolean> totemPops = this.add(new Setting<>("TotemPops", true));
    public Setting<Boolean> totemNoti = this.add(new Setting<Object>("TotemNoti", Boolean.FALSE, v -> this.totemPops.getValue()));
    public Setting<Integer> delay = this.add(new Setting<Object>("Delay", 200, 0, 5000, v -> this.totemPops.getValue(), "Delays messages."));
    public Setting<Boolean> clearOnLogout = this.add(new Setting<>("LogoutClear", false));
    public Setting<Boolean> moduleMessage = this.add(new Setting<>("ModuleMessage", true));
    private final Setting<Boolean> readfile = this.add(new Setting<Object>("LoadFile", Boolean.FALSE, v -> this.moduleMessage.getValue()));
    public Setting<Boolean> list = this.add(new Setting<Object>("List", Boolean.FALSE, v -> this.moduleMessage.getValue()));
    public Setting<Boolean> visualRange = this.add(new Setting<>("VisualRange", false));
    public Setting<Boolean> VisualRangeSound = this.add(new Setting<>("VisualRangeSound", false));
    public Setting<Boolean> coords = this.add(new Setting<Object>("Coords", Boolean.TRUE, v -> this.visualRange.getValue()));
    public Setting<Boolean> leaving = this.add(new Setting<Object>("Leaving", Boolean.FALSE, v -> this.visualRange.getValue()));
    public Setting<Boolean> pearls = this.add(new Setting<>("PearlNotifs", false));
    public Setting<Boolean> crash = this.add(new Setting<>("Crash", false));
    public Setting<Boolean> popUp = this.add(new Setting<>("PopUpVisualRange", false));
    public Timer totemAnnounce = new Timer();
    private List<EntityPlayer> knownPlayers = new ArrayList<>();
    private boolean check;

    public Notifications() {
        super("Notifications", "Sends Messages.", Category.CLIENT);
        this.setInstance();
    }

    public static Notifications getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Notifications();
        }
        return INSTANCE;
    }

    public static void displayCrash(Exception e) {
        Command.sendMessage("\u00a7cException caught: " + e.getMessage());
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        this.check = true;
        this.loadFile();
        this.check = false;
    }

    @Override
    public void onEnable() {
        this.knownPlayers = new ArrayList<>();
        if (!this.check) {
            this.loadFile();
        }
    }

    @Override
    public void onUpdate() {
        if (this.readfile.getValue()) {
            if (!this.check) {
                Command.sendMessage("Loading File...");
                this.timer.reset();
                this.loadFile();
            }
            this.check = true;
        }
        if (this.check && this.timer.passedMs(750L)) {
            this.readfile.setValue(false);
            this.check = false;
        }
        if (this.visualRange.getValue()) {
            ArrayList<EntityPlayer> tickPlayerList = new ArrayList<>(Notifications.mc.world.playerEntities);
            if (tickPlayerList.size() > 0) {
                for (EntityPlayer player : tickPlayerList) {
                    if (player.getName().equals(Notifications.mc.player.getName()) || this.knownPlayers.contains(player))
                        continue;
                    this.knownPlayers.add(player);
                    if (Managers.friendManager.isFriend(player)) {
                        Command.sendMessage("Player \u00a7a" + player.getName() + "\u00a7r" + " entered your visual range" + (this.coords.getValue() ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!"), this.popUp.getValue());
                    } else {
                        Command.sendMessage("Player \u00a7c" + player.getName() + "\u00a7r" + " entered your visual range" + (this.coords.getValue() ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!"), this.popUp.getValue());
                    }
                    if (this.VisualRangeSound.getValue()) {
                        Notifications.mc.player.playSound(SoundEvents.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                    }
                    return;
                }
            }
            if (this.knownPlayers.size() > 0) {
                for (EntityPlayer player : this.knownPlayers) {
                    if (tickPlayerList.contains(player)) continue;
                    this.knownPlayers.remove(player);
                    if (this.leaving.getValue()) {
                        if (Managers.friendManager.isFriend(player)) {
                            Command.sendMessage("Player \u00a7a" + player.getName() + "\u00a7r" + " left your visual range" + (this.coords.getValue() ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!"), this.popUp.getValue());
                        } else {
                            Command.sendMessage("Player \u00a7c" + player.getName() + "\u00a7r" + " left your visual range" + (this.coords.getValue() ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!"), this.popUp.getValue());
                        }
                    }
                    return;
                }
            }
        }
    }

    public void loadFile() {
        List<String> fileInput = FileManager.readTextFileAllLines(fileName);
        Iterator<String> i = fileInput.iterator();
        modules.clear();
        while (i.hasNext()) {
            String s = i.next();
            if (s.replaceAll("\\s", "").isEmpty()) continue;
            modules.add(s);
        }
    }

    @SubscribeEvent
    public void onReceivePacket(EventPacket.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject && this.pearls.getValue()) {
            SPacketSpawnObject packet = event.getPacket();
            EntityPlayer player = Notifications.mc.world.getClosestPlayer(packet.getX(), packet.getY(), packet.getZ(), 1.0, false);
            if (player == null) {
                return;
            }
            if (packet.getEntityID() == 85) {
                Command.sendMessage("\u00a7cPearl thrown by " + player.getName() + " at X:" + (int) packet.getX() + " Y:" + (int) packet.getY() + " Z:" + (int) packet.getZ());
            }
        }
    }


    public TextComponentString getNotifierOn(Module module) {
        if (ModuleTools.getInstance().isEnabled()) {
            switch (ModuleTools.getInstance().notifier.getValue()) {
                case INFINITYLOOP: {
                    TextComponentString text = new TextComponentString((Managers.commandManager.getClientMessage()) + " " + ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.RESET + ChatFormatting.GREEN + " enabled.");
                    return text;
                }
                case FUTURE: {
                    TextComponentString text = new TextComponentString(ChatFormatting.RED + "[Future] " + ChatFormatting.GRAY + module.getDisplayName() + " toggled " + ChatFormatting.GREEN + "on" + ChatFormatting.GRAY + ".");
                    return text;
                }
                case DOTGOD: {
                    TextComponentString text = new TextComponentString(ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.DARK_AQUA + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + " was " + ChatFormatting.GREEN + "enabled.");
                    return text;
                }
                case SNOW: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.BLUE + "[" + (Object)ChatFormatting.AQUA + "Snow" + (Object)ChatFormatting.BLUE + "] [" + (Object)ChatFormatting.DARK_AQUA + module.getDisplayName() + (Object)ChatFormatting.BLUE + "] " + (Object)ChatFormatting.GREEN + "enabled");
                    return text;
                }
                case WEATHER: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.AQUA + "[" + (Object)ChatFormatting.AQUA + "Weather" + (Object)ChatFormatting.AQUA + "] " + (Object)ChatFormatting.DARK_AQUA + module.getDisplayName() + (Object)ChatFormatting.WHITE + " was toggled " + (Object)ChatFormatting.GREEN + "on.");
                    return text;
                }
                case CATALYST: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.DARK_GRAY + "[" + (Object)ChatFormatting.AQUA + "Catalyst" + (Object)ChatFormatting.DARK_GRAY + "] " + (Object)ChatFormatting.GRAY + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + "" + (Object)ChatFormatting.GREEN + " ON");
                    return text;
                }
                case RUSHERHACK: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.WHITE + "[" + (Object)ChatFormatting.GREEN + "rusherhack" + (Object)ChatFormatting.WHITE + "] " + (Object)ChatFormatting.WHITE + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + "" + (Object)ChatFormatting.WHITE + " has been enabled");
                    return text;
                }
                case KONAS: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.DARK_GRAY + "[" + (Object)ChatFormatting.LIGHT_PURPLE + "Konas" + (Object)ChatFormatting.DARK_GRAY + "] " + (Object)ChatFormatting.WHITE + module.getDisplayName() + (Object)ChatFormatting.WHITE + " has been enabled");
                    return text;
                }
                case LEGACY: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.WHITE + "[" + (Object)ChatFormatting.LIGHT_PURPLE + "Legacy" + (Object)ChatFormatting.WHITE + "] " + (Object)ChatFormatting.BOLD + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + "" + (Object)ChatFormatting.GREEN + " enabled.");
                    return text;
                }
                case EUROPA: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.GRAY + "[" + (Object)ChatFormatting.RED + "Europa" + (Object)ChatFormatting.GRAY + "] " + (Object)ChatFormatting.RESET + (Object)ChatFormatting.WHITE + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + "" + (Object)ChatFormatting.GREEN + (Object)ChatFormatting.BOLD + " Enabled!");
                    return text;
                }
                case PYRO: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.DARK_RED + "[" + (Object)ChatFormatting.DARK_RED + "Pyro" + (Object)ChatFormatting.DARK_RED + "] " + (Object)ChatFormatting.GREEN + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + "" + (Object)ChatFormatting.GREEN + " has been enabled.");
                    return text;
                }
                case MUFFIN: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.LIGHT_PURPLE + "[" + (Object)ChatFormatting.DARK_PURPLE + "Muffin" + (Object)ChatFormatting.LIGHT_PURPLE + "] " + (Object)ChatFormatting.LIGHT_PURPLE + module.getDisplayName() + (Object)ChatFormatting.DARK_PURPLE + " was " + (Object)ChatFormatting.GREEN + "enabled.");
                    return text;
                }
                case ABYSS: {
                    TextComponentString text = new TextComponentString(TextUtil.coloredString("[Abyss] ", ModuleTools.getInstance().abyssColor.getPlannedValue()) + (Object)ChatFormatting.WHITE + module.getDisplayName() + (Object)ChatFormatting.GREEN + " ON");
                    return text;
                }
                case LUIGIHACK: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.GREEN + "[LuigiHack] " + (Object)ChatFormatting.GRAY + module.getDisplayName() + " toggled " + (Object)ChatFormatting.GREEN + "on" + (Object)ChatFormatting.GRAY + ".");
                    return text;
                }
            }
        }
        TextComponentString text = new TextComponentString(Managers.commandManager.getClientMessage() + " " + ChatFormatting.GREEN + module.getDisplayName() + " toggled on.");
        return text;
    }

    public TextComponentString getNotifierOff(Module module) {
        if (ModuleTools.getInstance().isEnabled()) {
            switch (ModuleTools.getInstance().notifier.getValue()) {
                case INFINITYLOOP: {
                    TextComponentString text = new TextComponentString((Managers.commandManager.getClientMessage()) + " " + ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.RESET + ChatFormatting.RED + " disabled.");
                    return text;
                }
                case FUTURE: {
                    TextComponentString text = new TextComponentString(ChatFormatting.RED + "[Future] " + ChatFormatting.GRAY + module.getDisplayName() + " toggled " + ChatFormatting.RED + "off" + ChatFormatting.GRAY + ".");
                    return text;
                }
                case DOTGOD: {
                    TextComponentString text = new TextComponentString(ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.DARK_AQUA + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + " was " + ChatFormatting.RED + "disabled.");
                    return text;
                }
                case SNOW: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.BLUE + "[" + (Object)ChatFormatting.AQUA + "Snow" + (Object)ChatFormatting.BLUE + "] [" + (Object)ChatFormatting.DARK_AQUA + module.getDisplayName() + (Object)ChatFormatting.BLUE + "] " + (Object)ChatFormatting.RED + "disabled");
                    return text;
                }
                case WEATHER: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.AQUA + "[" + (Object)ChatFormatting.AQUA + "Weather" + (Object)ChatFormatting.AQUA + "] " + (Object)ChatFormatting.DARK_AQUA + module.getDisplayName() + (Object)ChatFormatting.WHITE + " was toggled " + (Object)ChatFormatting.RED + "off.");
                    return text;
                }
                case CATALYST: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.DARK_GRAY + "[" + (Object)ChatFormatting.AQUA + "Catalyst" + (Object)ChatFormatting.DARK_GRAY + "] " + (Object)ChatFormatting.GRAY + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + "" + (Object)ChatFormatting.RED + " OFF");
                    return text;
                }
                case RUSHERHACK: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.WHITE + "[" + (Object)ChatFormatting.GREEN + "rusherhack" + (Object)ChatFormatting.WHITE + "] " + (Object)ChatFormatting.WHITE + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + "" + (Object)ChatFormatting.WHITE + " has been disabled");
                    return text;
                }
                case LEGACY: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.WHITE + "[" + (Object)ChatFormatting.LIGHT_PURPLE + "Legacy" + (Object)ChatFormatting.WHITE + "] " + (Object)ChatFormatting.BOLD + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + "" + (Object)ChatFormatting.RED + " disabled.");
                    return text;
                }
                case KONAS: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.DARK_GRAY + "[" + (Object)ChatFormatting.LIGHT_PURPLE + "Konas" + (Object)ChatFormatting.DARK_GRAY + "] " + (Object)ChatFormatting.WHITE + module.getDisplayName() + (Object)ChatFormatting.WHITE + " has been disabled");
                    return text;
                }
                case EUROPA: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.GRAY + "[" + (Object)ChatFormatting.RED + "Europa" + (Object)ChatFormatting.GRAY + "] " + (Object)ChatFormatting.RESET + (Object)ChatFormatting.WHITE + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + "" + (Object)ChatFormatting.RED + (Object)ChatFormatting.BOLD + " Disabled!");
                    return text;
                }
                case PYRO: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.DARK_RED + "[" + (Object)ChatFormatting.DARK_RED + "Pyro" + (Object)ChatFormatting.DARK_RED + "] " + (Object)ChatFormatting.RED + module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + "" + (Object)ChatFormatting.RED + " has been disabled.");
                    return text;
                }
                case MUFFIN: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.LIGHT_PURPLE + "[" + (Object)ChatFormatting.DARK_PURPLE + "Muffin" + (Object)ChatFormatting.LIGHT_PURPLE + "] " + (Object)ChatFormatting.LIGHT_PURPLE + module.getDisplayName() + (Object)ChatFormatting.DARK_PURPLE + " was " + (Object)ChatFormatting.RED + "disabled.");
                    return text;
                }
                case ABYSS: {
                    TextComponentString text = new TextComponentString(TextUtil.coloredString("[Abyss] ", ModuleTools.getInstance().abyssColor.getPlannedValue()) + (Object)ChatFormatting.WHITE + module.getDisplayName() + (Object)ChatFormatting.RED + " OFF");
                    return text;
                }
                case LUIGIHACK: {
                    TextComponentString text = new TextComponentString((Object)ChatFormatting.GREEN + ("[LuigiHack] ") + (Object)ChatFormatting.GRAY + module.getDisplayName() + " toggled " + (Object)ChatFormatting.RED + "off" + (Object)ChatFormatting.GRAY + ".");
                    return text;
                }
            }
        }
        TextComponentString text = new TextComponentString(Managers.commandManager.getClientMessage() + " " + ChatFormatting.RED + module.getDisplayName() + " toggled off.");
        return text;
    }

    @SubscribeEvent
    public void onToggleModule(ClientEvent event) {
        int moduleNumber;
        Module module;
        if (!this.moduleMessage.getValue()) {
            return;
        }
        if (!(event.getStage() != 0 || (module = (Module) event.getFeature()).equals(this) || !modules.contains(module.getDisplayName()) && this.list.getValue())) {
            moduleNumber = 0;
            for (char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(getNotifierOff(module), moduleNumber);
        }

        if (event.getStage() == 1 && (modules.contains((module = (Module) event.getFeature()).getDisplayName()) || !this.list.getValue())) {
            moduleNumber = 0;
            for (char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(getNotifierOn(module), moduleNumber);
        }
    }
}
