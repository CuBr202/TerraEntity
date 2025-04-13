package org.confluence.terraentity.entity.npc.brain;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Items;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.utils.TEUtils;

/**
 * 执行远程攻击的行为
 */
public class RangeAttackBrain extends Behavior<AbstractTerraNPC> {

    int prepareTime; // 看向敌人后，瞄准需要时间
    int _prepareTime;
    boolean isPreparing; // 是否准备攻击

    public RangeAttackBrain(int prepareTime) {
        super(ImmutableMap.of(
                        MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                        MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                        MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_PRESENT
                ),
                200);
        this.prepareTime = prepareTime;
        this._prepareTime = prepareTime;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractTerraNPC owner) {
        if(owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get().distanceTo(owner) > owner.getAttackRange() + 1){
            return false;
        }
        return !owner.getBrain().getMemory(MemoryModuleType.ATTACK_COOLING_DOWN).get();
    }

    @Override
    protected void tick(ServerLevel level, AbstractTerraNPC owner, long gameTime) {
        owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {
            BehaviorUtils.lookAtEntity(owner, target);
            owner.lookAt(target, 10, owner.getMaxHeadYRot());
            double angle = TEUtils.angleBetween(owner.getLookAngle(), target.getEyePosition().subtract(owner.getEyePosition()).normalize());
            angle = Mth.wrapDegrees(angle);

            if(angle < 0.1f) {
                isPreparing = true;
            }

            if(isPreparing){
                if(--prepareTime <= 0) {
                    Arrow arrow = new Arrow(owner.level(), owner, Items.ARROW.getDefaultInstance(), Items.ARROW.getDefaultInstance());
                    arrow.shootFromRotation(owner, owner.getXRot(), owner.getYRot(), 0.0F, 1.5F, 1.0F);
                    owner.level().addFreshEntity(arrow);
                }
            }

        });


    }

    @Override
    protected void start(ServerLevel level, AbstractTerraNPC entity, long gameTimeIn) {
    }

    @Override
    protected void stop(ServerLevel level, AbstractTerraNPC entity, long gameTimeIn) {
        entity.getBrain().setMemory(MemoryModuleType.ATTACK_COOLING_DOWN, true);
        isPreparing = false;
        prepareTime = _prepareTime;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, AbstractTerraNPC entity, long gameTimeIn) {
        return prepareTime > 0;
    }
}