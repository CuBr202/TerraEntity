package org.confluence.terraentity.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RandomStrollGoal.class)
public class RandomStrollGoalMixin {

    @Shadow @Final protected PathfinderMob mob;

    @ModifyExpressionValue(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PathfinderMob;isVehicle()Z"))
    private boolean canUseMixin(boolean original) {
        // 史莱姆控制僵尸时不限制僵尸行走
        return mob.hasControllingPassenger();
    }

    @ModifyExpressionValue(method = "canContinueToUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PathfinderMob;isVehicle()Z"))
    private boolean canContinueToUseMixin(boolean original) {
        // 史莱姆控制僵尸时不限制僵尸行走
        return mob.hasControllingPassenger();
    }
}
