package org.confluence.terraentity.entity.boss.wallofflesh;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.api.entity.Boss;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import org.confluence.terraentity.entity.monster.BaseWorm;
import org.confluence.terraentity.entity.monster.BaseWormPart;
import org.confluence.terraentity.entity.monster.demoneye.DemonEye;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.utils.TEUtils;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;


import javax.annotation.Nullable;

public class WallOfFleshMouse extends AbstractTerraBossBase implements Boss {

    public WallOfFlesh parentMob;

    private static final float DAMAGE = 39f;//一阶段接触伤害
    private int pendingSpawns = 0; // 待生成数量
    private int spawnInterval = 0; // 间隔计时器

    private int summonCDAll = 1000; //仆从召唤cd
    private int summonCD = summonCDAll;


    public WallOfFleshMouse(EntityType<WallOfFleshMouse> entityType, Level level) {
        super(entityType, level);
        //初始属性
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(DAMAGE);
        getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32f);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.playSound(TESounds.ROAR.get());
        if(ServerConfig.BOSS_NO_PHYSICS.get())
            this.noPhysics = true;
        collisionProperties.attackInternal = 1;
        collisionProperties.detectInternal = 1;
    }

    public WallOfFleshMouse(Level level) {
        this(TEBossEntities.WALL_OF_FLESH_MOUTH.get(), level);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return super.canAttack(target) && !(target instanceof DemonEye);
    }

    public void tick() {
        super.tick();
        if(this.getTarget() == null || !this.getTarget().isAlive() || this.parentMob == null || !this.parentMob.isAlive())return;
        if (spawnInterval > 0) {
            if (--spawnInterval <= 0 && pendingSpawns > 0) {
                spawnLeech(getTarget());
                pendingSpawns--;
                spawnInterval = pendingSpawns > 0 ? 10 : 0;
            }
        }

        if(this.getHealth()!= parentMob.getHealth())this.setHealth(parentMob.getHealth());

        if (--summonCD <= 0 && this.canShoot(getTarget(), 1.0f)) {
            summonCD = (int) (summonCDAll + summonCDAll * (0.1 + 0.3 * Math.random()));
            float healthPercent = parentMob.getHealthPercentage();
            int count;
            if (healthPercent > 0.5F) {
                count = 1;
            } else {
                float scaleFactor = Mth.clamp((0.5F - healthPercent) / 0.5F, 0.0F, 1.0F);

                count = 1 + (int) (scaleFactor * 4);
            }
            count = Mth.clamp(count, 1, 5);

            if (pendingSpawns == 0) {
                pendingSpawns = count;
                spawnInterval = 10;
            }
        }
    }

    protected boolean canShoot(Entity target, float range) {
        return target!= null && TEUtils.angleBetween(this.getLookAngle(), target.position().subtract(this.position())) < range;
    }

    private void spawnLeech(LivingEntity target) {
        if (level() instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) level();
            BaseWorm warm = new BaseWorm(TEMonsterEntities.LEECH.get(), this.level(), AbstractPrefab.WARM_BUILDER.get()){
                @Override
                protected BaseWormPart createPart(int index) {
                    return new BaseWormPart(this, index);
                }

                @Override
                public boolean hurt(DamageSource source, float amount) {
                    if(source.is(DamageTypes.MOB_ATTACK) && source.getEntity().is(WallOfFleshMouse.this))
                        return false;
                    return super.hurt(source, amount);
                }
                @Override
                public boolean canAttack(LivingEntity entity) {
                    return WallOfFleshMouse.this.parentMob.canAttack(entity);
                }
            };
            warm.setPos(position().add(getForward().normalize().scale(1)));
            warm.setTarget(target);
            serverLevel.addFreshEntity(warm);
        }
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
        return super.hurt(pSource, pAmount) && flag && parentMob.hurt(pSource,pAmount);
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
    public void addSkills() {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> state.setAndContinue(RawAnimation.begin().thenLoop("bait"))));
    }

    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return this.parentMob==null?super.addEffect(effectInstance, entity):this.parentMob.addEffect(effectInstance, entity);
    }

//    public boolean canUsePortal(boolean allowPassengers) {
//        return this.parentMob==null?super.canUsePortal(allowPassengers):this.parentMob.canUsePortal(allowPassengers);
//
//    }

    public boolean isInvulnerableTo(DamageSource source) {
        if(source.is(DamageTypeTags.IS_FIRE)||source.is(DamageTypeTags.IS_DROWNING)){
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean shouldEscape() {
        return false;
    }

    @Override
    public boolean shouldShowBossBar(){
        return false;
    }

    @Override
    public boolean isMainBody(){
        return false;
    }
}