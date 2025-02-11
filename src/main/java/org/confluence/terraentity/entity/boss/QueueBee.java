package org.confluence.terraentity.entity.boss;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.Boss;
import org.confluence.terraentity.entity.ai.MobSkill;
import org.confluence.terraentity.entity.proj.LineProj;
import org.confluence.terraentity.init.TEEntities;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class QueueBee extends AbstractTerraBossBase<QueueBee> implements Boss {
    private static final int health = 1237;
    private static final int armor = 2;

    public boolean isAngry = false;
    Vec3 targetPos = null;
    Vec3 targetDir = null;
    public static final EntityDataAccessor<Boolean> DATA_ANGRY = SynchedEntityData.defineId(QueueBee.class, EntityDataSerializers.BOOLEAN);

    public QueueBee(EntityType<? extends Monster> type, Level level) {
        super(type, level, health, armor);

        this._detectInternal = 2;
        this.noPhysics = true;
        this.setAttactDamage(1);
        this.xpReward = 1000;

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ANGRY, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == DATA_ANGRY && level().isClientSide) {
            this.isAngry = this.getEntityData().get(DATA_ANGRY);
        }
    }

    MobSkill<QueueBee> first_spawn;
    MobSkill<QueueBee> idle;
    MobSkill<QueueBee> summon_bee;
    MobSkill<QueueBee> summon_proj;

    MobSkill<QueueBee> pre_dash_idle;
    MobSkill<QueueBee> pre_dash;
    MobSkill<QueueBee> dash;

    RawAnimation wing = RawAnimation.begin().thenPlay("wing");


    void hangOn(LivingEntity target,float distance, float height, float speed){
        if(target!=null){
            Vec3 targetPos = position().subtract(target.position()).multiply(1,0,1).normalize().scale(distance).add(0,height,0).add(target.position());
            Vec3 targetDir = targetPos.subtract(position());
            addDeltaMovement(targetDir.scale(speed * 0.02f));
        }
    }

    @Override
    public void addSkills() {
        RawAnimation idle_animation = RawAnimation.begin().thenPlay("idle");
        RawAnimation summon_animation = RawAnimation.begin().thenPlay("summon");
        RawAnimation pre_dash_animation = RawAnimation.begin().thenPlay("pre_dash");
        RawAnimation dash_animation = RawAnimation.begin().thenPlay("dash");


        first_spawn = new MobSkill<QueueBee>(summon_animation, 50, 0)
                .onInit(e->{
                    if(e.tickCount > 10)
                        skills.forceStartIndex(1);
                })
        ;
        idle = new MobSkill<QueueBee>(idle_animation, 25, 0)
                .onTick(e->{
                    LookAt(10);
                    hangOn(getTarget(), 5, 4, 1);
                })
        ;

        summon_bee = new MobSkill<QueueBee>(summon_animation, 10, 10)
                .onTick(e->{
                    LookAt(10);

                    hangOn(getTarget(), 5, 4, 1);
                })
        ;
        summon_proj = new MobSkill<QueueBee>(summon_animation, 100, 10)
                .onTick(e->{
                    LivingEntity target = e.getTarget();
                    if(target!=null){
                        LookAt(10);
                        if(position().y < target.position().y + 2) addDeltaMovement(new Vec3(0,0.02f,0));
                        if( skills.tick % 10 ==0) {
                            LineProj proj = TEEntities.BEE_STICK_PROJ.get().create(level());
                            proj.setOwner(e);
                            proj.setPos(e.position());
                            Vec3 dir = target.getEyePosition().subtract(e.position());
                            proj.shoot(dir.x, dir.y, dir.z, 1, 5f);
                            level().addFreshEntity(proj);
                        }
                    }
                })
        ;

        pre_dash_idle = new MobSkill<QueueBee>(idle_animation, 20, 0)
                .onTick(e->{
                    if(target!=null && distanceToSqr(target) > 10*10) skills.tick = 15;
                    hangOn(getTarget(), 5, 1.5f, 1);
                    LookAt(10);
                })
        ;
        pre_dash = new MobSkill<QueueBee>(pre_dash_animation, 15, 0)
                .onTick(e->{
                    setDeltaMovement(0,0,0);
                    if(target!=null) {
                        LookAt(10);
                    }
                })
                .onOver(e->{
                    if(getTarget()!=null) {
                        // 预判冲
                        targetDir = getTarget().position().add(0, 1, 0).add(target.getKnownMovement().scale(10)).subtract(position());
                        lookAt(EntityAnchorArgument.Anchor.EYES, targetDir.add(position()));
                    }
                })

        ;
        dash = new MobSkill<QueueBee>(dash_animation, 50, 0)
                .onTick(e->{
                    setDeltaMovement(targetDir.normalize().scale(1));
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

        addSkill(pre_dash_idle);
        addSkill(pre_dash);
        addSkill(dash);

        addSkill(pre_dash_idle);
        addSkill(pre_dash);
        addSkill(dash);
    }

    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide){
            if(getTarget() == null){
                skills.forceStartIndex(1);
            }
        }
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
    public boolean isNoGravity(){ return true; }

}