package org.confluence.terraentity.entity.monster;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.goal.AccelerateOnSeeingGoal;
import org.confluence.terraentity.entity.ai.goal.ComeAndBackDashAttackGoal;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;

import java.util.EnumSet;

public class Wyvern<S extends BaseWormPart> extends BaseWorm<S> {

    public Wyvern(EntityType<? extends Wyvern> type, Level level, AttributeBuilder builder) {
        super(type, level, builder);
        this.segInternal = 1;
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(1, new WyvernAttackGoal(this, 16));
        this.goalSelector.addGoal(3, new WyvernRandomStrollGoal(this, 20));

        this.targetSelector.addGoal(1,new AccelerateOnSeeingGoal(this,0.25f));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class,false, LivingEntity::canBeSeenAsEnemy));
    }

    static class WyvernRandomStrollGoal extends Goal {
        Wyvern<? extends BaseWormPart> worm;
        Vec3 center;
        float radius;
        float angle;

        public WyvernRandomStrollGoal(Wyvern<? extends BaseWormPart> worm, float radius){
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.worm = worm;
            this.radius = radius;
        }

        @Override
        public boolean canUse() {
            return worm.getTarget() == null;
        }

        @Override
        public void start(){
            this.center = worm.position();

        }
        @Override
        public void tick(){
            this.angle += 0.015f;

            Vec3 dynamicPos = calPosition(radius, angle, Math.sin(worm.tickCount * 0.1f) * 4);
                    Vec3 dir = dynamicPos.subtract(worm.getEyePosition()).normalize();

            Vec3 lookPos = calPosition(radius, angle + 1f, 0);
            worm.lookControl.setLookAt(lookPos);
            worm.lookAt(EntityAnchorArgument.Anchor.EYES, lookPos);

            worm.setDeltaMovement(dir.scale(0.8f));
        }

        private Vec3 calPosition(float radius, float angle, double offsetY){
            return this.center.add(
                    radius * Math.cos(angle),
                    offsetY,
                    radius * Math.sin(angle));
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    static class WyvernAttackGoal extends ComeAndBackDashAttackGoal {

        int triggerDash = 0;
        public WyvernAttackGoal(Mob warm, float distanceToTurn) {
            super(warm, distanceToTurn);
        }

        @Override
        public void tick(){
            boolean turn = this.turning;
            if(this.turning){
                this.triggerDash--;
            }
            super.tick();
            if(!this.turning && turn){
                this.triggerDash = 0;
            }
        }
        @Override
        protected Vec3 getBackTurnMovement(double distance) {
            return worm.getLookAngle().normalize().scale(0.4f).add(0, 0.4f, 0);
        }

        @Override
        protected Vec3 getAttackMovement(LivingEntity target, double distance) {
            if(this.worm.distanceTo(target) < 5f && target.level().getBlockState(target.blockPosition().below()).isAir()){
                this.triggerDash = 15;
            }
            if(this.triggerDash > 0){
                return super.getAttackMovement(target, distance).scale(2.5f).add(0, 0.1, 0);
            }
            return super.getAttackMovement(target, distance).scale(2f);
        }

        @Override
        protected void attackLookAt(LivingEntity target){
            if(this.triggerDash > 0){
                worm.getLookControl().setLookAt(worm.getEyePosition().add(worm.getForward().normalize().scale(5)));
                return;
            }
            worm.getLookControl().setLookAt(target);
            worm.lookAt(target, 2f, 30f);
        }
    }

    @Override
    protected S createPart(int index) {
        return (S) createSimplePart(this, index);
    }

    public boolean hasLineOfSight(Entity entity) {
        return true;
    }
}
