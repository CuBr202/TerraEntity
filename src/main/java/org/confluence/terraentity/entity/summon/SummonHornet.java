package org.confluence.terraentity.entity.summon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.ai.goal.summon.SummonFlyFlowOwnerGoal;
import org.confluence.terraentity.entity.monster.Hornet;
import org.confluence.terraentity.entity.monster.prefab.FlyMonsterPrefab;
import org.confluence.terraentity.entity.proj.LineProj;
import org.confluence.terraentity.init.TEEntities;

import java.util.Optional;
import java.util.UUID;

public class SummonHornet extends Hornet implements ISummonMob<SummonHornet>{

    public SummonHornet(EntityType<? extends Monster> type, Level level) {
        super(type, level, FlyMonsterPrefab.BEE_BUILDER.get().setMovementSpeed(1));
        this.collisionProperties.detectInternal = 999999999;
        this.attackInternal = 5;
    }

    @Override
    protected void registerGoals() {
//        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2, true));
        this.goalSelector.addGoal(1, new BeeShootGoal(this, 0, 7){
            @Override
            protected boolean canShoot(Entity target) {
                return true;
            }
        });
        this.goalSelector.addGoal(2, new BeeKeepOnTargetGoal(this));
        this.goalSelector.addGoal(9, new FloatGoal(this));

        registerTargetGoal(this.targetSelector);
    }

    protected void registerTargetGoal(GoalSelector targetSelector){
        summon_registerCommonGoals();
    }

    protected LineProj createProj(){
        return TEEntities.SUMMON_BEE_STICK_PROJ.get().create(level());
    }

    public void summon_registerMoveGoal(){
        goalSelector.addGoal(6, new SummonFlyFlowOwnerGoal<>(this, 1.0, 10.0F, 2.0F));
    }


    public boolean ignoreAttributeModify(){
        return true;
    }


    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    /* Summon API */
    int cost;

    @Override
    public EntityDataAccessor<Optional<UUID>> get_DATA_OWNERUUID_ID() {
        return DATA_OWNERUUID_ID;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void setCost(int cost) {
        this.cost = cost;
    }

    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(SummonHornet.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);this.registerGoals();
        this.summon_addData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.summon_readData(compound);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        summon_onAddedToLevel();
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        summon_onRemovedFromLevel();
    }


    @Override
    public boolean canAttack(LivingEntity target) {
        if(target == summon_getOwner()) return false;
        return target.canBeSeenAsEnemy();
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return source.is(DamageTypes.GENERIC_KILL) && super.hurt(source, amount);
    }

    @Override
    public boolean summon_canFlyToOwner(){
        return true;
    }
}


