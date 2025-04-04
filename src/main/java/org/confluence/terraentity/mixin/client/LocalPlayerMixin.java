package org.confluence.terraentity.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.PlayerRideableJumping;
import org.confluence.terraentity.entity.ai.IFlyRideableMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Shadow @Nullable public abstract PlayerRideableJumping jumpableVehicle();

    @Shadow private int jumpRidingTicks;

    @Shadow private float jumpRidingScale;

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;aiStep()V"), cancellable = true)
    public void aiStep(CallbackInfo ci, @Local PlayerRideableJumping jumping) {
        if(jumpableVehicle() instanceof IFlyRideableMob mob){
            this.jumpRidingScale = mob.calJumpingScale(jumpRidingTicks);
        }

    }

}
