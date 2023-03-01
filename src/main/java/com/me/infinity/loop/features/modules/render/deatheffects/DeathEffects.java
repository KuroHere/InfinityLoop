package com.me.infinity.loop.features.modules.render.deatheffects;

import com.me.infinity.loop.event.events.network.EventDeath;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.ModuleCategory;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.util.utils.worlds.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class DeathEffects extends Module {
    private final Setting<Boolean> sound = this.register(new Setting<>("Sound", false));
    private final Setting<Boolean> particles = this.register(new Setting("Particles", false));
    private final Setting<Moderator> mode = this.register(new Setting("Mode", Moderator.SMOKE));
    private final Setting<Float> yOffset = this.register(new Setting("YPos offset", 0.0, 0.0, 2.0, v -> this.particles.getValue().booleanValue()));

    public DeathEffects() {
        super("DeathEffects", "Spawn a thunder to the loser", ModuleCategory.RENDER);

    }

    private Timer timer = new Timer();

    public static void Renderparticle(final EntityLivingBase entity, final String s, final double yoffset) {
        try {
            switch (s) {
                case "HEART": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.HEART, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                }
                case "SWORD": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.CRIT, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                }
                case "REDSTONE": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.REDSTONE, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                }
                case "MAGIC": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                }
                case "LAVA": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.LAVA, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.DRIP_LAVA, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                }
                case "SMOKE": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                }
                case "CLOUD": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.CLOUD, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                }
                case "FLAME": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.FLAME, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                }
                case "SLIME": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.SLIME, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                }
                case "EXPLOSION": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                }
                case "WATER": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                }
                case "FIREWORK": {
                    DeathEffects.mc.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, entity.posX, entity.posY + 0.01 + yoffset, entity.posZ, entity.motionX * 0.4, entity.motionY * 0.4, entity.motionZ * 0.4, new int[0]);
                    break;
                }
            }
        } catch (Exception ex) {
        }
    }

    @SubscribeEvent
    public void onDeath(EventDeath event) {
        if (!nullCheck() && event.player != null) {
            Entity entity = event.player;
            if (entity != null) {
                if (entity.isDead || ((EntityPlayer) entity).getHealth() <= 0 && timer.passedMs(1500)) {
                    mc.world.spawnEntity(new EntityLightningBolt(mc.world, entity.posX, entity.posY, entity.posZ, true));
                    if (sound.getValue()) {
                        mc.player.playSound(SoundEvents.ENTITY_LIGHTNING_THUNDER, 0.5f, 1.f);
                        timer.reset();
                    }
                    /*for (int i = 0; i < 5; ++i) {
                        DeathEffects.Renderparticle((EntityLivingBase) entity, String.valueOf(mode.getValue()), this.yOffset.getValue().floatValue());*/

                }
            }
        }
    }
}
