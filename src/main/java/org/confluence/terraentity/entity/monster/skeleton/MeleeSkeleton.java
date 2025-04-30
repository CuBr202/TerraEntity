package org.confluence.terraentity.entity.monster.skeleton;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.animation.BoneStateMachine;
import org.confluence.terraentity.entity.animation.BoneStates;
import org.confluence.terraentity.entity.animation.IUseItemAnimatable;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MeleeSkeleton extends AbstractSkeleton implements GeoEntity, IUseItemAnimatable<BoneStates> {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    BoneStateMachine<BoneStates> leftArmBoneStateMachine;
    BoneStateMachine<BoneStates> rightArmBoneStateMachine;

    public MeleeSkeleton(EntityType<? extends AbstractSkeleton> entityType, Level level, AttributeBuilder builder) {
        super(entityType, level);
        if(level.isClientSide){
            leftArmBoneStateMachine = new BoneStateMachine<>(BoneStates.IDLE);
            rightArmBoneStateMachine = new BoneStateMachine<>(BoneStates.IDLE);
        }
        builder.modify(this);
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
//        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BONE));
    }

    @Override
    protected @NotNull SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    @Override
    public void tick() {
        super.tick();
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
    public boolean isChargingCrossbow() {
        return false;
    }

    @Override
    public int getChargingTicks() {
        return 0;
    }

    @Override
    public BoneStateMachine<BoneStates> getLeftArmBoneStateMachine() {
        return leftArmBoneStateMachine;
    }

    @Override
    public BoneStateMachine<BoneStates> getRightArmBoneStateMachine() {
        return rightArmBoneStateMachine;
    }

    @Override
    public boolean isLieDown() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Idle", 5, state ->
                state.setAndContinue(state.isMoving() ? DefaultAnimations.WALK : DefaultAnimations.IDLE)
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
