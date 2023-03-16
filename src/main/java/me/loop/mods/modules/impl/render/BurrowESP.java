package me.loop.mods.modules.impl.render;

import me.loop.api.events.impl.render.Render3DEvent;
import me.loop.api.utils.impl.Util;
import me.loop.api.utils.impl.renders.ColorUtil;
import me.loop.api.utils.impl.renders.RenderUtil;
import me.loop.mods.commands.Command;
import me.loop.mods.modules.Module;
import me.loop.mods.modules.Category;
import me.loop.mods.modules.impl.client.Colors;
import me.loop.mods.settings.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BurrowESP extends Module {

    private static BurrowESP INSTANCE = new BurrowESP();
    public Setting<Boolean> burrowAlert = add(new Setting("BurrowAlert", true));
    public Setting<Integer> range = add(new Setting("Range", 20, 5, 50));
    public Setting<Boolean> self = add(new Setting("Self", true));
    public Setting<Boolean> text = add(new Setting("Text", true));
    public Setting<Boolean> customText = add(new Setting("CustomText", false, v -> this.text.getValue()));
    public Setting<String> textString = add(new Setting("TextString", "BURROW", v -> this.text.getValue() && this.customText.getValue()));
    public Setting<Boolean> rainbow = add(new Setting("Rainbow", false));
    public Setting<Integer> red = add(new Setting("Red", 0, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> green = add(new Setting("Green", 255, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> blue = add(new Setting("Blue", 0, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> alpha = add(new Setting("Alpha", 130, 0, 255));
    public Setting<Integer> outlineAlpha = add(new Setting("OL-Alpha", 250, 0, 255));
    private final ConcurrentHashMap<EntityPlayer, Integer> players = new ConcurrentHashMap();
    List<EntityPlayer> anti_spam = new ArrayList<EntityPlayer>();
    List<Entity> burrowedPlayers = new ArrayList<Entity>();
    private final List<BlockPos> posList = new ArrayList<>();
    private final RenderUtil renderUtil = new RenderUtil();

    public BurrowESP() {
        super("BurrowESP", "BURROWESP", Category.RENDER, true, false);
        this.setInstance();
    }

    public static BurrowESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BurrowESP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.players.clear();
        this.anti_spam.clear();
    }
    
    public void onTick() {
        posList.clear();
        for (EntityPlayer player : Util.mc.world.playerEntities) {
            BlockPos blockPos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY + 0.2), Math.floor(player.posZ));
            if ((Util.mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || Util.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && blockPos.distanceSq(Util.mc.player.posX, Util.mc.player.posY, Util.mc.player.posZ) <= this.range.getValue()) {
                if (!(blockPos.distanceSq(Util.mc.player.posX, Util.mc.player.posY, Util.mc.player.posZ) <= 1.5) || this.self.getValue()) {
                    posList.add(blockPos);
                }

            }
        }
    }

    @Override
    public void onUpdate() {
        if (BurrowESP.mc.player == null || BurrowESP.mc.world == null) {
            return;
        }
        if (burrowAlert.getValue().booleanValue()) {
            for (Entity entity : BurrowESP.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer).collect(Collectors.toList())) {
                if (!(entity instanceof EntityPlayer)) continue;
                if (!this.burrowedPlayers.contains(entity) && this.isBurrowed(entity)) {
                    this.burrowedPlayers.add(entity);
                    Command.sendMessage(ChatFormatting.RED + entity.getName() + " has just burrowed!");
                    continue;
                }
                if (!this.burrowedPlayers.contains(entity) || this.isBurrowed(entity)) continue;
                this.burrowedPlayers.remove(entity);
                Command.sendMessage(ChatFormatting.GREEN + entity.getName() + " is no longer burrowed!");
            }
        }
    }

    private boolean isBurrowed(Entity entity) {
        BlockPos entityPos = new BlockPos(this.roundValueToCenter(entity.posX), entity.posY + 0.2, this.roundValueToCenter(entity.posZ));
        return BurrowESP.mc.world.getBlockState(entityPos).getBlock() == Blocks.OBSIDIAN || BurrowESP.mc.world.getBlockState(entityPos).getBlock() == Blocks.ENDER_CHEST;
    }

    private double roundValueToCenter(double inputVal) {
        double roundVal = Math.round(inputVal);
        if (roundVal > inputVal) {
            roundVal -= 0.5;
        } else if (roundVal <= inputVal) {
            roundVal += 0.5;
        }
        return roundVal;
    }


    @Override
    public void onRender3D(Render3DEvent event) {
        for (BlockPos bp : posList) {
            String text = textString.getValue().toUpperCase();
            if (this.text.getValue().booleanValue() && customText.getValue()) {
                renderUtil.drawText(bp, text, rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), outlineAlpha.getValue()));
            } else {
                RenderUtil.drawText(bp, Util.mc.player.getGameProfile().getName());
            }
            RenderUtil.drawBoxESP(bp, rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), outlineAlpha.getValue()), 1.5F, true, true, alpha.getValue());
        }
    }
}
