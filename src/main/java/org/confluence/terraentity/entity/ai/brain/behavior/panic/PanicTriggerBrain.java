package org.confluence.terraentity.entity.ai.brain.behavior.panic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

/**
 * 恐慌触发器
 */
public class PanicTriggerBrain extends Behavior<LivingEntity> {
    public PanicTriggerBrain() {
        super(ImmutableMap.of());
    }

    protected boolean canStillUse(ServerLevel level, LivingEntity entity, long gameTime) {
        return isHurt(entity) || hasHostile(entity);
    }

    protected void start(ServerLevel level, LivingEntity entity, long gameTime) {
        if (isHurt(entity) || hasHostile(entity)) {
            Brain<?> brain = entity.getBrain();
            if (!brain.isActive(Activity.PANIC)) {
                brain.eraseMemory(MemoryModuleType.PATH);
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
                brain.eraseMemory(MemoryModuleType.BREED_TARGET);
                brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            }

            brain.setActiveActivityIfPossible(Activity.PANIC);
        }

    }

    protected void tick(ServerLevel level, LivingEntity owner, long gameTime) {



    }

    public static boolean hasHostile(LivingEntity entity) {
        return entity.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
    }

    public static boolean isHurt(LivingEntity entity) {
        return entity.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }
}
