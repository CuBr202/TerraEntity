package org.confluence.terraentity.entity.summon;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.EnumSet;

public class SummonSlime extends AbstractSummonMob {

    public SummonSlime(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5f);
        this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(0);
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(0.5f);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(10.0D);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.0D);

        this.moveControl = new SlimeMoveControl(this);
    }


    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SlimeFloatGoal(this));
        this.goalSelector.addGoal(2, new SlimeAttackGoal(this));
        this.goalSelector.addGoal(5, new SlimeKeepOnJumpingGoal(this));

//        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0, true));

//        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));

    }

    protected int getJumpDelay() {
        return this.random.nextInt(10) + 5;
    }


    static class SlimeMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final SummonSlime slime;
        private boolean isAggressive;

        public SlimeMoveControl(SummonSlime slime) {
            super(slime);
            this.slime = slime;
            this.yRot = 180.0F * slime.getYRot() / 3.1415927F;
        }

        public void setDirection(float yRot, boolean aggressive) {
            this.yRot = yRot;
            this.isAggressive = aggressive;
        }

        public void setWantedMovement(double speed) {
            this.speedModifier = speed;
            this.operation = Operation.MOVE_TO;
        }



        public void tick() {
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();
            if (this.operation != Operation.MOVE_TO) {
                this.mob.setZza(0.0F);
            } else {
                this.operation = Operation.WAIT;
                if (this.mob.onGround()) {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }

                        this.slime.getJumpControl().jump();

                    } else {
                        this.slime.xxa = 0.0F;
                        this.slime.zza = 0.0F;
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }
            }


        }
    }

    static class SlimeKeepOnJumpingGoal extends Goal {
        private final SummonSlime slime;

        public SlimeKeepOnJumpingGoal(SummonSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            if(slime.getOwner() == null) return false;
            float distance = this.slime.distanceTo(this.slime.getOwner());

            return this.slime.getTarget() == null && distance < 16;
        }

        public void tick() {
            MoveControl var2 = this.slime.getMoveControl();

            if (var2 instanceof SummonSlime.SlimeMoveControl slime$slimemovecontrol) {
                Vec3 dir = this.slime.getOwner().position().subtract(this.slime.position()).normalize();
                float yaw = -(float)Math.atan2(dir.x, dir.z) * 57.295776F;
                slime$slimemovecontrol.setDirection( yaw , true);
                float distance = this.slime.distanceTo(this.slime.getOwner());

                if(distance < 3){

                }else if(distance < 6) {
                    slime$slimemovecontrol.setWantedMovement(0.8f);
                    this.slime.lookControl.setLookAt(this.slime.getOwner());
                }
                else
                    slime$slimemovecontrol.setWantedMovement(1.5f);

            }


        }
    }

    static class SlimeAttackGoal extends Goal {
        private final SummonSlime slime;
        private int growTiredTimer;

        public SlimeAttackGoal(SummonSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity == null) {
                return false;
            } else {
                if(slime.getOwner() == null) return false;
                float distance = this.slime.distanceTo(this.slime.getOwner());
                if(distance > 16) return false  ;

                return this.slime.canAttack(livingentity) && this.slime.getMoveControl() instanceof SlimeMoveControl;
            }
        }

        public void start() {
            this.growTiredTimer = reducedTickDelay(300);
            super.start();
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity == null) {
                return false;
            } else {
                if(slime.getOwner() == null) return false;
                float distance = this.slime.distanceTo(this.slime.getOwner());
                if(distance > 16) return false  ;

                return this.slime.canAttack(livingentity) && --this.growTiredTimer > 0;
            }
        }

        public boolean requiresUpdateEveryTick() {
            return false;
        }

        public void tick() {



            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity != null) {
                this.slime.lookAt(livingentity, 10.0F, 10.0F);
            }

            MoveControl var3 = this.slime.getMoveControl();
            if (var3 instanceof SummonSlime.SlimeMoveControl slime$slimemovecontrol) {
                slime$slimemovecontrol.setDirection(this.slime.getYRot(), this.slime.isDealsDamage());
                slime$slimemovecontrol.setWantedMovement(1.5);
            }

        }
    }

    static class SlimeFloatGoal extends Goal {
        private final SummonSlime slime;

        public SlimeFloatGoal(SummonSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
            slime.getNavigation().setCanFloat(true);
        }

        public boolean canUse() {
            return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof SummonSlime.SlimeMoveControl;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.slime.getRandom().nextFloat() < 0.8F) {
                this.slime.getJumpControl().jump();
            }

            MoveControl var2 = this.slime.getMoveControl();
            if (var2 instanceof SummonSlime.SlimeMoveControl slime$slimemovecontrol) {
                slime$slimemovecontrol.setWantedMovement(1.2);
            }

        }
    }


    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
