package org.confluence.terraentity.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terraentity.mixed.IMobEffectExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onEffectAdded", at = @At("HEAD"), cancellable = true)
    private void addEffect(MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfo ci) {
        if(pEffectInstance.getEffect() instanceof IMobEffectExtension extension){
            extension.onEffectStarted((LivingEntity) (Object) this, pEffectInstance.getAmplifier());
        }
    }

    @Inject(method = "onEffectUpdated", at = @At("HEAD"), cancellable = true)
    private void updateEffect(MobEffectInstance pEffectInstance, boolean pForced, Entity pEntity, CallbackInfo ci) {
        if(pEffectInstance.getEffect() instanceof IMobEffectExtension extension){
            extension.onEffectStarted((LivingEntity) (Object) this, pEffectInstance.getAmplifier());
        }
    }
}
