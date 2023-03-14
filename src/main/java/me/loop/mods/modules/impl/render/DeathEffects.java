package me.loop.mods.modules.impl.render;

import me.loop.mods.modules.Category;
import me.loop.mods.modules.Module;
import me.loop.mods.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;


public class DeathEffects extends Module {

    public Setting<Boolean> thunder = this.add(new Setting<Boolean>("Thunder", true));
    public Setting<Integer> numbersThunder = this.add(new Setting<Integer>("Number Thunder", 1, 1, 10));
    public Setting<Boolean> sound = this.add(new Setting<Boolean>("Sound", true));
    public Setting<Integer> numberSound = this.add(new Setting<Integer>("Number Sound", 1, 1, 10));
    public Setting<Integer> timeActive = this.add(new Setting<Integer>("TimeActive", 20, 0, 50));
    public Setting<Boolean> lightning = this.add(new Setting<Boolean>("Lightning", true));
    public Setting<Boolean> totemPop = this.add(new Setting<Boolean>("TotemPop", true));
    public Setting<Boolean> totemPopSound = this.add(new Setting<Boolean>("TotemPopSound", false));
    public Setting<Boolean> firework = this.add(new Setting<Boolean>("FireWork", false));
    public Setting<Boolean> fire = this.add(new Setting<Boolean>("Fire", false));
    public Setting<Boolean> water = this.add(new Setting<Boolean>("Water", false));
    public Setting<Boolean> smoke = this.add(new Setting<Boolean>("Smoke", false));
    public Setting<Boolean> players = this.add(new Setting<Boolean>("Players", true));
    public Setting<Boolean> animals = this.add(new Setting<Boolean>("Animals", true));
    public Setting<Boolean> mobs = this.add(new Setting<Boolean>("Mobs", true));
    public Setting<Boolean> all = this.add(new Setting<Boolean>("All", true));
    ArrayList<EntityPlayer> playersDead = new ArrayList();
    final Object sync = new Object();

    public DeathEffects() {
        super("DeathEffects", "Spawn a thunder to the loser", Category.RENDER);

    }

    @Override
    public void onEnable() {
        this.playersDead.clear();
    }

    @Override
    public void onUpdate() {
        if (DeathEffects.mc.world == null) {
            this.playersDead.clear();
            return;
        }
        DeathEffects.mc.world.playerEntities.forEach(entity -> {
            if (this.playersDead.contains(entity)) {
                if (entity.getHealth() > 0.0f) {
                    this.playersDead.remove(entity);
                }
            } else if (entity.getHealth() == 0.0f) {
                int i;
                if (this.thunder.getValue().booleanValue()) {
                    for (i = 0; i < this.numbersThunder.getValue(); ++i) {
                        DeathEffects.mc.world.spawnEntity((Entity)new EntityLightningBolt((World)DeathEffects.mc.world, entity.posX, entity.posY, entity.posZ, true));
                    }
                }
                if (this.sound.getValue().booleanValue()) {
                    for (i = 0; i < this.numberSound.getValue(); ++i) {
                        DeathEffects.mc.player.playSound(SoundEvents.ENTITY_LIGHTNING_THUNDER, 0.5f, 1.0f);
                    }
                }
                this.playersDead.add((EntityPlayer)entity);
            }
        });
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() == DeathEffects.mc.player) {
            return;
        }
        if (this.shouldRenderParticle(event.getEntity())) {
            if (this.lightning.getValue().booleanValue()) {
                DeathEffects.mc.world.addEntityToWorld(-999, (Entity)new EntityLightningBolt((World)DeathEffects.mc.world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, true));
            }
            if (this.totemPop.getValue().booleanValue()) {
                this.totemPop(event.getEntity());
            }
            if (this.firework.getValue().booleanValue()) {
                DeathEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.FIREWORKS_SPARK, this.timeActive.getValue().intValue());
            }
            if (this.fire.getValue().booleanValue()) {
                DeathEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.FLAME, this.timeActive.getValue().intValue());
                DeathEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.DRIP_LAVA, this.timeActive.getValue().intValue());
            }
            if (this.water.getValue().booleanValue()) {
                DeathEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.WATER_BUBBLE, this.timeActive.getValue().intValue());
                DeathEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.WATER_DROP, this.timeActive.getValue().intValue());
            }
            if (this.smoke.getValue().booleanValue()) {
                DeathEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.SMOKE_NORMAL, this.timeActive.getValue().intValue());
            }
        }
    }

    public boolean shouldRenderParticle(Entity entity) {
        return entity != DeathEffects.mc.player && (this.all.getValue() != false || entity instanceof EntityPlayer && this.players.getValue() != false || entity instanceof EntityMob || entity instanceof EntitySlime && this.mobs.getValue() != false || entity instanceof EntityAnimal && this.animals.getValue() != false);
    }

    public void totemPop(Entity entity) {
        DeathEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.TOTEM, this.timeActive.getValue().intValue());
        if (this.totemPopSound.getValue().booleanValue()) {
            DeathEffects.mc.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0f, 1.0f, false);
        }
    }
}