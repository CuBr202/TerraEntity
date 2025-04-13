package org.confluence.terraentity.entity.ai.brain.sensor;

import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import net.minecraft.world.entity.monster.Enemy;

import java.util.Optional;
import java.util.Set;

public class NPCHostilesSensor extends NearestVisibleLivingEntitySensor {

    float range;

    public NPCHostilesSensor( float range) {
        this.range = range;
    }

    protected boolean isMatchingEntity(LivingEntity attacker, LivingEntity target) {
        return this.isHostile(target) && this.isClose(attacker, target);
    }

    private boolean isClose(LivingEntity attacker, LivingEntity target) {

        return target.distanceToSqr(attacker) <= (double)(range * range);
    }

    protected MemoryModuleType<LivingEntity> getMemory() {
        return MemoryModuleType.NEAREST_HOSTILE;
    }

    private boolean isHostile(LivingEntity entity) {
        return entity instanceof Enemy ;
    }


    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(this.getMemory());
    }

    protected void doTick(ServerLevel level, LivingEntity entity) {
        entity.getBrain().setMemory(this.getMemory(), this.getNearestEntity(entity));
    }

    private Optional<LivingEntity> getNearestEntity(LivingEntity entity) {
        return this.getVisibleEntities(entity)
                .flatMap(nearestVisibleLivingEntities ->
                        nearestVisibleLivingEntities.findClosest(living -> this.isMatchingEntity(entity, living))
                );
    }

    protected Optional<NearestVisibleLivingEntities> getVisibleEntities(LivingEntity entity) {
        return entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }
}
