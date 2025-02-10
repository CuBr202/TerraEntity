package org.confluence.terraentity.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraftforge.common.ForgeMod;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;

import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.confluence.terraentity.utils.TEUtils.getMultiple;

public class AbstractMonster extends Monster implements GeoEntity {
    protected int attackInternal = 0;
    protected int _attackInternal = 20;
    protected int _detectInternal = 10;
    public Builder builder;
    protected boolean dirty = true;

    public AbstractMonster(EntityType<? extends Monster> type, Level level,Builder builder) {
        super(type, level);
        this.builder = builder;
        if (!level.isClientSide)
            this.registerGoals();
        this.navigation = createNavigation(level);
        this.setDiscardFriction(builder.noFriction);

        this.setHealth(builder.MAX_HEALTH);
        this.getAttribute(Attributes.ARMOR).setBaseValue(builder.ARMOR);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(builder.ATTACK_DAMAGE);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(builder.MOVEMENT_SPEED);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(builder.FOLLOW_RANGE);
        this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(builder.SPAWN_REINFORCEMENTS_CHANCE);
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(builder.KNOCKBACK_RESISTANCE);
        this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(builder.ATTACK_KNOCKBACK);
        this.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(builder.ATTACK_SPEED);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(builder.FLYING_SPEED);
        this.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(builder.SAFE_FALL);
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(builder.JUMP_STRENGTH);
        this.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08f);

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
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
    }

    public void firstSpawn(){};

    @Override
    public void onAddedToWorld(){
        super.onAddedToWorld();
        if(!level().isClientSide){
            if(dirty){
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(builder.MAX_HEALTH);
                this.setHealth(getMaxHealth());
                firstSpawn();
            }
        }
    }

    public float getAttributeMultiplier(Attribute attribute){
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
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get())
                .add(ForgeMod.ENTITY_GRAVITY.get(),0.01f)
                .add(Attributes.JUMP_STRENGTH)
                ;
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
        if (y >= 260) {
            return false; // 不能生成在 y = 260 或更高的位置
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

    public float getJumpBoostPower() {
        return (float) (super.getJumpBoostPower() + getAttributeValue(Attributes.JUMP_STRENGTH));
    }
    @Override
    public boolean isNoGravity() {
        if(builder == null)return true;
        return builder.noGravity;
    }

    @Override
    protected int calculateFallDamage(float p_21237_, float p_21238_) {
        int damage = super.calculateFallDamage(p_21237_, p_21238_);
        if(builder != null && builder.SAFE_FALL > 0)
            damage -= (int) (builder.SAFE_FALL);
        return damage;
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        return super.causeFallDamage(p_147187_, p_147188_, p_147189_) && builder.SAFE_FALL < p_147187_;
    }

    public void tick(){
        super.tick();
        if(builder!=null && builder.ticker!=null) builder.ticker.accept(this);
        if(!level().isClientSide && builder.attachAttack && isAlive()){
            if(--attackInternal < 0){
                attackInternal = _detectInternal;
                doCollisionAttack(this, builder.attackIncrease, 1);
            }
        }
    }

    public void doCollisionAttack(Entity entity, float expand, float modify){
        var entities = level().getEntities(entity, entity.getBoundingBox().inflate(expand));
        if (!entities.isEmpty()) {
            for (var e : entities) {
                if (e instanceof LivingEntity living && canAttack(living) && !(e instanceof Monster)){
                    doAttack(living, modify);
                }
            }
        }
    }

    public void doAttack(LivingEntity entity, float modify) {
        attackInternal = _attackInternal;
        entity.hurt(this.damageSources().generic(), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * modify);
    }

    public void doAttack(LivingEntity entity) {
        doAttack(entity, 1);
    }


    public boolean canAttack(LivingEntity entity) {
        return attackInternal < 0 && entity.canBeSeenAsEnemy() &&
                        entity != this &&!(entity instanceof AbstractTerraBossBase);
    }



    public static class Builder {
        public int ATTACK_DAMAGE = 15;
        public int MAX_HEALTH = 31;
        public int ARMOR = 2;
        public float MOVEMENT_SPEED = 0.38f;
        public int FOLLOW_RANGE = 32;
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


        public Supplier<SoundEvent> deathSound;
        public Supplier<SoundEvent> ambientSound;
        public Supplier<SoundEvent> hurtSound;
        public Consumer<AbstractMonster> ticker;

        public BiConsumer<AnimatableManager.ControllerRegistrar,AbstractMonster> controller;
        public List<BiConsumer<GoalSelector,AbstractMonster>> goals = new ArrayList<>();
        public List<BiConsumer<GoalSelector,AbstractMonster>> targets = new ArrayList<>();
        public Function<AbstractMonster,PathNavigation> navigation;


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
        public Builder setNoAttackAttack() {
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
    }



    public static AbstractMonster.Builder copyFrom(Supplier<AbstractMonster.Builder> supplier) {
        return supplier.get();
    }


}
