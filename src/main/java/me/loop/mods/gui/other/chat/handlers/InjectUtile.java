package me.loop.mods.gui.other.chat.handlers;

import me.loop.api.utils.impl.Util;
import me.loop.mods.gui.other.chat.gui.GuiBetterChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


// full credit goes to llamalad7
// too lazy to make it a mixin lol
public class InjectUtile implements Util {
    public static GuiBetterChat chatGUI;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        chatGUI = new GuiBetterChat(mc);
        ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, mc.ingameGUI, chatGUI, "field_73840_e");
    }
}
