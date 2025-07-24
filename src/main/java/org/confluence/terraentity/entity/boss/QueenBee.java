package org.confluence.terraentity.entity.boss;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.config.ServerConfig;
import org.confluence.terraentity.api.entity.Boss;
import org.confluence.terraentity.api.entity.IAngryMob;
import org.confluence.terraentity.entity.ai.MobSkill;
import org.confluence.terraentity.entity.ai.motion.DashComponent;
import org.confluence.terraentity.entity.monster.LittleHornet;
import org.confluence.terraentity.entity.proj.LineProj;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.init.entity.TEProjectileEntities;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

/**
 * 蜂后
 */
public class QueenBee extends AbstractTerraBossBase<QueenBee> implements Boss, IAngryMob {
    private static final int health = 1237;
    private static final int armor = 2;

    DashComponent dashComponent;
    public static final EntityDataAccessor<Boolean> DATA_ANGRY = SynchedEntityData.defineId(QueenBee.class, EntityDataSerializers.BOOLEAN);

    public QueenBee(EntityType<? extends Monster> type, Level level) {
        super(type, level, health, armor);

        collisionProperties.detectInternal = 1;
        this.noPhysics = true;
        this.setAttactDamage(14);
        this.xpReward = 1000;
        if(ServerConfig.BOSS_NO_PHYSICS.get())
            this.noPhysics = true;

        this.dashComponent = new DashComponent(this);
    }
    public QueenBee(Level level) {
        this(TEBossEntities.QUEEN_BEE.get(), level);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ANGRY, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
    }

    MobSkill<QueenBee> first_spawn;
    MobSkill<QueenBee> idle;
    MobSkill<QueenBee> summon_bee;
    MobSkill<QueenBee> summon_proj;

    MobSkill<QueenBee> pre_dash_idle;
    MobSkill<QueenBee> pre_dash;
    MobSkill<QueenBee> dash;

    RawAnimation wing = RawAnimation.begin().thenPlay("wing");

    @Override
    public void addSkills() {
        RawAnimation idle_animation = RawAnimation.begin().thenPlay("idle");
        RawAnimation summon_animation = RawAnimation.begin().thenPlay("summon");
        RawAnimation pre_dash_animation = RawAnimation.begin().thenPlay("pre_dash");
        RawAnimation dash_animation = RawAnimation.begin().thenPlay("dash");


        first_spawn = new MobSkill<QueenBee>(summon_animation, 50, 0)
                .onTick(e->{
                    if(e.tickCount > 50)
                        skills.forceStartIndex(1);
                })
        ;
        idle = new MobSkill<QueenBee>(idle_animation, 25, 0)
                .onTick(e->{
                    lookAt(10);
                    dashComponent.hangOn(getTarget(), 5, 1.5f, getMoveSpeed());
                })
        ;

        summon_bee = new MobSkill<QueenBee>(summon_animation, 60, 10)
                .onTick(e->{
                    lookAt(10);
                    dashComponent.hangOn(getTarget(), 5, 4, getMoveSpeed());
                    if(skills.tick % 10 == 0) {
                        LittleHornet bee = TEMonsterEntities.LITTLE_HORNET.get().create(level());
                        if (bee!=null) {
                            bee.minion_setOwner(e);
                            bee.setPos(e.position());
                            bee.setYRot(e.getYRot());
                            level().addFreshEntity(bee);
                        }
                    }
                })
        ;
        summon_proj = new MobSkill<QueenBee>(summon_animation, 60, 10)
                .onTick(e->{
                    LivingEntity target = e.getTarget();
                    if(target!=null){
                        lookAt(10);
                        if(position().y < target.position().y + 2) addDeltaMovement(new Vec3(0,0.02f,0));
                        if( skills.tick % 10 ==0) {
                            LineProj proj = TEProjectileEntities.BEE_STICK_PROJ.get().create(level());
                            if (proj!=null) {
                                proj.setDamage((float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                                proj.setOwner(e);
                                proj.setPos(e.position());
                                proj.addEffect(new MobEffectInstance(MobEffects.POISON, 100, isAngry() ? 1 : 0));
                                Vec3 dir = target.getEyePosition().subtract(e.position());
                                proj.shoot(dir.x, dir.y, dir.z, 1, 5f);
                                level().addFreshEntity(proj);
                            }
                        }
                    }
                    // 低血量减少弹幕次数
                    if(getHealthPercentage() < 0.3f && skills.tick > 50){
                        skills.forceEnd();
                    }
                })
        ;

        pre_dash_idle = new MobSkill<QueenBee>(idle_animation, 20, 0)
                .onTick(e->{
                    if(target== null) return;
                    if(
                            distanceToSqr(target) > 10 * 10 ||
                            Math.abs(target.getY() - e.getY()) > 2 ||
                            Math.abs(this.getXRot()) > 10
                    ){
                        if(difficult && random.nextBoolean())
                            skills.tick--;
                    }

                    dashComponent.hangOn(getTarget(), 5, 0, getMoveSpeed() * 1.2f);
                    lookAt(10);
                })
        ;
        pre_dash = new MobSkill<QueenBee>(pre_dash_animation, 15, 0)
                .onTick(e->{
                    setDeltaMovement(0,0,0);
                    if(target!=null) {
                        // 预判冲
                        dashComponent.setPredictDirection(target);
                        dashComponent.lookAtDirection();
                    }
                })
                .onOver(e->{
                    if(getTarget()!=null) {

                    }
                })

        ;
        dash = new MobSkill<QueenBee>(dash_animation, 50, 0)

                .onTick(e->{
                    if(getTarget() == null) return;
                    dashComponent.uniformMove(getMoveSpeed() * 2f * (isAngry() && difficult? 1.5f : 1f));
                    if(distanceToSqr(target) > 15 * 15) skills.forceEnd();

                })
        ;

        addSkill(first_spawn);
        addSkill(idle);
        addSkill(summon_bee);
        addSkill(summon_proj);

        addSkill(pre_dash_idle);
        addSkill(pre_dash);
        addSkill(dash);

        addSkill(pre_dash_idle);
        addSkill(pre_dash);
        addSkill(dash);

        addSkill(pre_dash_idle); // 10
        addSkill(pre_dash);
        addSkill(dash);

        addSkill(pre_dash_idle);
        addSkill(pre_dash);
        addSkill(dash);
    }

/* anger */

    @Override
    public boolean isAngry() {
        return this.getEntityData().get(DATA_ANGRY);
    }

    @Override
    public boolean shouldAnger() {
        return !level().getBiome(blockPosition()).is(Biomes.JUNGLE);
    }

    @Override
    public void setAngry(boolean angry) {
        this.getEntityData().set(DATA_ANGRY, angry);
    }

    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide){
            if(getTarget() == null){
                skills.forceStartIndex(1);
            }
            setAngry(shouldAnger());
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return super.canAttack(target) && !(target instanceof LittleHornet);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        controllers.add(fsmAnimationController(),
                new AnimationController<GeoAnimatable>(this, "wing",10, state->{
                    state.setAnimation(wing);
                    return PlayState.CONTINUE;
                })
        );
    }
    @Override
    protected SoundEvent getDeathSound() {
        return TESounds.ROUTINE_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return TESounds.ROUTINE_HURT.get();
    }

    @Override
    public boolean isNoGravity(){ return true; }

    protected BossEvent.BossBarColor getBossBarColor(){
        return BossEvent.BossBarColor.YELLOW;
    };

    protected boolean shouldOverPlayer(){
        return true;
    }
}