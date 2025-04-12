package org.confluence.terraentity.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.mixed.SelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public abstract class MobMixin implements SelfGetter<Mob> {

    @ModifyExpressionValue(method = "getControllingPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;getFirstPassenger()Lnet/minecraft/world/entity/Entity;"))
    public Entity getControllingPassenger(Entity original) {
        if (original != null && original.getType().is(TETags.EntityTypes.NON_CONTROLLING_RIDER)){
            // 史莱姆骑僵尸时不会控制僵尸行为
            return null;
        }
        return original;
    }
}
