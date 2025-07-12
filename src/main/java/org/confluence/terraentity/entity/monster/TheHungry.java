package org.confluence.terraentity.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.IMinion;
import org.confluence.terraentity.entity.boss.WallOfFlesh;
import org.confluence.terraentity.entity.monster.demoneye.DemonEyeSurroundTargetGoal;
import org.confluence.terraentity.entity.monster.demoneye.DemonEyeWanderGoal;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.UUID;

/**
 * 饿鬼
 */
public class TheHungry extends AbstractMonster implements IMinion<TheHungry>{
    Mob owner;
    protected Vec3 initPos;
    boolean isFree = false;

    Vec3 initDir;
    int phase = 0;
    int _phase = 100;

    float forwardSpeed = 0.15f; // 向前速度
    float forwardFreq = 0.15f; // 向前频率
    float backSpeed = 0.1f; // 返回起始点速度
    float backLen = this.getTarget()==null?5:10;  // 距离起始点方向的距离
    float v_speed = 3.5f;   // 回到起始方向的速度
    int switchTime = 20; // 切换方向的时间
    public DemonEyeSurroundTargetGoal surroundTargetGoal;
    public DemonEyeWanderGoal wanderGoal;
    private static final EntityDataAccessor<Vector3f> DATA_TRIGGER =  SynchedEntityData.defineId(TheHungry.class, EntityDataSerializers.VECTOR3);

    public TheHungry(EntityType<? extends Monster> type, Level level, AttributeBuilder builder) {
        super(type, level, builder.setController((state,e)->{
            state.add(DefaultAnimations.genericIdleController(e));
        }));
        this.noPhysics = true;
        this.collisionProperties = new CollisionProperties(5,20,0.3f);
    }

    public TheHungry(Level level,boolean isFree) {
        this(TEMonsterEntities.THE_HUNGRY.get(), level,new AbstractPrefab(30,1,10,32,0.75f,1).getPrefab());
        this.isFree = isFree;
    }

