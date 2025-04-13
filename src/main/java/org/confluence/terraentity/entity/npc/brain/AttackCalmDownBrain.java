package org.confluence.terraentity.entity.npc.brain;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

/**
 * 攻击行为清除
 */
public class AttackCalmDownBrain extends Behavior<Mob> {
    float distanceToRemove;
    public AttackCalmDownBrain(float distanceToRemove) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
        this.distanceToRemove = distanceToRemove * distanceToRemove;
    }

    @Override
    protected void start(ServerLevel level, Mob living, long gameTimeIn) {
        Brain<?> brain = living.getBrain();
        LivingEntity target = brain.getMemory(MemoryModuleType.ATTACK_TARGET).get();
        // 距离过远，目标死亡，没有视线
        boolean shouldRemove = target.distanceToSqr(living) > distanceToRemove || !target.isAlive();
        if (shouldRemove) {
            brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
            brain.updateActivityFromSchedule(level.getDayTime(), gameTimeIn);
        }
    }
}
