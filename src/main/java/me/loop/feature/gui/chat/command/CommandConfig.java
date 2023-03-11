



package me.loop.feature.gui.chat.command;

import me.loop.feature.gui.chat.gui.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CommandConfig extends CommandBase
{
    public String getName() {
        return "betterchat";
    }
    
    public String getUsage(final ICommandSender sender) {
        return "/betterchat";
    }
    
    public boolean checkPermission(final MinecraftServer server,  final ICommandSender sender) {
        return true;
    }
    
    public void execute(final MinecraftServer server,  final ICommandSender sender,  final String[] args) throws CommandException {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiConfig());
    }
}
