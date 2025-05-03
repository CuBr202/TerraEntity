package org.confluence.terraentity.entity.monster.skeleton;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;

public class MeleeSkeleton extends RangeSkeleton {


    public MeleeSkeleton(EntityType<? extends AbstractSkeleton> entityType, Level level, AttributeBuilder builder) {
        super(entityType, level, builder);


    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {

    }


    @Override
    public int getCurrentSwingDuration() {
        return 10;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        this.swing(InteractionHand.MAIN_HAND, true);
        return super.doHurtTarget(entity);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Idle", 5, state ->{
            state.setControllerSpeed(2f);
            return state.setAndContinue(state.isMoving() ? DefaultAnimations.WALK : DefaultAnimations.IDLE);
        }
        ));
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        if (effectInstance.is(MobEffects.POISON)) return false;
        return super.addEffect(effectInstance, entity);
    }
}
