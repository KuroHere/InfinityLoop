package com.me.infinity.loop.features.modules.render;

import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ShadowESP
        extends Module {
    public ShadowESP() {
        super("ShadowEsp","ShadowEsp", ModuleCategory.RENDER);
    }

    @Override
    public void onDisable() {
        for (EntityPlayer player : ShadowESP.mc.world.playerEntities) {
            if (!player.isGlowing()) continue;
            player.setGlowing(false);
        }
        super.onDisable();
        for (Entity entity : ShadowESP.mc.world.playerEntities) {
            if (!entity.isGlowing()) continue;
            entity.setGlowing(false);
        }
        super.onDisable();
    }

    @Override
    public void onUpdate() {
        for (Entity player : ShadowESP.mc.world.loadedEntityList) {
            if (!(player instanceof EntityPlayer)) continue;
            player.setGlowing(true);
        }
        for (Entity entity : ShadowESP.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityPlayer)) continue;
            entity.setGlowing(true);
        }
    }
}
