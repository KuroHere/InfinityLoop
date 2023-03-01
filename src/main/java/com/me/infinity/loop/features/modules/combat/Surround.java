package com.me.infinity.loop.features.modules.combat;

import com.me.infinity.loop.features.command.Command;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.util.utils.maths.MathUtil;
import com.me.infinity.loop.util.utils.minecraft.BlockUtil;
import com.me.infinity.loop.util.utils.minecraft.InventoryUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;

public class Surround
        extends Module {
    public int placements;
    public BlockPos startPosition;
    public int tries;
    public Setting<Modes> mode = this.register(new Setting<>("Mode", Modes.Normal));
    public Setting<Enum> item = this.register(new Setting<>("Item", InventoryUtil.The_Item.Obsidian));
    public Setting<Enum> switchMode = this.register(new Setting<>("Switch", InventoryUtil.Switch.NORMAL));
    public Setting<Integer> blocks =  this.register(new Setting<>("Blocks", 8, 1, 40));
    public Setting<Enum> supportBlocks = this.register(new Setting<>("SupportBlocks", Supports.Dynamic));
    public Setting<Integer> retries = this.register(new Setting<>("Retries", 5, 0, 20));
    public Setting<Boolean> dynamic = this.register(new Setting<>("Dynamic", true));
    public Setting<Boolean> center = this.register(new Setting<>("Center", false));
    public Setting<Boolean> rotate = this.register(new Setting<>("Rotate", false));
    public Setting<Boolean> floor = this.register(new Setting<>("Floor", false));

    public Surround() {
        super("Surround", "Places blocks around your feet to protect you from crystals.", ModuleCategory.COMBAT);
    }

    @Override
    public void onMotionUpdate() {
        block8: {
            if ((double)this.startPosition.getY() != MathUtil.roundToPlaces(Surround.mc.player.posY, 0) && this.mode.getValue().equals((Object)Modes.Normal)) {
                this.disable();
                return;
            }
            int slot = InventoryUtil.getCombatBlock(this.item.getValue().toString());
            int lastSlot = Surround.mc.player.inventory.currentItem;
            if (slot == -1) {
                Command.sendMessage("No blocks could be found.");
                this.disable();
                return;
            }
            if (!this.getUnsafeBlocks().isEmpty()) {
                InventoryUtil.switchSlot(slot, this.switchMode.getValue().equals((Object)InventoryUtil.Switch.SILENT));
                for (BlockPos position : this.getUnsafeBlocks()) {
                    if (!this.supportBlocks.getValue().equals((Object)Supports.None)) {
                        if ((BlockUtil.getPlaceableSide(position) == null || this.supportBlocks.getValue().equals((Object)Supports.Static)) && BlockUtil.isPositionPlaceable(position.down(), true, true)) {
                            this.placeBlock(position.down());
                        }
                    }
                    if (!BlockUtil.isPositionPlaceable(position, true, true, this.tries <= this.retries.getValue().intValue())) continue;
                    this.placeBlock(position);
                    ++this.tries;
                }
                if (!this.switchMode.getValue().equals((Object)InventoryUtil.Switch.NONE)) {
                    InventoryUtil.switchSlot(lastSlot, this.switchMode.getValue().equals((Object)InventoryUtil.Switch.SILENT));
                }
            }
            this.placements = 0;
            if (!this.getUnsafeBlocks().isEmpty()) break block8;
            this.tries = 0;
            if (this.mode.getValue().equals((Object)Modes.Toggle)) {
                this.disable();
            }
        }
    }

    public List<BlockPos> getOffsets() {
        ArrayList<BlockPos> offsets = new ArrayList<BlockPos>();
        if (this.dynamic.getValue()) {
            int z;
            int x;
            double decimalX = Math.abs(Surround.mc.player.posX) - Math.floor(Math.abs(Surround.mc.player.posX));
            double decimalZ = Math.abs(Surround.mc.player.posZ) - Math.floor(Math.abs(Surround.mc.player.posZ));
            int lengthX = this.calculateLength(decimalX, false);
            int negativeLengthX = this.calculateLength(decimalX, true);
            int lengthZ = this.calculateLength(decimalZ, false);
            int negativeLengthZ = this.calculateLength(decimalZ, true);
            ArrayList<BlockPos> tempOffsets = new ArrayList<BlockPos>();
            offsets.addAll(this.getOverlapPositions());
            for (x = 1; x < lengthX + 1; ++x) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), x, 1 + lengthZ));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), x, -(1 + negativeLengthZ)));
            }
            for (x = 0; x <= negativeLengthX; ++x) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -x, 1 + lengthZ));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -x, -(1 + negativeLengthZ)));
            }
            for (z = 1; z < lengthZ + 1; ++z) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), 1 + lengthX, z));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -(1 + negativeLengthX), z));
            }
            for (z = 0; z <= negativeLengthZ; ++z) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), 1 + lengthX, -z));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -(1 + negativeLengthX), -z));
            }
            offsets.addAll(tempOffsets);
        } else {
            for (EnumFacing side : EnumFacing.HORIZONTALS) {
            }
        }
        return offsets;
    }

    public BlockPos getPlayerPosition() {
        return new BlockPos(Surround.mc.player.posX, Surround.mc.player.posY - Math.floor(Surround.mc.player.posY) > Double.longBitsToDouble(Double.doubleToLongBits(19.39343307331816) ^ 0x7FDAFD219E3E896DL) ? Math.floor(Surround.mc.player.posY) + Double.longBitsToDouble(Double.doubleToLongBits(4.907271931218261) ^ 0x7FE3A10BE4A4A510L) : Math.floor(Surround.mc.player.posY), Surround.mc.player.posZ);
    }

    public List<BlockPos> getOverlapPositions() {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        int offsetX = this.calculateOffset(Surround.mc.player.posX - Math.floor(Surround.mc.player.posX));
        int offsetZ = this.calculateOffset(Surround.mc.player.posZ - Math.floor(Surround.mc.player.posZ));
        positions.add(this.getPlayerPosition());
        for (int x = 0; x <= Math.abs(offsetX); ++x) {
            for (int z = 0; z <= Math.abs(offsetZ); ++z) {
                int properX = x * offsetX;
                int properZ = z * offsetZ;
                positions.add(this.getPlayerPosition().add(properX, -1, properZ));
            }
        }
        return positions;
    }

    public BlockPos addToPosition(final BlockPos position, double x, double z) {
        if (position.getX() < 0) {
            x = -x;
        }
        if (position.getZ() < 0) {
            z = -z;
        }
        return position.add(x, Double.longBitsToDouble(Double.doubleToLongBits(1.4868164896774578E308) ^ 0x7FEA7759ABE7F7C1L), z);
    }


    /*
     * WARNING - void declaration
     */
    public int calculateOffset(double d) {
        double dec = 0;
        return dec >= Double.longBitsToDouble(Double.doubleToLongBits(22.19607388697261) ^ 0x7FD05457839243F9L) ? 1 : (dec <= Double.longBitsToDouble(Double.doubleToLongBits(7.035587642812949) ^ 0x7FCF1742257B24DBL) ? -1 : 0);
    }

    /*
     * WARNING - void declaration
     */
    public int calculateLength(double d, boolean bl) {
        double decimal = 0;
        double negative = 0;
        if (negative != 1) {
            return decimal <= Double.longBitsToDouble(Double.doubleToLongBits(30.561776836994962) ^ 0x7FEDBCE3A865B81CL) ? 1 : 0;
        }
        return decimal >= Double.longBitsToDouble(Double.doubleToLongBits(22.350511399288944) ^ 0x7FD03FDD7B12B45DL) ? 1 : 0;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (Surround.mc.player == null || Surround.mc.world == null) {
            this.disable();
            return;
        }
        this.startPosition = new BlockPos(MathUtil.roundVector(Surround.mc.player.getPositionVector(), 0));
    }

    /*
     * WARNING - void declaration
     */
    public void placeBlock(BlockPos blockPos) {
        if (this.placements < this.blocks.getValue().intValue()) {
            BlockUtil.placeBlock((BlockPos) blockPos, EnumHand.MAIN_HAND, true);
            ++this.placements;
        }
    }

    public List<BlockPos> getUnsafeBlocks() {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        for (BlockPos position : this.getOffsets()) {
            if (this.isSafe(position)) continue;
            positions.add(position);
        }
        return positions;
    }

    /*
     * WARNING - void declaration
     */
    public boolean isSafe(BlockPos blockPos) {
        return !Surround.mc.world.getBlockState((BlockPos)blockPos).getBlock().isReplaceable((IBlockAccess)Surround.mc.world, (BlockPos)blockPos);
    }

    @Override
    public String getHudInfo() {
        return " " + this.tries + "/" + this.retries.getValue().intValue();
    }

    public enum Supports {
        None,
        Dynamic,
        Static;

    }

    public enum Modes {
        Normal,
        Persistent,
        Toggle,
        Shift;

    }
}