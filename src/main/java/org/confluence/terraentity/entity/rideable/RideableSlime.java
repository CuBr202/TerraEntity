package org.confluence.terraentity.entity.rideable;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;

import static software.bernie.geckolib.constant.DefaultAnimations.*;

public class RideableSlime extends AbstractRideableEntity {

    public RideableSlime(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);

        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5f);
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(2.0f);
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        if (this.isJumping) {
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

    }

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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Fly/Idle/Move", 0, state ->
                state.setAndContinue(this.isJumping() ? FLY : IDLE)
        ));
    }
}