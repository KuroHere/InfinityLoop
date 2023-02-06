package com.me.infinity.loop.features.modules.render;
import com.me.infinity.loop.event.events.Render3DEvent;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.client.Colors;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.util.renders.ColorUtil;
import com.me.infinity.loop.util.renders.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BurrowESP extends Module {

    private static BurrowESP INSTANCE = new BurrowESP();

    public Setting<Integer> range = register(new Setting("Range", 20, 5, 50));
    public Setting<Boolean> self = register(new Setting("Self", true));
    public Setting<Boolean> text = register(new Setting("Text", true));
    public Setting<Boolean> customText = register(new Setting("CustomText", false, v -> this.text.getValue()));
    public Setting<String> textString = register(new Setting("TextString", "BURROW", v -> this.text.getValue() && this.customText.getValue()));
    public Setting<Boolean> rainbow = register(new Setting("Rainbow", false));
    public Setting<Integer> red = register(new Setting("Red", 0, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> green = register(new Setting("Green", 255, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> blue = register(new Setting("Blue", 0, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> alpha = register(new Setting("Alpha", 130, 0, 255));
    public Setting<Integer> outlineAlpha = register(new Setting("OL-Alpha", 250, 0, 255));

    private final List<BlockPos> posList = new ArrayList<>();

    private final RenderUtil renderUtil = new RenderUtil();

    public BurrowESP() {
        super("BurrowESP", "BURROWESP", Category.RENDER, true, false, false);
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

    public void onTick() {
        posList.clear();
        for (EntityPlayer player : mc.world.playerEntities) {
            BlockPos blockPos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY + 0.2), Math.floor(player.posZ));
            if ((mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= this.range.getValue()) {
                if (!(blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= 1.5) || this.self.getValue()) {
                    posList.add(blockPos);
                }

            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        for (BlockPos bp : posList) {
            String text = textString.getValue().toUpperCase();
            if (this.text.getValue().booleanValue() && customText.getValue()) {
                renderUtil.drawText(bp, text, rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), outlineAlpha.getValue()));
            } else {
                RenderUtil.drawText(bp, mc.player.getGameProfile().getName());
            }
            RenderUtil.drawBoxESP(bp, rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), outlineAlpha.getValue()), 1.5F, true, true, alpha.getValue());
        }
    }
}
