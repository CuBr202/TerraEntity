package org.confluence.terraentity.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.common.ForgeMod;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.entity.ai.ICollisionAttackEntity;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import org.confluence.terraentity.entity.monster.prefab.IAttributeHolder;
import org.confluence.terraentity.init.TESounds;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import static org.confluence.terraentity.utils.TEUtils.getMultiple;

public class AbstractMonster extends Monster implements GeoEntity , ICollisionAttackEntity<AbstractMonster>, IAttributeHolder {

    protected CollisionProperties collisionProperties = new CollisionProperties(10, 20, 0);
    public AttributeBuilder builder;
    protected boolean dirty = true;

    public AbstractMonster(EntityType<? extends Monster> type, Level level, AttributeBuilder builder) {
        super(type, level);
        this.builder = builder.setSpawnWithoutLight();
        if (!level.isClientSide) {
            // 防止重复注册ai
            this.goalSelector.removeAllGoals(g->true);
            this.targetSelector.removeAllGoals(t->true);
            this.registerGoals();
        }
        this.navigation = createNavigation(level);
        this.builder.modify(this);

        this.xpReward = builder.xpReward;
    }

//    @Override
//    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnReason) {
//        return spawnReason == MobSpawnType.NATURAL; // 无视光照
//    }

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
        registerTargetGoal(targetSelector);
    }

    protected void registerTargetGoal(GoalSelector targetSelector){

    }

    public boolean ignoreAttributeModify(){
        return false;
    }

    public void firstSpawn(){};

    @Override
    public void onAddedToWorld(){
        super.onAddedToWorld();
        if(!level().isClientSide && !ignoreAttributeModify()){
            if(dirty && !ServerConfig.DISABLE_BUILTIN_MODIFIER.get()){
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getMaxHealth());
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
                .add(Attributes.MOVEMENT_SPEED, 0.25f)
                .add(Attributes.FOLLOW_RANGE)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
                .add(Attributes.KNOCKBACK_RESISTANCE)
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(Attributes.ATTACK_SPEED)
                .add(Attributes.FLYING_SPEED)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get())
                .add(ForgeMod.ENTITY_GRAVITY.get(),0.08f)
                .add(Attributes.JUMP_STRENGTH, 0)
                ;
    }


    public boolean isPushable() {
        return super.isPushable() && builder.pushable;
    }


    @Override
    protected SoundEvent getDeathSound() {
        if(builder.deathSound == null) return TESounds.ROUTINE_DEATH.get();
        return builder.deathSound.get();
    }
    @Override
    protected SoundEvent getAmbientSound() {
        if(builder.ambientSound == null) return super.getAmbientSound();
        return builder.ambientSound.get();
    }
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        if(builder.hurtSound == null) return TESounds.ROUTINE_HURT.get();
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

    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        if(builder.spawnWithoutLight){
            return 0;
        }
        return super.getWalkTargetValue(pos, level);
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
            doCollisionAttack(e->e instanceof LivingEntity living && canAttack(living) && e.getType() != this.getType(),
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


    @Override
    public AttributeBuilder getAttributeBuilder() {
        return builder;
    }
}
