package org.confluence.terraentity.entity.rideable;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.IFlyRideableMob;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

public class RideableBee extends AbstractRideableEntity implements IFlyRideableMob {

    int _flyTick = 100;
    int flyTick = 100;
    protected boolean moving;
    int movingCounter = 0;
    int stopCounter = 0;

    public RideableBee(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        this.getAttribute(Attributes.GRAVITY).setBaseValue(0.03f);
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        Vec2 vec2 = this.getRiddenRotation(player);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if (this.isControlledByLocalInstance()) {
            Vec3 speed = getDeltaMovement();
            if(this.isJumping){
                double vy;
                if(--flyTick > 0){
                    vy = Math.min(speed.y + 0.035f, 0.2f);
                }else{
                    vy = Math.min(speed.y + 0.02f, 0.2f);
                }
                this.setDeltaMovement(speed.x, vy, speed.z);
            }
            if(onGround()){
                flyTick = Math.min(_flyTick, flyTick + 5);
            }
        }
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float f = player.xxa * 0.5F;
        float f1 = Math.max(player.zza, -0.1f);
        if(level().isClientSide) {
            LocalPlayer lp = (LocalPlayer) player;
            this.isJumping = lp.input.jumping;
            moving = lp.input.left || lp.input.right || lp.input.up || lp.input.down;
            if(moving && !isJumping){
                movingCounter++;
                stopCounter = 0;
            }else{
                movingCounter = 0;
                stopCounter++;
            }
        }

        if (this.onGround()) {
            return new Vec3(f*0.08f,0,f1*0.15f);
        } else {
            return new Vec3(f*0.5f, 0.0, f1);
        }
    }

    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {

        float offsetY = this.moving && !isJumping?
                Mth.lerp(Math.min((movingCounter + partialTick) / 10f, 1f), 0.4f, 0.1f)
                : Mth.lerp(Math.min((stopCounter + partialTick) / 10f, 1f), 0.1f, 0.4f);
        return super.getPassengerAttachmentPoint(entity, dimensions, partialTick).add(0,offsetY,0);
    }

    @Override
    public void onPlayerJump(int jumpPower) {
    }

    @Override
    public float calJumpingScale(float jumpTick) {
        return (float) (flyTick) / _flyTick;
    }

    RawAnimation wing = RawAnimation.begin().thenLoop("wing");
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "Wing", 10, state -> {
                if (isJumping) {
                    return state.setAndContinue(wing);
                }
                state.resetCurrentAnimation();
                return PlayState.STOP;
            }),
            new AnimationController<>(this, "Fly/Idle/Move", 10, state -> {
                if (moving) {
                    if (isJumping) {
                        return state.setAndContinue(DefaultAnimations.FLY);
                    }
                    return state.setAndContinue(DefaultAnimations.WALK);
                }
                return state.setAndContinue(DefaultAnimations.IDLE);
            })
        );
    }
}
