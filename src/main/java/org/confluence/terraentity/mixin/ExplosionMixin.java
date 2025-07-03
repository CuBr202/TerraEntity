package org.confluence.terraentity.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import org.confluence.terraentity.mixed.IExplosionDamageCalculator;
import org.confluence.terraentity.mixed.SelfGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Explosion.class)
public class ExplosionMixin implements SelfGetter<Explosion> {


    @Shadow @Final private ExplosionDamageCalculator damageCalculator;

    @WrapOperation(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    public boolean explode(Entity instance, DamageSource pSource, float pAmount, Operation<Boolean> original) {
        if(((this.damageCalculator instanceof IExplosionDamageCalculator diy))){
            if(diy.shouldDamageEntity(te$getSelf(), instance)) {
                return instance.hurt(pSource, diy.getEntityDamageAmount(te$getSelf(), instance, pAmount));
            } else{
                return false;
            }
        }
        return instance.hurt(pSource, pAmount);
    }

}
