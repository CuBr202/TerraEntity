package org.confluence.terraentity.entity.npc.brain;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

/**
 * 攻击行为清除
 */
public class AttackCalmDownBrain extends Behavior<LivingEntity> {
    public AttackCalmDownBrain() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected void start(ServerLevel level, LivingEntity living, long gameTimeIn) {
        Brain<?> brain = living.getBrain();
        LivingEntity target = brain.getMemory(MemoryModuleType.ATTACK_TARGET).get();
        boolean shouldRemove = target.distanceToSqr(living) > 15 * 15 || !target.isAlive();
        if (shouldRemove) {
            brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
            brain.updateActivityFromSchedule(level.getDayTime(), gameTimeIn);
        }
    }
}
