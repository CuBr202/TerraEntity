package org.confluence.terraentity.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.confluence.terraentity.entity.ai.ICollisionAttackEntity;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.confluence.terraentity.utils.TEUtils.getMultiple;

public class AbstractMonster extends Monster implements GeoEntity , ICollisionAttackEntity<AbstractMonster> {

    protected CollisionProperties collisionProperties = new CollisionProperties(10, 20, 0);
    public Builder builder;
    protected boolean dirty = true;

    public AbstractMonster(EntityType<? extends Monster> type, Level level,Builder builder) {
        super(type, level);
        this.builder = builder;
        if (!level.isClientSide) {
            // 防止重复注册ai
            this.goalSelector.removeAllGoals(g->true);
            this.targetSelector.removeAllGoals(t->true);
            this.registerGoals();
        }
        this.navigation = createNavigation(level);
        this.setDiscardFriction(builder.noFriction);

        this.getAttribute(Attributes.ARMOR).setBaseValue(builder.ARMOR);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(builder.ATTACK_DAMAGE);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(builder.MOVEMENT_SPEED);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(builder.FOLLOW_RANGE);
        this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(builder.SPAWN_REINFORCEMENTS_CHANCE);
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(builder.KNOCKBACK_RESISTANCE);
        this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(builder.ATTACK_KNOCKBACK);
        this.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(builder.ATTACK_SPEED);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(builder.FLYING_SPEED);
        this.getAttribute(Attributes.SAFE_FALL_DISTANCE).setBaseValue(builder.SAFE_FALL);
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(builder.JUMP_STRENGTH);
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(builder.STEP_HEIGHT);

        this.xpReward = builder.xpReward;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("dirty", false);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("dirty")) {
            dirty = false;
        }
    }


    @Override
    protected void registerGoals() {
        if(builder!= null) builder.goals.forEach(g->g.accept(goalSelector,this));
        if(builder!= null) builder.targets.forEach(t->t.accept(targetSelector,this));
        registerTargetGoal(targetSelector);
    }

    protected void registerTargetGoal(GoalSelector targetSelector){

    }

    public boolean ignoreAttributeModify(){
        return false;
    }

    public void firstSpawn(){};

    @Override
    public void onAddedToLevel(){
        super.onAddedToLevel();
        if(!level().isClientSide && !ignoreAttributeModify()){
            if(dirty){
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(builder.MAX_HEALTH);
                this.setHealth(getMaxHealth());
                firstSpawn();
            }
        }
    }

    public float getAttributeMultiplier(Holder<Attribute> attribute){
        return getMultiple(level(), attribute);
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE)
                .add(Attributes.MAX_HEALTH)
                .add(Attributes.ARMOR)
                .add(Attributes.MOVEMENT_SPEED)
                .add(Attributes.FOLLOW_RANGE)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
                .add(Attributes.KNOCKBACK_RESISTANCE)
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(Attributes.ATTACK_SPEED)
                .add(Attributes.FLYING_SPEED)

                ;
    }


    public boolean isPushable() {
        return super.isPushable() && builder.pushable;
    }


    public static boolean checkFlyingFishSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        // 判断是否下雨
        if (!level.isRaining()) {
            return false;
        }

        int y = pPos.getY();
        if (y < 60 || y >= 260) {
            return false; // 只能生成在 y = 60 到 y = 260 之间
        }

        return true;
    }
    public static boolean checkRoutineMonsterSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y >= 260) {
            return false; // 不能生成在 y = 260 或更高的位置
        }

        return true;
    }
    public static boolean checkUndergroundMonsterSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y < -55 || y > 30) {
            return false; // 只能生成在 y = -55 到 y = 30 之间
        }

        return true;
    }
    public static boolean checkNetherMonsterSpawn(EntityType<? extends Mob> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false;
        }

        int y = pPos.getY();
        if (y < 30 || y > 100) {
            return false; // 只能生成在 y = 30 到 y = 100 之间
        }

        return true;
    }

    @Override
    protected SoundEvent getDeathSound() {
        if(builder.deathSound == null) return super.getDeathSound();
        return builder.deathSound.get();
    }
    @Override
    protected SoundEvent getAmbientSound() {
        if(builder.ambientSound == null) return super.getAmbientSound();
        return builder.ambientSound.get();
    }
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        if(builder.hurtSound == null) return super.getHurtSound(pDamageSource);
        return builder.hurtSound.get();
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        if(builder != null && builder.controller != null)
            builder.controller.accept(controllers,this);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        if(builder != null && builder.navigation != null)
            return builder.navigation.apply(this);
        return super.createNavigation(level);
        /*
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
        */
    }

    public boolean isNoGravity() {
        if(builder == null)return true;
        return builder.noGravity;
    }

    public void tick(){
        super.tick();
        if(builder!=null && builder.ticker!=null) builder.ticker.accept(this);
        if(!level().isClientSide && builder.attachAttack && isAlive()){
            doCollisionAttack(e->canAttack(e) && e.getType() != this.getType(),
                    this::doHurtTarget
            );
        }
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        return entity.canBeSeenAsEnemy() &&
                        entity != this &&!(entity instanceof AbstractTerraBossBase);
    }

    @Override
    public CollisionProperties getCollisionProperties() {
        return collisionProperties;
    }

    @Override
    public boolean shouldDoCollision() {
        return getTarget() != null;
    }

    public static class Builder {
        public int ATTACK_DAMAGE = 15;
        public int MAX_HEALTH = 31;
        public int ARMOR = 2;
        public int xpReward = 5;
        public int FOLLOW_RANGE = 32;
        public float MOVEMENT_SPEED = 0.38f;
        public float SPAWN_REINFORCEMENTS_CHANCE = 0.01f;
        public float KNOCKBACK_RESISTANCE = 0.8f;
        public float ATTACK_KNOCKBACK = 0.5f;
        public float ATTACK_SPEED = 0.6f;
        public float FLYING_SPEED = 0.4f;
        public float SAFE_FALL = 5f;
        public float JUMP_STRENGTH = 0.41999998688697815f;
        public float STEP_HEIGHT = 0.6f;
        public float attackIncrease = 0;


        public boolean attachAttack = true;
        public boolean noGravity = false;
        public boolean noFriction = false;
        public boolean pushable = true;


        public Supplier<SoundEvent> deathSound;
        public Supplier<SoundEvent> ambientSound;
        public Supplier<SoundEvent> hurtSound;
        public Consumer<AbstractMonster> ticker;

        public BiConsumer<AnimatableManager.ControllerRegistrar,AbstractMonster> controller;
        public List<BiConsumer<GoalSelector,AbstractMonster>> goals = new ArrayList<>();
        public List<BiConsumer<GoalSelector,AbstractMonster>> targets = new ArrayList<>();
        public Function<AbstractMonster,PathNavigation> navigation;


        public Builder setXpReward(int xpReward) {
            this.xpReward = xpReward;
            return this;
        }

        public Builder modify(Function<Builder, Builder> modifier){
            return modifier.apply(this);
        }

        public Builder setAttachIncrease(float attackIncrease) {
            this.attackIncrease = attackIncrease;
            return this;

        }
        public Builder setAttackDamage(int attackDamage) {
            this.ATTACK_DAMAGE = attackDamage;
            return this;
        }

        public Builder setHealth(int maxHealth) {
            this.MAX_HEALTH = maxHealth;
            return this;
        }

        public Builder setArmor(int defense) {
            this.ARMOR = defense;
            return this;
        }
        public Builder setMovementSpeed(float movementSpeed) {
            this.MOVEMENT_SPEED = movementSpeed;
            return this;
        }

        public Builder setFollowRange(int followRange) {
            this.FOLLOW_RANGE = followRange;
            return this;
        }

        public Builder setKnockbackResistance(float knockbackResistance) {
            this.KNOCKBACK_RESISTANCE = knockbackResistance;
            return this;
        }

        public Builder setDeathSound(Supplier<SoundEvent> deathSound) {
            this.deathSound = deathSound;
            return this;
        }

        public Builder setAmbientSound(Supplier<SoundEvent> ambientSound) {
            this.ambientSound = ambientSound;
            return this;
        }

        public Builder setHurtSound(Supplier<SoundEvent> hurtSound) {
            this.hurtSound = hurtSound;
            return this;
        }

        public Builder setController(BiConsumer<AnimatableManager.ControllerRegistrar,AbstractMonster> controller) {
            this.controller = controller;
            return this;
        }


        public Builder addGoal(BiConsumer<GoalSelector,AbstractMonster> goal) {
            this.goals.add(goal) ;
            return this;
        }

        public Builder addTarget(BiConsumer<GoalSelector,AbstractMonster> target) {
            this.targets.add(target);
            return this;
        }

        public Builder setNavigation(Function<AbstractMonster,PathNavigation> navigation) {
            this.navigation = navigation;
            return this;
        }

        public Builder setNoGravity() {
            this.noGravity = true;
            return this;
        }
        public Builder setKnockBack(float knockBack) {
            this.ATTACK_KNOCKBACK = knockBack;
            return this;
        }

        public Builder setSafeFall(float value) {
            this.SAFE_FALL = value;
            return this;
        }
        public Builder setNoAttachAttack() {
            this.attachAttack = false;
            return this;
        }
        public Builder setNoFriction() {
            this.noFriction = true;
            return this;
        }
        public Builder setJumpStrength(float jumpStrength) {
            this.JUMP_STRENGTH = jumpStrength;
            return this;
        }
        public Builder setStepHeight(float stepHeight) {
            this.STEP_HEIGHT = stepHeight;
            return this;
        }
        public Builder setTicker(Consumer<AbstractMonster> ticker) {
            this.ticker = ticker;
            return this;
        }

        public Builder setPushable(boolean pushable) {
            this.pushable = pushable;
            return this;
        }
    }



    public static AbstractMonster.Builder copyFrom(Supplier<AbstractMonster.Builder> supplier) {
        return supplier.get();
    }


}
