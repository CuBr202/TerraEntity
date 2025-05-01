package org.confluence.terraentity.entity.monster.prefab;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AttributeBuilder {
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

    public BiConsumer<AnimatableManager.ControllerRegistrar, AbstractMonster> controller;
    public List<BiConsumer<GoalSelector, AbstractMonster>> goals = new ArrayList<>();
    public List<BiConsumer<GoalSelector, AbstractMonster>> targets = new ArrayList<>();
    public Function<AbstractMonster, PathNavigation> navigation;

    public static AttributeBuilder copyFrom(Supplier<AttributeBuilder> supplier) {
        return supplier.get();
    }


    public void modify(Mob mob) {
        mob.setDiscardFriction(noFriction);

        mob.getAttribute(Attributes.ARMOR).setBaseValue(ARMOR);
        mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(ATTACK_DAMAGE);
        mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(MOVEMENT_SPEED);
        mob.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(FOLLOW_RANGE);
        mob.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(SPAWN_REINFORCEMENTS_CHANCE);
        mob.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(KNOCKBACK_RESISTANCE);
        mob.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(ATTACK_KNOCKBACK);
        mob.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(ATTACK_SPEED);
        mob.getAttribute(Attributes.FLYING_SPEED).setBaseValue(FLYING_SPEED);
        mob.getAttribute(Attributes.SAFE_FALL_DISTANCE).setBaseValue(SAFE_FALL);
        mob.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(JUMP_STRENGTH);
        mob.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(STEP_HEIGHT);
    }

    public AttributeBuilder setXpReward(int xpReward) {
        this.xpReward = xpReward;
        return this;
    }

    public AttributeBuilder modify(Function<AttributeBuilder, AttributeBuilder> modifier) {
        return modifier.apply(this);
    }

    public AttributeBuilder setAttachIncrease(float attackIncrease) {
        this.attackIncrease = attackIncrease;
        return this;

    }

    public AttributeBuilder setAttackDamage(int attackDamage) {
        this.ATTACK_DAMAGE = attackDamage;
        return this;
    }

    public AttributeBuilder setHealth(int maxHealth) {
        this.MAX_HEALTH = maxHealth;
        return this;
    }

    public AttributeBuilder setArmor(int defense) {
        this.ARMOR = defense;
        return this;
    }

    public AttributeBuilder setMovementSpeed(float movementSpeed) {
        this.MOVEMENT_SPEED = movementSpeed;
        return this;
    }

    public AttributeBuilder setFollowRange(int followRange) {
        this.FOLLOW_RANGE = followRange;
        return this;
    }

    public AttributeBuilder setKnockbackResistance(float knockbackResistance) {
        this.KNOCKBACK_RESISTANCE = knockbackResistance;
        return this;
    }

    public AttributeBuilder setDeathSound(Supplier<SoundEvent> deathSound) {
        this.deathSound = deathSound;
        return this;
    }

    public AttributeBuilder setAmbientSound(Supplier<SoundEvent> ambientSound) {
        this.ambientSound = ambientSound;
        return this;
    }

    public AttributeBuilder setHurtSound(Supplier<SoundEvent> hurtSound) {
        this.hurtSound = hurtSound;
        return this;
    }

    public AttributeBuilder setController(BiConsumer<AnimatableManager.ControllerRegistrar, AbstractMonster> controller) {
        this.controller = controller;
        return this;
    }


    public AttributeBuilder addGoal(BiConsumer<GoalSelector, AbstractMonster> goal) {
        this.goals.add(goal);
        return this;
    }

    public AttributeBuilder addTarget(BiConsumer<GoalSelector, AbstractMonster> target) {
        this.targets.add(target);
        return this;
    }

    public AttributeBuilder setNavigation(Function<AbstractMonster, PathNavigation> navigation) {
        this.navigation = navigation;
        return this;
    }

    public AttributeBuilder setNoGravity() {
        this.noGravity = true;
        return this;
    }

    public AttributeBuilder setKnockBack(float knockBack) {
        this.ATTACK_KNOCKBACK = knockBack;
        return this;
    }

    public AttributeBuilder setSafeFall(float value) {
        this.SAFE_FALL = value;
        return this;
    }

    public AttributeBuilder setNoAttachAttack() {
        this.attachAttack = false;
        return this;
    }

    public AttributeBuilder setNoFriction() {
        this.noFriction = true;
        return this;
    }

    public AttributeBuilder setJumpStrength(float jumpStrength) {
        this.JUMP_STRENGTH = jumpStrength;
        return this;
    }

    public AttributeBuilder setStepHeight(float stepHeight) {
        this.STEP_HEIGHT = stepHeight;
        return this;
    }

    public AttributeBuilder setTicker(Consumer<AbstractMonster> ticker) {
        this.ticker = ticker;
        return this;
    }

    public AttributeBuilder setPushable(boolean pushable) {
        this.pushable = pushable;
        return this;
    }
}
