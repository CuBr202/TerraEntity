package org.confluence.terraentity.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terraentity.mixinauxiliary.SelfGetter;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <h1>包围盒碰撞接口</h1>
 * @param <T> 实体类型
 */
public interface ICollisionAttackEntity<T extends Entity> extends SelfGetter<T> {

    CollisionProperties getCollisionProperties();

    // 开启碰撞伤害
    default boolean canCollisionHurt() {
        return true;
    }

    default void doCollisionAttack(Predicate<LivingEntity> filter, Consumer<Entity> attackCallback){
        if(te$getSelf().level().isClientSide) return;
        getCollisionProperties().reduceAttackInterval();
        if (canCollisionHurt() && !te$getSelf().level().isClientSide && getCollisionProperties().canAttack()) {
            getCollisionProperties().reDetect();
            // 包围盒检测造成伤害
            var entities = te$getSelf().level().getEntities(te$getSelf(), te$getSelf().getBoundingBox().inflate(getCollisionProperties().attackRangeExtent), e-> e instanceof LivingEntity && e!= te$getSelf());
            if (!entities.isEmpty()) {
                for (var e : entities) {
                    if ( e instanceof LivingEntity living && filter.test(living) ){
                        getCollisionProperties().rewind();
                        attackCallback.accept(e);
                    }
                }
            }
        }
    }

    /**
     * AABB攻击检测属性
     */
    class CollisionProperties{
        public int detectInternal;
        public int attackInternal;
        public float attackRangeExtent;
        public int actualAttackInterval;

        /**
         * AABB攻击检测属性
         * @param detectInternal 检测的间隔
         * @param attackInternal 攻击的间隔
         * @param attackRangeExtent 攻击范围
         */
        public CollisionProperties(int detectInternal, int attackInternal, float attackRangeExtent) {
            this.detectInternal = detectInternal;
            this.attackInternal = attackInternal;
            this.attackRangeExtent = attackRangeExtent;
            this.actualAttackInterval = attackInternal;
        }

        /**
         * 重置攻击间隔
         */
        public void rewind() {
            actualAttackInterval = attackInternal;
        }

        /**
         * 重新检测间隔
         */
        public void reDetect() {
            actualAttackInterval = detectInternal;
        }

        /**
         * 减少攻击间隔
         */
        public void reduceAttackInterval() {
            actualAttackInterval --;
        }

        /**
         * 是否可以攻击
         */
        public boolean canAttack() {
            return actualAttackInterval <= 0;
        }
    }
}
