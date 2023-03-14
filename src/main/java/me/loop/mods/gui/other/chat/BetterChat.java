



package me.loop.mods.gui.other.chat;

import me.loop.mods.gui.other.chat.command.CommandConfig;
import me.loop.mods.gui.other.chat.handlers.InjectUtile;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = "betterchat",  name = "Better Chat",  version = "1.5")
@SideOnly(Side.CLIENT)
public class BetterChat
{
    public static final String MODID = "betterchat";
    public static final String NAME = "Better Chat";
    public static final String VERSION = "1.5";
    private static ChatSettings settings;
    
    public static ChatSettings getSettings() {
        return BetterChat.settings;
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        (BetterChat.settings = new ChatSettings(new Configuration(event.getSuggestedConfigurationFile()))).loadConfig();
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)new InjectUtile());
        ClientCommandHandler.instance.registerCommand((ICommand)new CommandConfig());
    }
}