    @Override
    protected void registerGoals() {
        surroundTargetGoal =  new DemonEyeSurroundTargetGoal(this){
            @Override
            public boolean canUse(){
                return mob.getTarget() != null && mob.getTarget().isAlive();
            }
        };
        wanderGoal = new DemonEyeWanderGoal(this){
            @Override
            public boolean canUse(){
                return mob.getTarget() == null && mob instanceof TheHungry hungry && hungry.isFree;
            }
        };

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
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
        if(WallOfFlesh.isWallOfFleshMob(pSource.getEntity())||WallOfFlesh.isWallOfFleshMob(pSource.getDirectEntity()))
            return false;
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void tick() {
        // 饥饿逻辑优化
        if (this.owner == null && !this.level().isClientSide && this.tickCount % 60 == 0) {
            if (this.isFree) {
                this.hurt(this.damageSources().starve(), 1.0F);
            } else {
                this.discard();
            }
        }
        super.tick();

        // 阶段计算优化
        phase++;
        if (phase >= _phase) {
            phase = 0;
        }
        int stage = (phase * 2) / _phase + 1;  // 避免浮点运算

        // 初始化方向优化
        if (initDir == null && this.owner instanceof WallOfFlesh wall && !this.isFree) {
            initDir = wall.getForward().normalize();
        }

        if (initPos != null && initDir != null && !this.isFree) {
            Vec3 speed = Vec3.ZERO;
            LivingEntity target = getTarget();

            if (target != null) {
                Vec3 targetPos = target.position().add(0, target.getEyeHeight() * 0.5f, 0);

                this.lookControl.setLookAt(target, 200, 85);
                this.lookAt(target, 200, 85);

                Vec3 c = targetPos.subtract(initPos);
                Vec3 a = initPos.subtract(position());
                Vec3 b = targetPos.subtract(position());

                float sp = stage == 2 ? v_speed * 0.15f : v_speed;
                double c_len = initDir.subtract(targetPos).length();
                double v_len = a.dot(b) / c_len;

                speed = speed.add(
                        a.cross(b)
                                .cross(c)
                                .normalize()
                                .scale(-v_len * sp)
                );

                initDir = c.normalize();
            } else {
                // 无目标时的随机方向优化
                if (--switchTime <= 0) {
                    switchTime = random.nextInt(100) + 100;  // 减少随机范围

                    if (this.owner instanceof WallOfFlesh wall) {
                        Vec3 baseDir = wall.getForward().normalize();
                        this.setYRot((float) Math.toDegrees(Math.atan2(-baseDir.x, baseDir.z)));

                        // 随机偏移优化
                        Vec3 randomOffset = new Vec3(
                                random.nextFloat() * 0.4f - 0.2f,  // X轴：-0.2 ~ +0.2
                                random.nextFloat() * 0.2f - 0.1f,  // Y轴：-0.1 ~ +0.1
                                random.nextFloat() * 0.4f - 0.2f   // Z轴：-0.2 ~ +0.2
                        );

                        Vec3 testDir = baseDir.add(randomOffset).normalize();
                        BlockPos testPos = BlockPos.containing(
                                testDir.scale(5).add(position())
                        );

                        if (level().getBlockState(testPos).isAir() && testPos.getY() > level().getMinBuildHeight()) {
                            initDir = testDir;
                        }
                    }
                }
            }

            float flag = target == null ? 1 : 2;
            float sinValue = (float) Math.sin(this.tickCount * forwardFreq * flag);
            Vec3 forward = initDir.normalize().scale(forwardSpeed * sinValue);

            float lengthFactor = target == null ? 1 : stage;
            float backOffset = backLen * lengthFactor * 0.5f * (2.25f + (float) Math.sin(this.tickCount * 0.05 * flag));
            Vec3 backPos = initPos.add(initDir.scale(backOffset));
            Vec3 v_back = backPos.subtract(position()).scale(backSpeed);

            Vec3 finalSpeed = speed.add(forward).add(v_back);
            double len = finalSpeed.length();
            if (len > 0.35f) {
                finalSpeed = finalSpeed.scale(0.35f / len);
            }
            this.setDeltaMovement(finalSpeed);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TRIGGER, Vec3.ZERO.toVector3f());
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("initPos")) {
            Vector3f initPos = new Vector3f(tag.getFloat("initPosX"), tag.getFloat("initPosY"), tag.getFloat("initPosZ"));
            this.entityData.set(DATA_TRIGGER, initPos);
            this.initPos = new Vec3(initPos);
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
        minion_saveData(tag);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(key == DATA_TRIGGER && level().isClientSide){
            this.initPos = new Vec3(this.entityData.get(DATA_TRIGGER));
        }
    }

    @Override
    public void onAddedToWorld(){
        super.onAddedToWorld();
        setNoGravity(true);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(10);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);

        boolean flag = this.owner instanceof WallOfFlesh && this.owner.isAlive();
        if (!level().isClientSide && flag && !this.isFree && this.getInitPos() != null) {
             TheHungry hungry = new TheHungry(level(), true) {
                @Override
                protected boolean shouldDropLoot() {
                    return false;
                }
            };
            if(surroundTargetGoal!= null)hungry.goalSelector.addGoal(0, this.surroundTargetGoal);
            if(wanderGoal!= null)hungry.goalSelector.addGoal(1, this.wanderGoal);
            hungry.setPos(this.position());
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

    public Vec3 getDir(){
        return this.owner instanceof WallOfFlesh wall?wall.getForward():Vec3.ZERO;
    }

    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(TheHungry.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    public EntityDataAccessor<Optional<UUID>> getDATA_OWNER_UUID() {
        return DATA_OWNER_UUID;
    }

    public void minion_setOwner(Entity owner) {
        if (owner instanceof WallOfFlesh wall) {
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
}
