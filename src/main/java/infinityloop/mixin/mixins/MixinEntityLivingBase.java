package infinityloop.mixin.mixins;

import infinityloop.features.modules.render.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value={EntityLivingBase.class})
public abstract class MixinEntityLivingBase
        extends Entity {
    @Shadow public abstract boolean isPotionActive(Potion potionIn);

    @Shadow @Nullable public abstract PotionEffect getActivePotionEffect(Potion potionIn);

    public MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "getArmSwingAnimationEnd", at = @At("HEAD"), cancellable = true)
    private void yesido(CallbackInfoReturnable<Integer> cir) {
        if(Animation.getInstance().isEnabled() && Animation.getInstance().swing.getValue()) {
            if(isPotionActive(MobEffects.HASTE)) cir.setReturnValue(Animation.getInstance().speed.getValue() - (getActivePotionEffect(MobEffects.HASTE).getAmplifier()));
            else cir.setReturnValue(isPotionActive(MobEffects.MINING_FATIGUE) ? Animation.getInstance().speed.getValue() + (getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier() + 1) * 2 : Animation.getInstance().speed.getValue());
        }
    }
}