package com.me.infinity.loop.features.modules.render;

import com.me.infinity.loop.event.events.UpdateWalkingPlayerEvent;
import com.me.infinity.loop.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;

public class ShadowESP
        extends Module {
    public ShadowESP() {
        super("ShadowEsp","ShadowEsp", Category.RENDER, true, false, false);
    }

    @Override
    public void onDisable() {
        for (EntityPlayer player : ShadowESP.mc.world.playerEntities) {
            if (!player.isGlowing()) continue;
            player.setGlowing(false);
        }
        super.onDisable();
    }

    @Override
    public void onUpdate() {
        for (Entity player : ShadowESP.mc.world.loadedEntityList) {
            if (!(player instanceof EntityPlayer)) continue;
            player.setGlowing(true);
        }
    }
}
