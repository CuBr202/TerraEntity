package org.confluence.terraentity.entity.npc.brain;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;

/**
 * 当远程攻击冷却时, 或者距离过远试图接近敌人的走a行为
 */
public class RangeAttackOnCooldownBrain extends Behavior<AbstractTerraNPC> {

    public RangeAttackOnCooldownBrain(int cooldownTime) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_PRESENT
                ),cooldownTime);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractTerraNPC owner) {
        if(owner.getBrain().getMemory(MemoryModuleType.ATTACK_COOLING_DOWN).get()){
            // 在冷却时执行
            return true;
        }
        LivingEntity attackTarget = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        // 距离过远时尝试接近
        return attackTarget.distanceTo(owner) > owner.getAttackRange() + 2;

    }

    @Override
    protected void tick(ServerLevel level, AbstractTerraNPC owner, long gameTime) {

        if(!owner.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)) { // 防止一直寻路导致鬼畜
            owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {

                float safeDistance = owner.getAttackRange();
                Vec3 targetPos = target.position();
                Vec3 ownerPos = owner.position();
                Vec3 toPos;
                if (ownerPos.distanceTo(targetPos) > safeDistance) {
//                owner.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.0);
                    toPos = LandRandomPos.getPosTowards(owner, (int) safeDistance, 5, targetPos);

                } else {
//                owner.getNavigation().stop();
                    toPos = LandRandomPos.getPosAway(owner, (int) safeDistance, 5, targetPos);
                }
                if (toPos != null) {
                    owner.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(toPos, 1.0f, (int) 1f));
                } else {
                    // debug
                    owner.setDeltaMovement(new Vec3(0, 0.02f, 0));
                }

            });
        }
    }

    @Override
    protected void start(ServerLevel level, AbstractTerraNPC entity, long gameTimeIn) {

    }

    @Override
    protected void stop(ServerLevel level, AbstractTerraNPC entity, long gameTimeIn) {
        entity.getBrain().setMemory(MemoryModuleType.ATTACK_COOLING_DOWN, false);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, AbstractTerraNPC entity, long gameTimeIn) {
        return this.checkExtraStartConditions(level, entity);
    }
}