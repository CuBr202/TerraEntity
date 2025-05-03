package org.confluence.terraentity.entity.monster.skeleton;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.confluence.terraentity.entity.animation.BoneStateMachine;
import org.confluence.terraentity.entity.animation.BoneStates;
import org.confluence.terraentity.entity.animation.IUseItemAnimatable;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MeleeSkeleton extends AbstractSkeleton implements GeoEntity, IUseItemAnimatable<BoneStates> {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    boolean dirty = false;

    BoneStateMachine<BoneStates> leftArmBoneStateMachine;
    BoneStateMachine<BoneStates> rightArmBoneStateMachine;

    AttributeBuilder builder;

    public MeleeSkeleton(EntityType<? extends AbstractSkeleton> entityType, Level level, AttributeBuilder builder) {
        super(entityType, level);
        if(level.isClientSide){
            leftArmBoneStateMachine = new BoneStateMachine<>(BoneStates.IDLE);
            rightArmBoneStateMachine = new BoneStateMachine<>(BoneStates.IDLE);
        }
        this.builder = builder;
        builder.modify(this);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnReason) {
        return spawnReason == MobSpawnType.NATURAL; // 无视光照
    }

    public void onAddedToLevel() {
        super.onAddedToLevel();
        if(!dirty && !level().isClientSide) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(builder.MAX_HEALTH);
            this.setHealth(getMaxHealth());
        }
    }
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if(compound.contains("Health")){
            dirty = true;
        }

    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {

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
        controllers.add(new AnimationController<>(this, "Walk/Idle", 5, state ->{
            state.setControllerSpeed(2f);
            return state.setAndContinue(state.isMoving() ? DefaultAnimations.WALK : DefaultAnimations.IDLE);
        }
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        if (effectInstance.is(MobEffects.POISON)) return false;
        return super.addEffect(effectInstance, entity);
    }
}
