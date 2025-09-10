package org.confluence.terraentity.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.api.entity.Boss;
import org.confluence.terraentity.api.entity.IMinion;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFlesh;
import org.confluence.terraentity.entity.monster.demoneye.DemonEyeSurroundTargetGoal;
import org.confluence.terraentity.entity.monster.demoneye.DemonEyeWanderGoal;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.mixin.accessor.EntityAccessor;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.UUID;

/**
 * 饿鬼
 */
public class   TheHungry extends AbstractMonster implements IMinion, Boss.BossPart {
    Mob owner;
    protected Vec3 initPos = Vec3.ZERO;
    public Vec3 lastInitPos = Vec3.ZERO;
    public boolean needLastPos = false;  //是否需要记录lastInitPos
    boolean isFree = false;

    Vec3 initDir = Vec3.ZERO;
    int phase = 0;
    int _phase = 60;

    float forwardSpeed = 0.25f; // 向前速度
    float forwardFreq = 0.15f; // 向前频率
    float backSpeed = 0.15f; // 返回起始点速度
    float backLen = this.getTarget()==null?5:10;  // 距离起始点方向的距离
    float minDis = 8.0f; // 距离起始点的最小距离
    float maxDis = 64.0f; // 距离起始点的最大距离
    float v_speed = 1.15f;   // 回到起始方向的速度
    int switchTime = 5; // 切换方向的时间

