package org.confluence.terraentity.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.confluence.terraentity.mixinauxiliary.SelfGetter;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ICollisionAttackMob<T extends Mob> extends SelfGetter<T> {

    // 检测间隔
    int getDetectInternal();
    // 攻击间隔
    int getAttackInternal();

    int getActualAttackInterval();

    void setActualAttackInterval(int interval);

    float getAttackRangeExtent();

    // 开启碰撞伤害
    default boolean canCollisionHurt() {
        return true;
    }

    default void doCollisionAttack(Predicate<LivingEntity> filter, Consumer<Entity> attackCallback){
        if(te$getSelf().level().isClientSide) return;
        setActualAttackInterval(getActualAttackInterval() - 1);
        if (canCollisionHurt() && !te$getSelf().level().isClientSide && getActualAttackInterval() <= 0) {
            setActualAttackInterval(getDetectInternal());
            // 包围盒检测造成伤害
            var entities = te$getSelf().level().getEntities(te$getSelf(), te$getSelf().getBoundingBox().inflate(getAttackRangeExtent()), e-> e instanceof LivingEntity && e!= te$getSelf());
            if (!entities.isEmpty()) {
                for (var e : entities) {
                    if ( e instanceof LivingEntity living && te$getSelf().canAttack(living) && filter.test(living) ){
                        setActualAttackInterval(getAttackInternal());
                        attackCallback.accept(e);
                    }
                }
            }
        }
    }
}
