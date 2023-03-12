package me.loop.client.modules.impl.combat;

import me.loop.api.events.impl.player.EventMotionUpdate;
import me.loop.api.managers.Managers;
import me.loop.api.utils.impl.DamageUtil;
import me.loop.api.utils.impl.EntityUtil;
import me.loop.api.utils.impl.maths.MathUtil;
import me.loop.api.utils.impl.worlds.Timer;
import me.loop.api.modules.Module;
import me.loop.client.modules.Category;
import me.loop.client.modules.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Killaura extends Module {
    public static Entity target;
    private final Timer timer = new Timer();
    public Setting<Float> range = add(new Setting("Range", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F)));
    public Setting<Boolean> delay = add(new Setting("HitDelay", Boolean.valueOf(true)));
    public Setting<Boolean> rotate = add(new Setting("Rotate", Boolean.valueOf(true)));
    public Setting<Boolean> onlySharp = add(new Setting("SwordOnly", Boolean.valueOf(true)));
    public Setting<Float> raytrace = add(new Setting("Raytrace", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F), "Wall Range."));
    public Setting<Boolean> players = add(new Setting("Players", Boolean.valueOf(true)));
    public Setting<Boolean> mobs = add(new Setting("Mobs", Boolean.valueOf(false)));
    public Setting<Boolean> animals = add(new Setting("Animals", Boolean.valueOf(false)));
    public Setting<Boolean> vehicles = add(new Setting("Entities", Boolean.valueOf(false)));
    public Setting<Boolean> projectiles = add(new Setting("Projectiles", Boolean.valueOf(false)));
    public Setting<Boolean> tps = add(new Setting("TpsSync", Boolean.valueOf(true)));
    public Setting<Boolean> packet = add(new Setting("Packet", Boolean.valueOf(false)));

    public Killaura() {
        super("Killaura", "Kills aura.", Category.COMBAT);
    }

    public void onTick() {
        if (!this.rotate.getValue().booleanValue())
            doKillaura();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(EventMotionUpdate event) {
        if (event.getStage() == 0 && this.rotate.getValue().booleanValue())
            doKillaura();
    }

    private void doKillaura() {
        if (this.onlySharp.getValue().booleanValue() && !EntityUtil.holdingWeapon(mc.player)) {
            target = null;
            return;
        }
        int wait = !this.delay.getValue().booleanValue() ? 0 : (int) (DamageUtil.getCooldownByWeapon(mc.player) * (this.tps.getValue().booleanValue() ? Managers.serverManager.getTpsFactor() : 1.0F));
        if (!this.timer.passedMs(wait))
            return;
        target = getTarget();
        if (target == null)
            return;
        if (this.rotate.getValue().booleanValue())
            Managers.rotationManager.lookAtEntity(target);
        EntityUtil.attackEntity(target, this.packet.getValue().booleanValue(), true);
        this.timer.reset();
    }

    private Entity getTarget() {
        Entity target = null;
        double distance = this.range.getValue().floatValue();
        double maxHealth = 36.0D;
        for (Entity entity : mc.world.playerEntities) {
            if (((!this.players.getValue().booleanValue() || !(entity instanceof EntityPlayer)) && (!this.animals.getValue().booleanValue() || !EntityUtil.isPassive(entity)) && (!this.mobs.getValue().booleanValue() || !EntityUtil.isMobAggressive(entity)) && (!this.vehicles.getValue().booleanValue() || !EntityUtil.isVehicle(entity)) && (!this.projectiles.getValue().booleanValue() || !EntityUtil.isProjectile(entity))) || (entity instanceof net.minecraft.entity.EntityLivingBase &&
                    EntityUtil.isntValid(entity, distance)))
                continue;
            if (!mc.player.canEntityBeSeen(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && mc.player.getDistanceSq(entity) > MathUtil.square(this.raytrace.getValue().floatValue()))
                continue;
            if (target == null) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
                continue;
            }
            if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer) entity, 18)) {
                target = entity;
                break;
            }
            if (mc.player.getDistanceSq(entity) < distance) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
            if (EntityUtil.getHealth(entity) < maxHealth) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
        }
        return target;
    }

    public String getDisplayInfo() {
        if (target instanceof EntityPlayer)
            return target.getName();
        return null;
    }
}
