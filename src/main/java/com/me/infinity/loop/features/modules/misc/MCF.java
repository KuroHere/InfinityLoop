package com.me.infinity.loop.features.modules.misc;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.modules.ModuleCategory;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.me.infinity.loop.features.command.Command;
import com.me.infinity.loop.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

public class MCF
        extends Module {
    private boolean clicked = false;

    public MCF() {
        super("MCF", "Middleclick Friends.", ModuleCategory.MISC);
    }

    @Override
    public void onUpdate() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked && MCF.mc.currentScreen == null) {
                this.onClick();
            }
            this.clicked = true;
        } else {
            this.clicked = false;
        }
    }

    private void onClick() {
        Entity entity;
        RayTraceResult result = MCF.mc.objectMouseOver;
        if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
            if (InfinityLoop.friendManager.isFriend(entity.getName())) {
                InfinityLoop.friendManager.removeFriend(entity.getName());
                Command.sendMessage(ChatFormatting.RED + entity.getName() + ChatFormatting.RED + " has been unfriended.");
            } else {
                InfinityLoop.friendManager.addFriend(entity.getName());
                Command.sendMessage(ChatFormatting.AQUA + entity.getName() + ChatFormatting.AQUA + " has been friended.");
            }
        }
        this.clicked = true;
    }
}

