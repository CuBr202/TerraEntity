package org.confluence.terraentity.entity.rideable;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.IFlyRideableMob;
import software.bernie.geckolib.animation.AnimatableManager;

public class RideableBee extends AbstractRideableEntity implements IFlyRideableMob {

    int _flyTick = 100;
    int flyTick = 100;

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
            this.isJumping = ((LocalPlayer)player).input.jumping;
        }

        if (this.onGround()) {
            return new Vec3(f*0.08f,0,f1*0.15f);
        } else {
            return new Vec3(f*0.5f, 0.0, f1);
        }
    }

    @Override
    public void onPlayerJump(int jumpPower) {
    }

    @Override
    public float calJumpingScale(float jumpTick) {
        return (float) (flyTick) / _flyTick;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(DefaultAnimations.genericIdleController(this));
    }
}
