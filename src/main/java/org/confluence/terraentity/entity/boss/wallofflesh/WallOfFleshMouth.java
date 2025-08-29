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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.api.entity.Boss;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import org.confluence.terraentity.entity.monster.BaseWorm;
import org.confluence.terraentity.entity.monster.BaseWormPart;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import javax.annotation.Nullable;

@SuppressWarnings("all")
public class WallOfFleshMouth extends AbstractTerraBossBase implements Boss.BossPart {

    public WallOfFlesh parentMob;

    private int pendingSpawns = 0;
    private int spawnInterval = 0;

    private static final int BASE_SUMMON_CD = 100;
    private int summonCDAll = BASE_SUMMON_CD;
    private int summonCD = summonCDAll;


    public WallOfFleshMouth(EntityType<WallOfFleshMouth> entityType, Level level) {
        super(entityType, level);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.playSound(TESounds.ROAR.get());
        this.noPhysics = true;
        collisionProperties.attackInternal = 1;
        collisionProperties.detectInternal = 1;
    }

    public WallOfFleshMouth(Level level) {
        this(TEBossEntities.WALL_OF_FLESH_MOUTH.get(), level);
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        if(this.parentMob == null)
            return super.canAttack(entity);
        else return this.parentMob.canAttack(entity);
    }

    @Override
    public float getYRot() {
        if(this.parentMob!=null)return this.parentMob.getYRot();
        return super.getYRot();
    }

    @Override
    public float getXRot() {
        if(this.parentMob!=null)return this.parentMob.getXRot();
        return super.getXRot();
    }

    @Override
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
            summonCD = summonCDAll + random.nextInt(6) * 20;
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
                    if(source.is(DamageTypes.MOB_ATTACK) && source.getEntity().is(WallOfFleshMouth.this))
                        return false;
                    return super.hurt(source, amount);
                }
                @Override
                public boolean canAttack(LivingEntity entity) {
                    return WallOfFleshMouth.this.canAttack(entity);
                }
            };
            warm.setPos(position().add(getForward().normalize().scale(1)));
            warm.setTarget(target);
            serverLevel.addFreshEntity(warm);
        }
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
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
    public void addSkills() {}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> state.setAndContinue(RawAnimation.begin().thenLoop("bait"))));
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return this.parentMob==null?super.addEffect(effectInstance, entity):this.parentMob.addEffect(effectInstance, entity);
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return this.parentMob==null?super.canUsePortal(allowPassengers):this.parentMob.canUsePortal(allowPassengers);
    }

    @Override
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
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return TESounds.THE_HUNGRY_HURT.get();
    }
}