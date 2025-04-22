package org.confluence.terraentity.mixed;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;

public interface IExplosionDamageCalculator {

    default boolean shouldDamageEntity(Explosion explosion, Entity entity){
        return true;
    }

    float getEntityDamageAmount(Explosion explosion, Entity entity, double original);

}
