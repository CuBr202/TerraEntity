package org.confluence.terraentity.entity.boss;

import net.minecraft.ChatFormatting;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.jmx.Server;
import org.confluence.terraentity.api.entity.Boss;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.entity.monster.demoneye.DemonEye;
import org.confluence.terraentity.entity.proj.TrailProjectile;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEProjectileEntities;
import org.confluence.terraentity.utils.TEUtils;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import javax.annotation.Nullable;

public class WallOfFleshEye extends AbstractTerraBossBase<WallOfFleshEye> implements Boss {

    public WallOfFlesh parentMob;

    @Nullable
    private LivingEntity clientSideCachedAttackTarget;
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(WallOfFleshEye.class, EntityDataSerializers.INT);
    private static final float DAMAGE = 4f;//一阶段接触伤害

    //定义技能参数
    private int summonCDAll = 60; //仆从召唤cd
    private int summonCD = summonCDAll;

    public WallOfFleshEye(EntityType<WallOfFleshEye> entityType, Level level) {
        super(entityType, level,WallOfFlesh.MAX_HEALTHS,2);
        //初始属性
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(DAMAGE);
        getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(16);
        //getAttribute(FOLLOW_RANGE).setBaseValue(32.0);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.playSound(TESounds.ROAR.get());
        if(ServerConfig.BOSS_NO_PHYSICS.get())
            this.noPhysics = true;
        collisionProperties.attackInternal = 1;
        collisionProperties.detectInternal = 1;
    }

    public WallOfFleshEye(Level level) {
        this(TEBossEntities.WALL_OF_FLESH_EYE.get(), level);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return super.canAttack(target) && !(target instanceof DemonEye);
    }

    protected boolean canShoot(Entity target,float range) {
        return target!= null && TEUtils.angleBetween(this.getLookAngle(), target.position().subtract(this.position())) < range;
    }

    protected void registerGoals() {
        //this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 45F));
        //this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
        //this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
    }

    @Override // 受伤音效
    protected SoundEvent getHurtSound(DamageSource damageSource) {return TESounds.ROUTINE_HURT.get();}

    @Override
    protected SoundEvent getDeathSound() {
        return TESounds.ROUTINE_DEATH.get();
    }
    @Override
    public boolean isNoGravity(){ return true; }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(WallOfFlesh.isWallOfFleshMob(pSource.getEntity())||WallOfFlesh.isWallOfFleshMob(pSource.getDirectEntity()))
            return false;
        boolean flag = parentMob!=null && parentMob.isAlive();
     return super.hurt(pSource, pAmount) &&  flag && parentMob.hurt(pSource,pAmount);
    }

    public void setParent(WallOfFlesh parent){
        this.parentMob = parent;
    }

    @Override
    public boolean shouldBeSaved(){
        if(this.parentMob != null)return this.parentMob.shouldBeSaved();
        return  false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.parentMob== null || !this.parentMob.isAlive()) return;

        Vec3 forward = this.parentMob.getForward().normalize();
        if(getTarget() !=null && getTarget().isAlive()) {
            Vec3 toTarget = getTarget().position().subtract(this.parentMob.position());
            if(this.hasActiveAttackTarget()&&getTarget() != this.getActiveAttackTarget()) {
                this.setActiveAttackTarget(getTarget().getId());
            }else if (forward.dot(new Vec3(toTarget.x, 0, toTarget.z).normalize()) >= 0) {
                this.setActiveAttackTarget(getTarget().getId());
            }else this.setActiveAttackTarget(0);
        }else if(this.getActiveAttackTarget()!=null && this.getActiveAttackTarget()instanceof Player player && (player.isCreative() || player.isSpectator())) this.setActiveAttackTarget(0);
        // 生成仆从
        if (!this.level().isClientSide) {

            if (getTarget() == null || !getTarget().isAlive()) return;

            if(this.getHealth()!= parentMob.getHealth())this.setHealth(parentMob.getHealth());

            if (--summonCD > 0) return;
            float healthPercent = parentMob.getHealthPercentage();
            summonCD = healthPercent < 0.5f? (int) (summonCDAll * Math.clamp(healthPercent, 0.2F, 1.0f)) : summonCDAll;
            if (canShoot(this.getTarget(),0.75F)) {
                TrailProjectile proj = TEProjectileEntities.TRAIL_PROJECTILE.get().create(level());
                if (proj!=null) {
                    proj.setDamage((float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    proj.setExistTick(200);
                    proj.setOwner(this);
                    proj.setTrailColor(ChatFormatting.DARK_PURPLE.getColor());
                    proj.setPos(this.position());
                    proj.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST));
                    Vec3 dir = target.getEyePosition().add(0, 0.15F, 0).subtract(this.position());
                    proj.shoot(dir.x, dir.y, dir.z, 1, 0.15f);
                    level().addFreshEntity(proj);
                }
            }
        }
    }

    public double getEyeY() {
        return this.position().y + this.getHitbox().getYsize() / 2;
    }

    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return this.parentMob==null?super.addEffect(effectInstance, entity):this.parentMob.addEffect(effectInstance, entity);
    }

    public boolean canUsePortal(boolean allowPassengers) {
        return this.parentMob==null?super.canUsePortal(allowPassengers):this.parentMob.canUsePortal(allowPassengers);
    }

    public boolean isInvulnerableTo(DamageSource source) {
        if(source.is(DamageTypeTags.IS_FIRE)||source.is(DamageTypeTags.IS_DROWNING)){
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_ATTACK_TARGET, 0);
    }

    void setActiveAttackTarget(int activeAttackTargetId) {
        this.entityData.set(DATA_ID_ATTACK_TARGET, activeAttackTargetId);
    }

    public boolean hasActiveAttackTarget() {
        return this.entityData.get(DATA_ID_ATTACK_TARGET) != 0;
    }

    @Nullable
    public LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) {
            return null;
        } else if (this.level().isClientSide) {
            if (this.clientSideCachedAttackTarget != null) {
                return this.clientSideCachedAttackTarget;
            } else {
                Entity entity = this.level().getEntity(this.entityData.get(DATA_ID_ATTACK_TARGET));
                if (entity instanceof LivingEntity living) {
                    this.clientSideCachedAttackTarget = living;
                    return this.clientSideCachedAttackTarget;
                } else {
                    return null;
                }
            }
        } else {
            if(this.getTarget() !=null && this.getTarget().isAlive()) {
                return this.getTarget();
            }else if (this.clientSideCachedAttackTarget != null) {
                return this.clientSideCachedAttackTarget;
            } else {
                Entity entity = this.level().getEntity(this.entityData.get(DATA_ID_ATTACK_TARGET));
                if (entity instanceof LivingEntity living) {
                    this.clientSideCachedAttackTarget = living;
                    return this.clientSideCachedAttackTarget;
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_ID_ATTACK_TARGET.equals(key)) {
            this.clientSideCachedAttackTarget = null;
        }
    }

    @Override
    public void addSkills() {}

    @Override
    public boolean shouldEscape() {
        return false;
    }

    @Override
    public boolean shouldShowBossBar(){
        return false;
    }

    public boolean isMainBody(){
        return false;
    }
}