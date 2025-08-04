package org.confluence.terraentity.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.api.entity.Boss;
import org.confluence.terraentity.entity.ai.goal.LookForwardWanderFlyGoal;
import org.confluence.terraentity.entity.proj.SkullProjectile;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEProjectileEntities;
import org.confluence.terraentity.network.s2c.SyncBossEventHealthPacket;
import org.confluence.terraentity.utils.AdapterUtils;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 骷髅王
 */
public class Skeletron extends AbstractTerraBossBase<Skeletron> implements Boss {

    private float projDamageFactor = 0.33f; // 弹幕伤害倍率，相对于攻击力
    private float attackDamage = 18.2f; // 攻击伤害
    public int phase = 0;
    public boolean enraged = false;
    protected double acceleration;
    protected double maxSpeed;
    protected boolean expert = false;
    private boolean ftw;
    public final List<SkeletronHand> hands = new ArrayList<>();
    public final List<SkeletronHand> _hands = new ArrayList<>(); // 用于计算boss总血量
    public static final EntityDataAccessor<Boolean> DATA_SPINNING = SynchedEntityData.defineId(Skeletron.class, EntityDataSerializers.BOOLEAN);

    public Skeletron(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level, 2288, 10);
        this.setAttactDamage(attackDamage);
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
        collisionProperties = new CollisionProperties(1,1,0.5f);
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
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return TESounds.TR_SKELETON_HURT.get();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_DROWNING)) {
            return false;
        }
        return super.hurt(pSource, pAmount); // confluence mixin here
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SPINNING, false);
    }

    @Override
    public void tick() {
        boolean server = false;
        if(level() instanceof ServerLevel && !(this instanceof SkeletronHand)) {
            enraged = level().isDay();
            hands.removeIf(hand -> !hand.isAlive());
            if(hands.isEmpty()){
                // 失去手时防御归零
                this.getAttribute(Attributes.ARMOR).setBaseValue(0);
            }
            server = true;
            if(target!= null && position().y < target.getY()){
                addDeltaMovement(new Vec3(0,0.02f,0));
            }
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
    public float[] getBossEventProgress(){
        float value = getHealth();
        float getMax = getMaxHealth();
        for (SkeletronHand hand : _hands) {
            value += hand.getHealth();
            getMax += hand.getMaxHealth();
        }
        AdapterUtils.sendToAllPlayers(new SyncBossEventHealthPacket(bossEvent.getId(), value, getMax));
        return new float[]{value , getMax};
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
    public boolean shouldDoCollision() {
        return super.shouldDoCollision();
    }




    @Override
    public void firstSpawn() {
        if (isMainBody() && !level().isClientSide) {
            SkeletronHand hand1 = TEUtils.spawnEntity(()->new SkeletronHand(TEBossEntities.SKELETRON_HAND.get(), level(), this, SkeletronHand.HandSide.LEFT), (ServerLevel) level(), position());
            SkeletronHand hand2 = TEUtils.spawnEntity(()->new SkeletronHand(TEBossEntities.SKELETRON_HAND.get(), level(), this, SkeletronHand.HandSide.RIGHT), (ServerLevel)level(),position());
//            ;
//            new SkeletronHand(TEBossEntities.SKELETRON_HAND.get(), level(), this, SkeletronHand.HandSide.RIGHT);
            if (hand1 != null) {
                hand1.setPos(position());
                hands.add(hand1);
                _hands.add(hand1);
            }
            if (hand2 != null) {
                hand2.setPos(position());
                hands.add(hand2);
                _hands.add(hand2);
            }

            this.playSound(TESounds.ROAR.get());
        }
    }

    public void attachHand(SkeletronHand hand) {
        hands.add(hand);
        _hands.add(hand);
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        // confluence mixin here
        return super.addEffect(effectInstance, entity);
    }

    public class FloatGoal extends Goal {
        boolean crazy = false;
        @Override
        public boolean canUse() {
//            return false;
            return !enraged && phase < 267 && level().isNight() && getTarget() != null;
        }

        public double getDamping(){
            return 10;
        }

        public Vec3 getTargetPosition() {
            if(getTarget() == null){
                return position();
            }
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
            if(crazy){
                if(getTarget() != null) {
                    addDeltaMovement(getTarget().position().subtract(position()).scale(0.01f));
                }
            }
            lookAt(90);
        }

        @Override
        public void start() {
            // 有概率换一个目标，别只追着一个人跑，除非这一个人的仇恨值比所有人都高
            if (random.nextBoolean()) {
                setTarget(findTarget());
            }
            crazy = random.nextFloat() < 0.3f;
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
            if(getTarget() == null){
                return;
            }
            Vec3 vec = getTarget().position().subtract(position());
            if (enraged) { // 白天最快
                setDeltaMovement(vec.normalize().scale(1));
            }else if (expert) { // 专家以上越远越快
                double distance = vec.length();
                double speed = Mth.clamp(0.01 * distance + 0.16, 0.22, 0.48);
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
            if(getTarget() == null){
                return;
            }
            int interval = hands.isEmpty() ? 7 : 13;
            if (ftw) {
                interval = (int) (interval * 0.8);
            }
            if (tickCount % interval == 0) {
                SkullProjectile skull = new SkullProjectile(TEProjectileEntities.SKULL.get(), level(), getTarget());
                skull.addDamage((float) getAttribute(Attributes.ATTACK_DAMAGE).getValue() * projDamageFactor);
                skull.setPos(position());
                skull.setOwner(Skeletron.this);
                skull.setDeltaMovement(getTarget().position().subtract(position()).normalize().scale(0.001));
                level().addFreshEntity(skull);
            }
        }
    }
    protected BossEvent.BossBarColor getBossBarColor(){
        return BossEvent.BossBarColor.WHITE;
    };

    protected boolean shouldOverPlayer(){
        return true;
    }
}
