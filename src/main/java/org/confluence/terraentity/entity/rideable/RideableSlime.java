package org.confluence.terraentity.entity.rideable;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.joml.Vector3f;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;


public class RideableSlime extends AbstractRideableEntity {

    Vector3f initSpeed;
    private static final EntityDataAccessor<Vector3f> DATA_INIT_SPEED = SynchedEntityData.defineId(RideableSlime.class, EntityDataSerializers.VECTOR3);;

    public RideableSlime(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);

        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5f);
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(2.0f);
        this.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.12f);

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_INIT_SPEED, new Vector3f(0,0,0));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(key == DATA_INIT_SPEED && level().isClientSide){
            this.initSpeed = this.entityData.get(DATA_INIT_SPEED);
        }
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        if (this.isJumping && !onGround()) {
            boolean trigger = false;
            for (int i = 0; i < 4; i++) {
                float offsetX = (i == 1 || i == 2) ? 1 : 0;
                float offsetZ = (i == 2 || i == 3) ? 1 : 0;
                if (getHitResult(offsetX, offsetZ)) {
                    trigger = true;
                    break;
                }
            }
            if (trigger) {
                this.setDeltaMovement(getDeltaMovement().x, getJumpPower(), getDeltaMovement().z);
            }
        }
        super.tickRidden(player, travelVector);
    }

    private boolean getHitResult(float offsetX, float offsetZ) {
        HitResult hitResult = ProjectileUtil.getEntityHitResult(level(), this, position(), position().subtract(offsetX, 1f, offsetZ), getBoundingBox().inflate(2), e -> e.isAttackable());
        if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
            return true;
        }
        return false;
    }

    @Override
    public void tick() {

        super.tick();

        if(this.isInWater() && !level().isClientSide){
            float power = 0.1f;
            if (this.getRandom().nextFloat() < 0.8F) {
                this.addDeltaMovement(new Vec3(0,power + 0.1f,0));
            }else{
                this.addDeltaMovement(new Vec3(0,power,0));
            }
        }
    }

    /**
     * 处理运动
     */
    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float f = player.xxa * 0.5F;
        float f1 = player.zza;

        if (this.onGround()) {
            return new Vec3(f*0.2f,0,f1*0.2f);
        } else {
            return new Vec3(f, 0.0, f1);
        }
    }

    protected void tickRiddenLocal(Player player, Vec3 travelVector){
        super.tickRiddenLocal(player, travelVector);
        if(tickCount < 50 && initSpeed!= null){
            initSpeed.y = Math.max(0, Math.min(initSpeed.y, 0.25f) - 0.02f);
            this.setDeltaMovement(getDeltaMovement().add(0, initSpeed.y, 0));
        }
    }

        @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity target = source.getEntity();

        if(target != null){
            if(source.is(DamageTypes.MOB_ATTACK)){
                if(!onGround()){
                    if(target.getY() < this.getY() - 0.5f){
                        return false;
                    }
                }
            }
        }
        return super.hurt(source, amount);
    }
    @Override
    public double getPassengersRidingOffset() {

        if(jumpCount == 0){
            double a = (Math.cos(movingCounter * 0.6f) - 1) * 0.3f;
            double f = Math.sin(a) * 0.6F;
            return super.getPassengersRidingOffset() + f + 0.2f;
        }else {
            double a = Math.min((jumpCount) * 0.5F, Math.PI);
            double f = Math.sin(a) * 0.5F;
            return super.getPassengersRidingOffset() + f + 0.2f;
        }
    }

    RawAnimation jump = RawAnimation.begin().thenPlay("jump");
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Move/Jump", 0, state ->{

            if(isJumping() && jumpCount < 20) {
                return state.setAndContinue(jump);
            }

            if(this.onGround() && isMoving){
                return state.setAndContinue(DefaultAnimations.WALK);
            }
            state.resetCurrentAnimation();
            return PlayState.STOP;
        }
        ));
    }

    public void onInit(Player player){
        this.entityData.set(DATA_INIT_SPEED, new Vector3f(player.xxa, (float) player.getDeltaMovement().y, player.zza));
    }
}