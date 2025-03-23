package org.confluence.terraentity.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.entity.ai.Boss;
import org.confluence.terraentity.entity.ai.ICollisionAttackEntity;
import org.confluence.terraentity.entity.ai.goal.LookForwardWanderFlyGoal;
import org.confluence.terraentity.entity.proj.SkullProjectile;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class Skeletron extends AbstractTerraBossBase<Skeletron> implements GeoEntity, Boss, ICollisionAttackEntity<Skeletron> {
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);
    public int phase = 0;
    public boolean enraged = false;
    protected double acceleration;
    protected double maxSpeed;
    protected boolean expert = false;
    private boolean ftw;
    public final List<SkeletronHand> hands = new ArrayList<>();

    public static final CollisionProperties COLLISION_PROP = new CollisionProperties(0, 0, 0);
    public static final EntityDataAccessor<Boolean> DATA_SPINNING = SynchedEntityData.defineId(Skeletron.class, EntityDataSerializers.BOOLEAN);

    public Skeletron(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level, 10, 1);
        setDiscardFriction(true);
        if (ServerConfig.BOSS_NO_PHYSICS.get()) {
            noPhysics = true;
        }
        if(!(level instanceof ServerLevel serverLevel)) return;
        if (serverLevel.getDifficulty() == Difficulty.EASY) {
            acceleration = 0.07;
            maxSpeed = 0.7;
        }else {
            acceleration = 0.1;
            maxSpeed = 1;
            expert = true;
        }
        if (TEUtils.isFTWWorld(serverLevel)) {
            acceleration = 0.16;
            maxSpeed = 2;
            expert = true;
            ftw = true;
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public void addSkills() {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return TESounds.TR_SKELETON_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TESounds.TR_ZOMBIE_DEATH.get();
    }

    @Override
    protected void registerGoals() {
        targetSelector.addGoal(1,new FloatGoal());
        targetSelector.addGoal(1,new SpinGoal());
        targetSelector.addGoal(1,new ShootSkullGoal());
        goalSelector.addGoal(10, new LookForwardWanderFlyGoal(this,0.3f, 0));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SPINNING, false);
    }

    @Override
    public void tick() {
        boolean server = false;
        if(level() instanceof ServerLevel) {
            if (level().isDay()) {
                enraged = true;
            }
            hands.removeIf(hand -> !hand.isAlive());
            server = true;
        }
        super.tick();
        if (server) {
            phase++;
            if (phase > 400) {
                phase = 0;
            }
        }else {
            if(getEntityData().get(DATA_SPINNING)) {
                phase++;
            }else {
                phase = 0;
            }
        }
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        if (!super.canAttack(entity)) return false;
        return !(entity instanceof Skeletron);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public CollisionProperties getCollisionProperties() {
        return COLLISION_PROP;
    }

    @Override
    public boolean shouldDoCollision() {
        return true;
    }




    @Override
    public void firstSpawn() {
        if (isMainBody() && !level().isClientSide) {
            SkeletronHand hand1 = new SkeletronHand(TEEntities.SKELETRON_HAND.get(), level(), this, SkeletronHand.HandSide.LEFT);
            SkeletronHand hand2 = new SkeletronHand(TEEntities.SKELETRON_HAND.get(), level(), this, SkeletronHand.HandSide.RIGHT);
            hand1.setPos(position());
            hand2.setPos(position());
            level().addFreshEntity(hand1);
            level().addFreshEntity(hand2);
            hands.add(hand1);
            hands.add(hand2);
        }
    }

    public void attachHand(SkeletronHand hand) {
        hands.add(hand);
    }

    public class FloatGoal extends Goal {
        @Override
        public boolean canUse() {
//            return false;
            return !enraged && phase < 267 && level().isNight() && getTarget() != null;
        }

        public double getDamping(){
            return 10;
        }

        public Vec3 getTargetPosition() {
            return getTarget().position().add(0, 5, 0);
        }

        @Override
        public void tick() {
            Vec3 targetPos = getTargetPosition();
            Vec3 velocity = getDeltaMovement().scale(getDamping());
            double distance = targetPos.add(0, -5, 0).subtract(position()).length();
            Vec3 acc = targetPos
                .subtract(position())
                .subtract(velocity)
                .normalize().scale(Math.max(acceleration * (0.07 * distance - 0.29), 0.01));
            Vec3 resultVelocity = getDeltaMovement().add(acc);
            double resultSpeed = resultVelocity.length();
            if (resultSpeed > maxSpeed) {
                resultVelocity = resultVelocity.scale(maxSpeed / resultSpeed);
            }
            setDeltaMovement(resultVelocity);
            lookAt(90);
        }

        @Override
        public void start() {
            // 有概率换一个目标，别只追着一个人跑，除非这一个人的仇恨值比所有人都高
            if (random.nextBoolean()) {
                setTarget(findTarget());
            }
        }
    }

    public class SpinGoal extends Goal {

        @Override
        public boolean canUse() {
//            return getTarget() != null;
            return (enraged || phase >= 267 || level().isDay()) && getTarget() != null;
        }

        @Override
        public void tick() {
            Vec3 vec = getTarget().position().subtract(position());
            if (enraged) { // 白天最快
                setDeltaMovement(vec.normalize().scale(1));
            }else if (expert) { // 专家以上越远越快
                double distance = vec.length();
                double speed = Mth.clamp(0.01 * distance + 0.16, 0.2, 0.45);
                if (ftw) {
                    speed *= 1.3;
                }
                int handCount = hands.size();
                speed *= handCount == 1 ? 1.05 : handCount == 0 ? 1.1 : 1;
                setDeltaMovement(vec.normalize().scale(speed));
            }else{ //简单难度固定超慢速
                setDeltaMovement(vec.normalize().scale(0.2));
            }
            lookAt(90);
        }

        @Override
        public void start() {
            playSound(TESounds.ROAR.get());
            getEntityData().set(DATA_SPINNING, true);
        }

        @Override
        public void stop() {
            getEntityData().set(DATA_SPINNING, false);
        }
    }

    public class ShootSkullGoal extends Goal{

        @Override
        public boolean canUse() {
//            return true;
            return expert && getTarget() != null && !getEntityData().get(DATA_SPINNING) && (getHealth() / getMaxHealth() < 0.75 || hands.size() < 2);
        }

        @Override
        public void tick() {
            int interval = hands.isEmpty() ? 7 : 13;
            if (ftw) {
                interval = (int) (interval * 0.8);
            }
            if (tickCount % interval == 0) {
                SkullProjectile skull = new SkullProjectile(TEEntities.SKULL.get(), level(), getTarget());
                skull.setPos(position());
                skull.setOwner(Skeletron.this);
                skull.setDeltaMovement(getTarget().position().subtract(position()).normalize().scale(0.001));
                level().addFreshEntity(skull);
            }
        }
    }
}
