package com.me.infinity.loop.features.modules.combat;

import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.util.utils.minecraft.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class SelfFill
        extends Module {
    private final Setting<Boolean> packet = this.register(new Setting<Boolean>("PacketPlace", Boolean.FALSE));

    public SelfFill() {
        super("SelfFill", "SelfFills yourself in a hole.", ModuleCategory.COMBAT);
    }

    @Override
    public void onEnable() {
        SelfFill.mc.player.jump();
        SelfFill.mc.player.jump();
    }

    @Override
    public void onUpdate() {
        BlockPos pos = new BlockPos(SelfFill.mc.player.posX, SelfFill.mc.player.posY, SelfFill.mc.player.posZ);
        if (SelfFill.mc.world.getBlockState(pos.down()).getBlock() == Blocks.AIR && BlockUtil.isPositionPlaceable(pos.down(), true, true)) {
            BlockUtil.placeBlock(pos.down(), EnumHand.MAIN_HAND, false, this.packet.getValue(), false);
        }
        if (SelfFill.mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) {
            SelfFill.mc.player.connection.sendPacket(new CPacketPlayer.Position(SelfFill.mc.player.posX, SelfFill.mc.player.posY - 1.3, SelfFill.mc.player.posZ, false));
            SelfFill.mc.player.setPosition(SelfFill.mc.player.posX, SelfFill.mc.player.posY - 1.3, SelfFill.mc.player.posZ);
            this.toggle();
        }
    }
}

