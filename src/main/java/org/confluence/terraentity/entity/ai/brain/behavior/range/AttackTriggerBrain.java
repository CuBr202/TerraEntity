package org.confluence.terraentity.entity.ai.brain.behavior.range;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.confluence.terraentity.init.TEAi;

import java.util.function.Function;

/**
 * 触发攻击行为
 */
public class AttackTriggerBrain<T extends LivingEntity> extends Behavior<T> {
    float detectDistance;

    /**
     * 用于其他的攻击目标触发条件，比如护士的攻击目标是朋友，而不是敌对生物
     * @param modifier 用于修改MemoryModuleType
     * @param detectDistance 检测距离
     */
    public AttackTriggerBrain(Function<ImmutableMap.Builder<MemoryModuleType<?>, MemoryStatus>, ImmutableMap.Builder<MemoryModuleType<?>, MemoryStatus>> modifier, float detectDistance) {
        super(modifier.apply(
                ImmutableMap.<MemoryModuleType<?>, MemoryStatus>builder()
                        .put(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT)
        ).build());

        this.detectDistance = detectDistance;
    }

    /**
     * 默认攻击敌对生物
     * @param detectDistance 检测距离
     */
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
        var memory = brain.getMemory(targetMemoryType());
        if(memory.isPresent()) {
            LivingEntity target = memory.get();
            boolean shouldAdd = target.distanceToSqr(living) < getDetectDistanceSqr(living) && target.isAlive();
            if (shouldAdd) {
                brain.setMemory(MemoryModuleType.ATTACK_TARGET, target);
                brain.setActiveActivityIfPossible(TEAi.Activities.RANGE_ATTACK.get());
            }
        }
    }

    protected MemoryModuleType<LivingEntity> targetMemoryType() {
        return MemoryModuleType.NEAREST_HOSTILE;
    }

    protected float getDetectDistanceSqr(T living) {
        return detectDistance * detectDistance;
    }
}
