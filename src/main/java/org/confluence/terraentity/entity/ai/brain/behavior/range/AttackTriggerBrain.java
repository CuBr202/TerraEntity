package org.confluence.terraentity.entity.ai.brain.behavior.range;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.confluence.terraentity.init.TEAi;

/**
 * 触发攻击行为
 */
public class AttackTriggerBrain<T extends LivingEntity> extends Behavior<T> {
    float detectDistance;
    public AttackTriggerBrain(float detectDistance) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.NEAREST_HOSTILE, MemoryStatus.VALUE_PRESENT
        ));
        this.detectDistance = detectDistance;
    }

    @Override
    protected void start(ServerLevel level, T living, long gameTimeIn) {
        Brain<?> brain = living.getBrain();
        var memory = brain.getMemory(MemoryModuleType.NEAREST_HOSTILE);
        if(memory.isPresent()) {
            LivingEntity target = memory.get();
            boolean shouldAdd = target.distanceToSqr(living) < getDetectDistanceSqr(living) && target.isAlive();
            if (shouldAdd) {
                brain.setMemory(MemoryModuleType.ATTACK_TARGET, target);
                brain.setActiveActivityIfPossible(TEAi.Activities.RANGE_ATTACK);
            }
        }
    }

    protected float getDetectDistanceSqr(T living) {
        return detectDistance * detectDistance;
    }
}
