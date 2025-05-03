package org.confluence.terraentity.entity.summon;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.Optional;
import java.util.UUID;

public class SummonFinch  extends AbstractSummonMob<SummonFinch> {
    public SummonFinch(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public EntityDataAccessor<Optional<UUID>> get_DATA_OWNERUUID_ID() {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
