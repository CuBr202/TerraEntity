package org.confluence.terraentity.entity.monster;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AbstractMonster extends Monster implements GeoEntity {

    public Builder builder;
    public AbstractMonster(EntityType<? extends Monster> type, Level level,Builder builder) {
        super(type, level);
        this.builder = builder;
        this.registerGoals();
        this.navigation = createNavigation(level);

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
        this.getAttribute(Attributes.SAFE_FALL_DISTANCE).setBaseValue(builder.SAFE_FALL);

    }

    protected void registerGoals() {
        if(builder!= null && builder.goal!= null)
            builder.goal.accept(goalSelector,this);
        if(builder!= null && builder.target!= null)
            builder.target.accept(targetSelector,this);
//        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class,false));
//        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class,false));

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
/*
    public static boolean checkBloodCrawlerSpawn(EntityType<? extends CrimsonKemera> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!(pLevel instanceof Level level)) {
            return false; // 如果 pLevel 不是 Level 的实例，返回 false
        }

        if (!checkMobSpawnRules(type, pLevel, pSpawnType, pPos, pRandom)) {
            return false; // 如果不满足基本生成规则，返回 false
        }

        int y = pPos.getY();
        if (y >= 260) {
            return false; // 不能生成在 y = 260 或更高的位置
        }

        return true;
    }*/
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


    public static class Builder {
        private int ATTACK_DAMAGE = 15;
        private int MAX_HEALTH = 31;
        private int ARMOR = 2;
        private float MOVEMENT_SPEED = 0.38f;
        private int FOLLOW_RANGE = 32;
        private float SPAWN_REINFORCEMENTS_CHANCE = 0.01f;
        private float KNOCKBACK_RESISTANCE = 0.8f;
        private float ATTACK_KNOCKBACK = 0.5f;
        private float ATTACK_SPEED = 0.6f;
        private float FLYING_SPEED = 0.4f;
        private float SAFE_FALL = 5f;
        boolean noGravity = false;



        private Supplier<SoundEvent> deathSound;
        private Supplier<SoundEvent> ambientSound;
        private Supplier<SoundEvent> hurtSound;

        private BiConsumer<AnimatableManager.ControllerRegistrar,AbstractMonster> controller;
        private BiConsumer<GoalSelector,AbstractMonster> goal;
        private BiConsumer<GoalSelector,AbstractMonster> target;
        private Function<AbstractMonster,PathNavigation> navigation;
/*
        public AttributeSupplier.Builder createAttributes() {
            return LivingEntity.createLivingAttributes()
                    .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)  // 攻击力
                    .add(Attributes.MAX_HEALTH, MAX_HEALTH)        // 生命值
                    .add(Attributes.ARMOR, ARMOR)                 // 防御值
                    .add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED)          // 移动速度
                    .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)             // 跟随距离
                    .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, SPAWN_REINFORCEMENTS_CHANCE)  // 召唤物品的几率
                    .add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE);     // 击退抗性
        }*/

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

        public Builder setSpawnReinforcementsChance(float spawnReinforcementsChance) {
            this.SPAWN_REINFORCEMENTS_CHANCE = spawnReinforcementsChance;
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

        public Builder setGoal(BiConsumer<GoalSelector,AbstractMonster> goal) {
            this.goal = goal;
            return this;
        }

        public Builder setTarget(BiConsumer<GoalSelector,AbstractMonster> target) {
            this.target = target;
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
        public Builder setAttackSpeed(float attackSpeed) {
            this.ATTACK_SPEED = attackSpeed;
            return this;
        }
        public Builder setFlyingSpeed(float flyingSpeed) {
            this.FLYING_SPEED = flyingSpeed;
            return this;
        }
        public Builder setSafeFall(float value) {
            this.SAFE_FALL = value;
            return this;
        }

    }
    public static AbstractMonster.Builder copyFrom(Supplier<AbstractMonster.Builder> supplier) {
        return supplier.get();
    }
}
