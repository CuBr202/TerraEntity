package org.confluence.terraentity.entity.monster.skeleton;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.confluence.terraentity.entity.animation.BoneStateMachine;
import org.confluence.terraentity.entity.animation.BoneStates;
import org.confluence.terraentity.entity.animation.IUseItemAnimatable;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import org.confluence.terraentity.entity.monster.prefab.IAttributeHolder;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * 腐瘸
 */
public class RangeSkeleton extends AbstractSkeleton implements GeoEntity, IUseItemAnimatable<BoneStates>, IAttributeHolder {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    BoneStateMachine<BoneStates> leftArmBoneStateMachine;
    BoneStateMachine<BoneStates> rightArmBoneStateMachine;
    boolean dirty = false;

    AttributeBuilder builder;
    public RangeSkeleton(EntityType<? extends AbstractSkeleton> entityType, Level level, AttributeBuilder builder) {
        super(entityType, level);
        if(level.isClientSide){
            leftArmBoneStateMachine = new BoneStateMachine<>(BoneStates.IDLE);
            rightArmBoneStateMachine = new BoneStateMachine<>(BoneStates.IDLE);
        }
        this.builder = builder;
        builder.modify(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.ATTACK_DAMAGE, 6);
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if(!dirty && !level().isClientSide) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(builder.MAX_HEALTH);
            this.setHealth(getMaxHealth());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if(compound.contains("Health")){
            dirty = true;
        }

    }

    public float getWalkTargetValue(BlockPos pos) {
        if(this.builder.spawnWithoutLight){
            return 0;
        }
        return super.getWalkTargetValue(pos);
    }


    @Override
    protected boolean isSunBurnTick() {
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
    public AttributeBuilder getAttributeBuilder() {
        return builder;
    }
}
