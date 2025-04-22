package org.confluence.terraentity.mixin;

import net.minecraft.world.level.ExplosionDamageCalculator;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExplosionDamageCalculator.class)
public class ExplosionDamageCalculatorMixin {

//    public float terraEntity$getEntityDamageAmount(Explosion explosion, Entity entity, double original) {
//
//        return (float) original;
//    }
//
//    public boolean terraEntity$shouldDamageEntity(Explosion explosion, Entity entity) {
//        return true;
//    }
}
