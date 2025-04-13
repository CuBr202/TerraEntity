package org.confluence.terraentity.entity.npc.brain;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.confluence.terraentity.init.TEAi;

/**
 * 恐慌行为清除
 */
public class PanicCalmDownBrain extends Behavior<Mob> {
    public PanicCalmDownBrain() {
        super(ImmutableMap.of(
                MemoryModuleType.NEAREST_HOSTILE, MemoryStatus.REGISTERED,
                MemoryModuleType.HURT_BY, MemoryStatus.REGISTERED,
                MemoryModuleType.HURT_BY_ENTITY,MemoryStatus.REGISTERED
        ));
    }

    public static boolean hasHostile(Mob living) {
        return living.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
    }

    public static boolean isHurt(Mob living) {
        return living.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }

    public static boolean isHurtByEntity(Mob living) {
        return living.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY_ENTITY);
    }

    @Override
    protected void start(ServerLevel level, Mob living, long gameTimeIn) {
        Brain<?> brain = living.getBrain();
        boolean hurtOrHostileOrAway = brain.hasMemoryValue(MemoryModuleType.HURT_BY)
//                || brain.hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE)
                || brain.getMemory(MemoryModuleType.HURT_BY_ENTITY).filter(entity -> entity.distanceTo(living) < 10 && entity!= living).isPresent();
        // 受到伤害后一定时间内不再恐慌
        if (!hurtOrHostileOrAway || living.tickCount - living.getLastHurtByMobTimestamp() < 100 ) {
            LivingEntity target = brain.getMemory(MemoryModuleType.HURT_BY_ENTITY).orElse(null);
            brain.eraseMemory(MemoryModuleType.PATH);
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
//            brain.updateActivityFromSchedule(level.getDayTime(), gameTimeIn);
            brain.setActiveActivityIfPossible(TEAi.Activities.RANGE_ATTACK);
            if(target!= null && target.isAlive() && target != living) {
                brain.setMemory(MemoryModuleType.ATTACK_TARGET, target);
                brain.eraseMemory(MemoryModuleType.HURT_BY_ENTITY);
            }
            brain.setMemory(MemoryModuleType.ATTACK_COOLING_DOWN, false);

        }
    }
}
