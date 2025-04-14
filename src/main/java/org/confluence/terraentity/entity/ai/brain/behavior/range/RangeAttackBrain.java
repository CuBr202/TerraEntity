package org.confluence.terraentity.entity.ai.brain.behavior.range;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.confluence.terraentity.utils.TEUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 执行远程攻击的行为
 */
public class RangeAttackBrain<T extends Mob> extends Behavior<T> {

    int prepareTime; // 看向敌人后，瞄准需要时间
    int _prepareTime;
    boolean isPreparing; // 是否准备攻击
    float attackRange;

    public RangeAttackBrain(int prepareTime, float attackRange) {
        super(ImmutableMap.of(
                        MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                        MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                        MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_PRESENT
                ),
                200);
        this.prepareTime = prepareTime;
        this._prepareTime = prepareTime;
        this.attackRange = attackRange;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T owner) {
        LivingEntity target = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        if(target.distanceTo(owner) > attackRange){
            return false;
        }
        return !owner.getBrain().getMemory(MemoryModuleType.ATTACK_COOLING_DOWN).get();
    }

    @Override
    protected void tick(ServerLevel level, T owner, long gameTime) {
        owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {
            BehaviorUtils.lookAtEntity(owner, target);
            owner.lookAt(target, 10, owner.getMaxHeadYRot());
            double angle = TEUtils.angleBetween(owner.getLookAngle(), target.getEyePosition().subtract(owner.getEyePosition()).normalize());
            angle = Mth.wrapDegrees(angle);

            if(angle < 0.1f) {
                if(owner.getSensing().hasLineOfSight(target)){
                    isPreparing = true;
                }else{
                    this.doStop(level, owner, gameTime);
                    owner.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
                    owner.getBrain().updateActivityFromSchedule(level.getDayTime(), gameTime);
                }
            }

            if(isPreparing){
                onPrepare(level, owner, target, prepareTime);
                if(--prepareTime <= 0) {
                    doAttack(level, owner, target);
                }
            }

        });

    }

    protected void onPrepare(ServerLevel level, T owner, LivingEntity target, int prepareTime){
        // 可能要停下来瞄准
//        if(owner.getRandom().nextFloat() < 0.1f){
//            owner.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
//        }
    }

    @Override
    protected void start(ServerLevel level, Mob entity, long gameTimeIn) {
        entity.startUsingItem(InteractionHand.MAIN_HAND);
    }

    @Override
    protected void stop(ServerLevel level, Mob entity, long gameTimeIn) {
        entity.getBrain().setMemory(MemoryModuleType.ATTACK_COOLING_DOWN, true);
        isPreparing = false;
        prepareTime = _prepareTime;
        entity.stopUsingItem();
//        entity.swing(InteractionHand.MAIN_HAND, true);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Mob entity, long gameTimeIn) {
        return prepareTime > 0;
    }

    protected void doAttack(ServerLevel level, Mob owner, LivingEntity target){
        if(customDoAttack(level, owner, target)){
            return;
        }
        defaultDoAttack(level, owner, target);
    }

    /**
     * 自定义远程攻击逻辑, 返回true表示已自定义攻击逻辑，否则使用默认逻辑
     */
    protected boolean customDoAttack(ServerLevel level, Mob owner, LivingEntity target){
        ItemStack stack = owner.getMainHandItem();
        if(stack.getItem() instanceof ProjectileWeaponItem weaponItem){
            stack.hurtAndBreak(1,level,owner,(entity) -> {});
            weaponItem.shoot( level, owner, InteractionHand.MAIN_HAND, stack, List.of(Items.ARROW.getDefaultInstance()), 1.5f, 1f, owner.getRandom().nextFloat() < 0.3f, target);
            return true;
        }
        return false;
    }

    /**
     * 默认远程攻击逻辑
     */
    protected void defaultDoAttack(ServerLevel level, Mob owner, LivingEntity target){
        // 调用默认的方法
        Arrow arrow = new Arrow(owner.level(), owner, Items.ARROW.getDefaultInstance(), Items.ARROW.getDefaultInstance());
        arrow.setPos(owner.getX(), owner.getY() + owner.getEyeHeight(), owner.getZ());
        arrow.shootFromRotation(owner, owner.getXRot(), owner.getYRot(), 0.0F, 1.5F, 1.0F);
        owner.level().addFreshEntity(arrow);
    }
}