    private static final EntityDataAccessor<Vector3f> DATA_TRIGGER =  SynchedEntityData.defineId(TheHungry.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> DATA_IS_FREE = SynchedEntityData.defineId(TheHungry.class, EntityDataSerializers.BOOLEAN);

    public TheHungry(EntityType<? extends Monster> type, Level level, AttributeBuilder builder) {
        super(type, level, builder.setController((state,e)->{
            state.add(DefaultAnimations.genericIdleController(e));
        }));
        int ranDir = this.getRandom().nextInt(7);
        this.minDis += ranDir;
        this.maxDis += ranDir;
        this.noPhysics = true;
        this.collisionProperties = new CollisionProperties(5,20,0.3f);
    }

    public TheHungry(Level level,boolean isFree) {
        this(TEMonsterEntities.THE_HUNGRY.get(), level,new AbstractPrefab().getPrefab());
        this.setFree(isFree);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public boolean hasLineOfSight(Entity entity) {
        return distanceToSqr(entity) < 32 * 32;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(pSource.getEntity()!= null && pSource.getEntity().getType().is(TETags.EntityTypes.FLESH_ALLIANCE))
            return false;
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void move(@NotNull MoverType pType, @NotNull Vec3 motion) {
        if (dead) {
            super.move(pType, motion);
            return;
        }

        Vec3 collide = ((EntityAccessor) this).callCollide(motion);
        if (collide.x != motion.x) {
            motion = new Vec3(motion.x < 0 ? 0.22 : -0.22, motion.y, motion.z);
        }
        if (collide.y != motion.y) {
            boolean downward = motion.y < 0;
            motion = new Vec3(motion.x, downward ? Mth.clamp(-motion.y, 0.1, 0.22) : Mth.clamp(-motion.y, -0.22, -0.1), motion.z);
        }
        if (collide.z != motion.z) {
            motion = new Vec3(motion.x, motion.y, motion.z < 0 ? 0.3 : -0.3);
        }

        setDeltaMovement(motion);
        super.move(pType, motion);
    }

    public float getMaxDis(){
        return maxDis;
    }

    /**
     * 检查是否超出反圆形范围
     * 水平方向距离限制最短，垂直方向范围更大
     * @return true表示超出范围，需要回退
     */
    private boolean isOutOfRange() {
        Vec3 relativePos = position().subtract(initPos);
        double horizontalDistance = Math.sqrt(relativePos.x * relativePos.x + relativePos.z * relativePos.z);
        double verticalDistance = Math.abs(relativePos.y);
        double maxDis = this.getMaxDis();
        double maxHorizontalDis = maxDis;
        double maxVerticalDis = maxDis * 2.0;
        
        // 使用椭圆方程：水平距离/水平限制 + 垂直距离/垂直限制 > 1 时超出范围
        return horizontalDistance / maxHorizontalDis + verticalDistance / maxVerticalDis > 1.0;
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        if(this.owner == null)
            return super.canAttack(entity);
        else return this.owner.canAttack(entity);
    }

    protected Vec3 initDirection(LivingEntity owner){
        return owner.getForward().normalize();
    }

    @Override
    public void tick() {
        if ((this.owner == null || !this.owner.isAlive()) && !this.level().isClientSide && this.tickCount % 60 == 0) {
            if (this.isFree) {
                this.hurt(this.damageSources().starve(), 1.0F);
            } else {
                this.discard();
            }
        }

        super.tick();

        phase++;
        if (phase >= _phase) {
            phase = 0;
        }

        if (this.owner instanceof LivingEntity wall && !this.isFree) {
            Vec3 testDir = this.initDirection(wall);
            if(this.initDir == null || !this.initDir.equals(testDir)){
                initDir = testDir;
            }
        }else if(this.isFree){
            Vec3 pos = position();
            setTarget(level().getNearestPlayer(pos.x, pos.y, pos.z, 40, true));
            TEUtils.updateEntityRotation(this, this.getDeltaMovement().multiply(1, -1, 1));
        }

        if (initPos != null && initDir != null && !this.isFree) {
            Vec3 speed = Vec3.ZERO;
            LivingEntity target = getTarget();

            if (target != null) {
                Vec3 targetPos = getTarget().position().add(0,getTarget().getEyeHeight() * 0.5f,0);

                this.lookControl.setLookAt(target, 200, 85);
                this.lookAt(target, 200, 85);

                Vec3 c = targetPos.subtract(initPos);

                // 计算朝向目标的速度，避免垂直方向速度不一致的问题
                Vec3 toTarget = targetPos.subtract(position()).normalize();
                Vec3 toStart = initPos.subtract(position()).normalize();
                
                // 混合方向：既朝向目标，又保持在起始点附近
                Vec3 mixedDir = toTarget.add(toStart.scale(0.3f)).normalize();
                // 有目标且不超出范围时加大移动效率，包括垂直方向
                boolean hasTargetInRange = !isOutOfRange();
                Vec3 v_v = mixedDir.scale(v_speed * (hasTargetInRange ? 2.0f : 1.0f));
                
                speed = speed.add(v_v);
                Vec3 newDir = c.normalize();
                if (initDir != null) {
                    initDir = initDir.lerp(newDir, 0.3f);
                } else {
                    initDir = newDir;
                }
            } else {
                if (--switchTime <= 0) {
                    switchTime = random.nextInt(20) + 10;

                    if (this.owner instanceof WallOfFlesh wall) {
                        Vec3 baseDir = wall.getForward().normalize();
                        this.setYRot((float) Math.toDegrees(Math.atan2(-baseDir.x, baseDir.z)));

                        Vec3 testDir = new Vec3(Math.random() - 0.5f, Math.random() - 0.5f, Math.random() - 0.5f).normalize();
                        BlockPos testPos = BlockPos.containing(
                                testDir.scale(5).add(position())
                        );

                        if (level().getBlockState(testPos).isAir() && testPos.getY() > level().getMinBuildHeight()) {
                            if (initDir != null) {
                                initDir = initDir.lerp(testDir, 0.4f);
                            } else {
                                initDir = testDir;
                            }
                        }
                    }
                }
            }

            float flag = target == null ? 1 : 2;
            float sinValueX = (float) Math.sin(this.tickCount * forwardFreq * flag * 0.7f);
            float sinValueY = (float) Math.sin(this.tickCount * forwardFreq * flag * 1.3f);
            float sinValueZ = (float) Math.sin(this.tickCount * forwardFreq * flag);

            Vec3 shakeOffset = new Vec3(
                sinValueX * forwardSpeed * 0.35f,
                sinValueY * forwardSpeed * 0.45f,
                sinValueZ * forwardSpeed * 0.3f
            );
            
            // 有目标且不超出范围时减少正弦运动影响
            boolean hasTargetInRange = target != null && !isOutOfRange();
            float shakeMultiplier = hasTargetInRange ? 0.3f : 1.0f;
            Vec3 forward = initDir.normalize().scale(forwardSpeed * (1.0f + (hasTargetInRange ? 0.5f : 0.0f))).add(shakeOffset.scale(shakeMultiplier));
            Vec3 v_back;

            if (isOutOfRange()) {
                // 如果距离起始点太远，强制返回到最小距离位置
                Vec3 minDisPos = initPos.add(initDir.scale(minDis));
                v_back = minDisPos.subtract(position()).normalize().scale(backSpeed * 2.0f);
            } else {
                Vec3 minDisPos = initPos.add(initDir.scale(minDis));
                float backOffset = backLen * 0.5f * (2.25f + (float) Math.sin(this.tickCount * 0.05 * flag));
                Vec3 backPos = minDisPos.add(initDir.scale(backOffset));
                
                // 没有目标或超出范围时使用原本力度，否则使用一半力度
                float backMultiplier = (target == null || isOutOfRange()) ? 1.0f : 0.5f;
                v_back = backPos.subtract(position()).scale(backSpeed * backMultiplier);
            }

            Vec3 finalSpeed = speed.add(forward).add(v_back);
            double len = finalSpeed.length();
            if (len > 0.35f) {
                finalSpeed = finalSpeed.scale(0.35f / len);
            }
            this.setDeltaMovement(finalSpeed);
        }else if(this.isFree){
            Vec3 speed = Vec3.ZERO;
            LivingEntity target = getTarget();

            if (target != null) {
                Vec3 targetPos = getTarget().position().add(0,getTarget().getEyeHeight() * 0.5f,0);

                this.lookControl.setLookAt(target, 200, 85);
                this.lookAt(target, 200, 85);

                Vec3 c = targetPos.subtract(initPos);

                // 计算朝向目标的速度，避免垂直方向速度不一致的问题
                Vec3 toTarget = targetPos.subtract(position()).normalize();
                Vec3 toStart = initPos.subtract(position()).normalize();

                // 混合方向：既朝向目标，又保持在起始点附近
                Vec3 mixedDir = toTarget.add(toStart.scale(0.3f)).normalize();
                // 有目标且不超出范围时加大移动效率，包括垂直方向
                Vec3 v_v = mixedDir.scale(v_speed);

                speed = speed.add(v_v);
                Vec3 newDir = c.normalize();
                if (initDir != null) {
                    initDir = initDir.lerp(newDir, 0.3f);
                } else {
                    initDir = newDir;
                }
            }

            float flag = 2;

            // 有目标且不超出范围时减少正弦运动影响
            Vec3 forward = initDir.normalize().scale(forwardSpeed * (1.25f));
            Vec3 v_back;

            Vec3 minDisPos = initPos.add(initDir.scale(minDis));
                float backOffset = backLen * 0.5f * (2.25f + (float) Math.sin(this.tickCount * 0.05 * flag));
                Vec3 backPos = minDisPos.add(initDir.scale(backOffset));

                // 没有目标或超出范围时使用原本力度，否则使用一半力度
                float backMultiplier = 0f;
                v_back = backPos.subtract(position()).scale(backSpeed * backMultiplier);


            Vec3 finalSpeed = speed.add(forward).add(v_back);
            double len = finalSpeed.length();
            if (len > 0.35f) {
                finalSpeed = finalSpeed.scale(0.35f / len);
            }
            this.setDeltaMovement(finalSpeed);
        }
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TRIGGER, Vec3.ZERO.toVector3f());
        builder.define(DATA_OWNER_UUID, Optional.empty());
        builder.define(DATA_IS_FREE, false);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("initPos")) {
            Vector3f initPos = new Vector3f(tag.getFloat("initPosX"), tag.getFloat("initPosY"), tag.getFloat("initPosZ"));
            this.entityData.set(DATA_TRIGGER, initPos);
            this.initPos = new Vec3(initPos);
        }
        if(tag.contains("isFree")) {
            this.entityData.set(DATA_IS_FREE, tag.getBoolean("isFree"));
            this.isFree = tag.getBoolean("isFree");
        }
        minion_readData(tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if(initPos!= null) {
            tag.putFloat("initPosX", (float) initPos.x);
            tag.putFloat("initPosY", (float) initPos.y);
            tag.putFloat("initPosZ", (float) initPos.z);
        }
        tag.putBoolean("isFree", this.isFree);
        minion_saveData(tag);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(level().isClientSide) {
            if (key == DATA_TRIGGER) {
                this.initPos = new Vec3(this.entityData.get(DATA_TRIGGER));
            }
            if (key == DATA_IS_FREE) {
                this.isFree = this.entityData.get(DATA_IS_FREE);
            }
        }
    }

    @Override
    public void onAddedToLevel(){
        super.onAddedToLevel();
        setNoGravity(true);
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        if(this.initPos == null){
            return super.getBoundingBoxForCulling().inflate(10);
        }
        return new AABB(this.position(), this.initPos);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);

        boolean flag = this.owner instanceof WallOfFlesh && this.owner.isAlive();
        if (owner instanceof WallOfFlesh &&!level().isClientSide && flag && !this.isFree && this.getInitPos() != null) {
             TheHungry hungry = new TheHungry(level(), true) {
                @Override
                protected boolean shouldDropLoot() {
                    return false;
                }
            };
            hungry.setNoGravity(true);
            hungry.setPos(this.position());
            hungry.setRot(this.getYRot(), this.getXRot());
            hungry.minion_setOwner(this.owner);
            level().addFreshEntity(hungry);
        }
    }

    public Vec3 getInitPos(){
        return initPos;
    }

    public void setInitPos(Vector3f initPos){
        if(!isFree) {
            this.entityData.set(DATA_TRIGGER, initPos);
            this.initPos = new Vec3(initPos);
        }
    }

    // 对于不是运动的情景，不需要更新服务端位置
    public void setClientInitPos(Vector3f initPos){
        if(needLastPos) this.lastInitPos = this.initPos;
        this.initPos = new Vec3(initPos);
    }

    public Vec3 getDir(){
        return this.owner instanceof WallOfFlesh wall?wall.getForward():Vec3.ZERO;
    }

    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(TheHungry.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    public EntityDataAccessor<Optional<UUID>> getDATA_OWNER_UUID() {
        return DATA_OWNER_UUID;
    }

    public void minion_setOwner(Entity owner) {
        if (owner instanceof Mob wall) {
            minion_setOwnerUUID(owner.getUUID());
            this.owner = wall;
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> state.setAndContinue(RawAnimation.begin().thenLoop("bait"))));
    }

    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public boolean isFree() {
        return this.entityData.get(DATA_IS_FREE);
    }

    public void setFree(boolean free) {
        this.isFree = free;
        this.entityData.set(DATA_IS_FREE, free);
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return TESounds.THE_HUNGRY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TESounds.THE_HUNGRY_DEATH.get();
    }
}
