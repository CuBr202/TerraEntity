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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.Boss;
import org.confluence.terraentity.entity.ai.ICollisionAttackEntity;
import org.confluence.terraentity.entity.ai.goal.LookForwardWanderFlyGoal;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Skeletron extends AbstractTerraBossBase<Skeletron> implements GeoEntity, Boss, ICollisionAttackEntity<Skeletron> {
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);
    public int phase = 0;
    private boolean enraged = false;
    private double acceleration = 0.2;
    private double maxSpeed = 2;
    private boolean expert = false;
    private final CollisionProperties COLLISION_PROP = new CollisionProperties(0, 0, 0);
    public static final EntityDataAccessor<Boolean> DATA_SPINNING = SynchedEntityData.defineId(Skeletron.class, EntityDataSerializers.BOOLEAN);

    public Skeletron(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level, 10, 1);
        setDiscardFriction(true);
        noPhysics = true;
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
        }
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
        this.goalSelector.addGoal(10, new LookForwardWanderFlyGoal(this,0.3f, 0));
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
            server = true;
        }
        super.tick();
        lookAt(90);
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

    private class FloatGoal extends Goal {

        @Override
        public boolean canUse() {
//            return false;
            return !enraged && phase < 267 && level().isNight() && getTarget() != null;
        }

        @Override
        public void tick() {
            LivingEntity target = getTarget();
            Vec3 velocity = getDeltaMovement().scale(10);
            double distance = target.position().subtract(position()).length();
            Vec3 acc = target.position().add(0, 5, 0)
                .subtract(position())
                .subtract(velocity)
                .normalize().scale(Math.max(acceleration * (0.07 * distance - 0.29), 0.01));
            Vec3 resultVelocity = getDeltaMovement().add(acc);
            double resultSpeed = resultVelocity.length();
            if (resultSpeed > maxSpeed) {
                resultVelocity = resultVelocity.scale(maxSpeed / resultSpeed);
            }
            setDeltaMovement(resultVelocity);
        }

        @Override
        public void start() {
            // 有概率换一个目标，别只追着一个人跑，除非这一个人的仇恨值比所有人都高
            if (random.nextBoolean()) {
                setTarget(findTarget());
            }
        }
    }

    private class SpinGoal extends Goal {

        @Override
        public boolean canUse() {
//            return getTarget() != null;
            return (enraged || phase >= 267 || level().isDay()) && getTarget() != null;
        }

        @Override
        public void tick() {
            Vec3 vec = getTarget().position().subtract(position());
            if (expert) {
                double distance = vec.length();
                setDeltaMovement(vec.normalize().scale(Mth.clamp(0.01 * distance + 0.16, 0.2, 0.45)));
            }else{
                setDeltaMovement(vec.normalize().scale(0.2));
            }
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
}
