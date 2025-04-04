package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.entity.rideable.RideableBee;
import org.confluence.terraentity.entity.rideable.RideableSlime;
import org.confluence.terraentity.init.TEEntities;

public class TERideableEntities {
    // 坐骑
    public static final DeferredHolder<EntityType<?>, EntityType<RideableSlime>> RIDEABLE_SLIME = TEEntities.registerEntity("rideable_slime", RideableSlime::new,0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<RideableBee>> RIDEABLE_BEE = TEEntities.registerEntity("rideable_bee", RideableBee::new,0.5F,0.5F);

    public static void register(){

    }
}
