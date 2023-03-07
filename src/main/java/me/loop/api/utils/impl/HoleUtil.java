package me.loop.api.utils.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class HoleUtil {
    public static final List<BlockPos> holeBlocks = Arrays.asList(new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1));
    private static Minecraft mc = Minecraft.getMinecraft();
    public static final Vec3d[] cityOffsets = new Vec3d[]{new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0)};

    public static boolean isInHole() {
        Vec3d playerPos = CombatUtil.interpolateEntity((Entity)HoleUtil.mc.player);
        BlockPos blockpos = new BlockPos(playerPos.x, playerPos.y, playerPos.z);
        int size = 0;
        for (BlockPos bPos : holeBlocks) {
            if (!CombatUtil.isHard(HoleUtil.mc.world.getBlockState(blockpos.add((Vec3i)bPos)).getBlock())) continue;
            ++size;
        }
        return size == 5;
    }

    public static BlockSafety isBlockSafe(Block block) {
        if (block == Blocks.BEDROCK) {
            return BlockSafety.UNBREAKABLE;
        }
        if (block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST || block == Blocks.ANVIL) {
            return BlockSafety.RESISTANT;
        }
        return BlockSafety.BREAKABLE;
    }

    public static HoleInfo isHole(BlockPos centreBlock, boolean onlyOneWide, boolean ignoreDown) {
        HoleInfo output = new HoleInfo();
        HashMap<BlockOffset, BlockSafety> unsafeSides = HoleUtil.getUnsafeSides(centreBlock);
        if (unsafeSides.containsKey((Object)BlockOffset.DOWN) && unsafeSides.remove((Object)BlockOffset.DOWN, (Object)BlockSafety.BREAKABLE) && !ignoreDown) {
            output.setSafety(BlockSafety.BREAKABLE);
            return output;
        }
        int size = unsafeSides.size();
        unsafeSides.entrySet().removeIf(entry -> entry.getValue() == BlockSafety.RESISTANT);
        if (unsafeSides.size() != size) {
            output.setSafety(BlockSafety.RESISTANT);
        }
        if ((size = unsafeSides.size()) == 0) {
            output.setType(HoleType.SINGLE);
            output.setCentre(new AxisAlignedBB(centreBlock));
            return output;
        }
        if (size == 1 && !onlyOneWide) {
            return HoleUtil.isDoubleHole(output, centreBlock, (BlockOffset)((Object)unsafeSides.keySet().stream().findFirst().get()));
        }
        output.setSafety(BlockSafety.BREAKABLE);
        return output;
    }
    
    public static BlockPos is2Hole(BlockPos pos) {
        if (isHole(pos))
            return null;
        BlockPos blockpos = pos;
        BlockPos blockpos2 = null;
        int size = 0;
        int size2 = 0;
        if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR)
            return null;
        for (BlockPos bPos : holeBlocks) {
            if (mc.world.getBlockState(blockpos.add((Vec3i)bPos)).getBlock() == Blocks.AIR && blockpos.add((Vec3i)bPos) != new BlockPos(bPos.getX(), bPos.getY() - 1, bPos.getZ())) {
                blockpos2 = blockpos.add((Vec3i)bPos);
                size++;
            }
        }
        if (size == 1) {
            for (BlockPos bPoss : holeBlocks) {
                if (mc.world.getBlockState(blockpos.add((Vec3i)bPoss)).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockpos.add((Vec3i)bPoss)).getBlock() == Blocks.OBSIDIAN)
                    size2++;
            }
            for (BlockPos bPoss : holeBlocks) {
                if (mc.world.getBlockState(blockpos2.add((Vec3i)bPoss)).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockpos2.add((Vec3i)bPoss)).getBlock() == Blocks.OBSIDIAN)
                    size2++;
            }
        }
        if (size2 == 8)
            return blockpos2;
        return null;
    }

    public static boolean isHole(BlockPos pos) {
        BlockPos blockpos = pos;
        int size = 0;
        for (BlockPos bPos : holeBlocks) {
            if (CombatUtil.isHard(mc.world.getBlockState(blockpos.add((Vec3i)bPos)).getBlock()))
                size++;
        }
        return (size == 5);
    }

    public static boolean is2securityHole(BlockPos pos) {
        if (is2Hole(pos) == null)
            return false;
        BlockPos blockpos = pos;
        BlockPos blockpos2 = is2Hole(pos);
        int size = 0;
        for (BlockPos bPoss : holeBlocks) {
            if (mc.world.getBlockState(blockpos.add((Vec3i)bPoss)).getBlock() == Blocks.BEDROCK)
                size++;
        }
        for (BlockPos bPoss : holeBlocks) {
            if (mc.world.getBlockState(blockpos2.add((Vec3i)bPoss)).getBlock() == Blocks.BEDROCK)
                size++;
        }
        return (size == 8);
    }

    private static HoleInfo isDoubleHole(HoleInfo info, BlockPos centreBlock, BlockOffset weakSide) {
        BlockPos unsafePos = weakSide.offset(centreBlock);
        HashMap<BlockOffset, BlockSafety> unsafeSides = HoleUtil.getUnsafeSides(unsafePos);
        int size = unsafeSides.size();
        unsafeSides.entrySet().removeIf(entry -> entry.getValue() == BlockSafety.RESISTANT);
        if (unsafeSides.size() != size) {
            info.setSafety(BlockSafety.RESISTANT);
        }
        if (unsafeSides.containsKey((Object)BlockOffset.DOWN)) {
            info.setType(HoleType.CUSTOM);
            unsafeSides.remove((Object)BlockOffset.DOWN);
        }
        if (unsafeSides.size() > 1) {
            info.setType(HoleType.NONE);
            return info;
        }
        double minX = Math.min(centreBlock.getX(), unsafePos.getX());
        double maxX = Math.max(centreBlock.getX(), unsafePos.getX()) + 1;
        double minZ = Math.min(centreBlock.getZ(), unsafePos.getZ());
        double maxZ = Math.max(centreBlock.getZ(), unsafePos.getZ()) + 1;
        info.setCentre(new AxisAlignedBB(minX, (double)centreBlock.getY(), minZ, maxX, (double)(centreBlock.getY() + 1), maxZ));
        if (info.getType() != HoleType.CUSTOM) {
            info.setType(HoleType.DOUBLE);
        }
        return info;
    }

    public static HashMap<BlockOffset, BlockSafety> getUnsafeSides(BlockPos pos) {
        HashMap<BlockOffset, BlockSafety> output = new HashMap<BlockOffset, BlockSafety>();
        BlockSafety temp = HoleUtil.isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.DOWN.offset(pos)).getBlock());
        if (temp != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.DOWN, temp);
        }
        if ((temp = HoleUtil.isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.NORTH.offset(pos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.NORTH, temp);
        }
        if ((temp = HoleUtil.isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.SOUTH.offset(pos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.SOUTH, temp);
        }
        if ((temp = HoleUtil.isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.EAST.offset(pos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.EAST, temp);
        }
        if ((temp = HoleUtil.isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.WEST.offset(pos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.WEST, temp);
        }
        return output;
    }

    public static enum BlockOffset {
        DOWN(0, -1, 0),
        UP(0, 1, 0),
        NORTH(0, 0, -1),
        EAST(1, 0, 0),
        SOUTH(0, 0, 1),
        WEST(-1, 0, 0);

        private final int x;
        private final int y;
        private final int z;

        private BlockOffset(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public BlockPos offset(BlockPos pos) {
            return pos.add(this.x, this.y, this.z);
        }

        public BlockPos forward(BlockPos pos, int scale) {
            return pos.add(this.x * scale, 0, this.z * scale);
        }

        public BlockPos backward(BlockPos pos, int scale) {
            return pos.add(-this.x * scale, 0, -this.z * scale);
        }

        public BlockPos left(BlockPos pos, int scale) {
            return pos.add(this.z * scale, 0, -this.x * scale);
        }

        public BlockPos right(BlockPos pos, int scale) {
            return pos.add(-this.z * scale, 0, this.x * scale);
        }
    }

    public static class HoleInfo {
        private HoleType type;
        private BlockSafety safety;
        private AxisAlignedBB centre;

        public HoleInfo() {
            this(BlockSafety.UNBREAKABLE, HoleType.NONE);
        }

        public HoleInfo(BlockSafety safety, HoleType type) {
            this.type = type;
            this.safety = safety;
        }

        public void setType(HoleType type) {
            this.type = type;
        }

        public void setSafety(BlockSafety safety) {
            this.safety = safety;
        }

        public void setCentre(AxisAlignedBB centre) {
            this.centre = centre;
        }

        public HoleType getType() {
            return this.type;
        }

        public BlockSafety getSafety() {
            return this.safety;
        }

        public AxisAlignedBB getCentre() {
            return this.centre;
        }
    }

    public static enum HoleType {
        SINGLE,
        DOUBLE,
        CUSTOM,
        NONE;

    }

    public static enum BlockSafety {
        UNBREAKABLE,
        RESISTANT,
        BREAKABLE;

    }
}
