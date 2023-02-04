package com.me.infinity.loop.features.modules.render;

/*import javafx.util.Pair;
import com.me.alpha432.oyvey.event.events.ProcessRightClickBlockEvent;
import com.me.alpha432.oyvey.event.events.Render3DEvent;
import com.me.alpha432.oyvey.features.modules.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BreakHighlight extends Module {

    ColourSetting self = new ColourSetting("SelfColour", new Colour(255, 255, 255, 200), this);
    ColourSetting other = new ColourSetting("OtherColour", new Colour(255, 0, 0), this);
    EnumSetting renderMode = new EnumSetting("Mode", "Both", Arrays.asList("Outline", "Both", "Fill"), this);
    HashMap<Integer, Pair<Integer, BlockPos>> breakingBlockList = new HashMap<>();

    public BreakHighlight() {
        super("BreakESP", "highlights where people are breaking ", Category.RENDER, true, false, false);
    }

    @Override
    public void onEnable() {
        breakingBlockList.clear();
    }


    public void damageBlockEvent(ProcessRightClickBlockEvent event) {
        if (mc.world.getBlockState(event.pos).getBlock() == Blocks.BEDROCK) return;
        if (breakingBlockList.isEmpty()) {
            breakingBlockList.putIfAbsent(event.breakingID, new Pair<>(event.breakStage, event.pos));
        } else {
            HashMap<Integer, Pair<Integer, BlockPos>> shitToAdd = new HashMap<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (int id : breakingBlockList.keySet()) {
                Pair<Integer, BlockPos> pair = breakingBlockList.get(id);
                if (event.breakingID != id) {
                    shitToAdd.put(event.breakingID, new Pair<>(event.breakStage, event.pos));
                } else {
                    if (event.breakStage > pair.getKey()) {
                        idsToRemove.add(event.breakingID);
                        shitToAdd.put(event.breakingID, new Pair<>(event.breakStage, event.pos));
                    }
                    if (event.breakingID == id && event.pos != pair.getValue()) {
                        idsToRemove.add(id);
                    }
                    if (event.breakingID == id && event.breakStage < pair.getKey()) {
                        idsToRemove.add(id);
                    }
                }
            }
            for (int id : idsToRemove) {
                breakingBlockList.remove(id);
            }
            breakingBlockList.putAll(shitToAdd);
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        for (int i : breakingBlockList.keySet()) {
            if (breakingBlockList.get(i).getValue() == null) continue;
            BlockPos pos = breakingBlockList.get(i).getValue();
            int state = breakingBlockList.get(i).getKey();
            EntityPlayer player = (EntityPlayer) mc.world.getEntityByID(i);
            if (pos != null && state != -1 && mc.world.getBlockState(pos).getBlock() != Blocks.AIR) {
                AxisAlignedBB bb = mc.world.getBlockState(pos).getSelectedBoundingBox(mc.world, pos);
                bb = calcBB(bb, state);
                Color color;
                if (player == mc.player) {
                    colourSetting = self;
                } else {
                    colourSetting = other;
                }

                switch (renderMode.getValue()) {
                    case "Both":
                        RenderUtil.drawBBBox(bb, colourSetting.getValue(), colourSetting.getValue().getAlpha());
                        RenderUtil.drawBlockOutlineBB(bb, new Color(colourSetting.getValue().getRed(), colourSetting.getValue().getGreen(), colourSetting.getValue().getBlue(), 255), 1f);
                        break;
                    case "Outline":
                        RenderUtil.drawBlockOutlineBB(bb, colourSetting.getValue(), 1f);
                        break;
                    case "Fill":
                        RenderUtil.drawBBBox(bb, colourSetting.getValue(), colourSetting.getValue().getAlpha());
                        break;
                }
                ;
            }
        }
    }


    private AxisAlignedBB calcBB(AxisAlignedBB bb, int state) {
        AxisAlignedBB rbb = bb;
        switch (state) {
            case 0:
                rbb = bb.shrink(0.6);
                break;
            case 1:
                rbb = bb.shrink(0.65);
                break;
            case 2:
                rbb = bb.shrink(0.7);
                break;
            case 3:
                rbb = bb.shrink(0.75);
                break;
            case 4:
                rbb = bb.shrink(0.8);
                break;
            case 5:
                rbb = bb.shrink(0.85);
                break;
            case 6:
                rbb = bb.shrink(0.9);
                break;
            case 7:
                rbb = bb.shrink(0.95);
                break;
            case 8:
                rbb = bb;
                break;
        }
        return rbb;
    }

    @Override
    public void onLogout() {
        breakingBlockList.clear();
    }
}*/
