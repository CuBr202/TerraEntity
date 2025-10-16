package org.confluence.terraentity.entity.animal;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Map;

public class WallOfFairy extends Fairy {

    final BlockPos guidePos;

    public WallOfFairy(EntityType<? extends WallOfFairy> entityType, Level level, Map<Integer, ResourceLocation> texturesMap, BlockPos guidePos) {
        super(entityType, level, texturesMap);
        this.guidePos = guidePos;
        this.noPhysics = true;
    }

    @Override
    protected void addGoals() {
        this.goalSelector.addGoal(0, new GuidePlayerGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0, 3.0F, 7.0F));
    }

    static class GuidePlayerGoal extends Goal {
        BlockPos guidePos;
        Player target;
        WallOfFairy mob;
        boolean isFollowing;
        float angle = 0;
        float radius = 3;
        float height = 3;

        public GuidePlayerGoal(WallOfFairy mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.mob = mob;

        }

        @Override
        public boolean canUse() {
            if(this.target == null){
                this.target = this.mob.level().getNearestPlayer(this.mob, 10.0D);
                if(this.target == null){
                    return false;
                }
                return true;
            }
            return true;
        }
        @Override
        public boolean canContinueToUse() {
            if(this.target == null){
                return false;
            }
            if(!target.isAlive()){
                this.target = null;
                return false;
            }
            return this.canUse();
        }

        @Override
        public void tick() {
            if(this.target == null){
                return;
            }
            this.angle += this.mob.getRandom().nextFloat() * 0.05f + 0.05f;
            Vec3 targetPos = this.target.position();
            Vec3 mobPos = this.mob.position();

            if(!this.isFollowing){
                // 开始跟随玩家
                this.moveToTarget(targetPos, mobPos);
                return;
            }
            if(this.mob.distanceTo(this.target) > 60){
                // 玩家离开了，停止跟随
                this.isFollowing = false;
                this.target = null;
                return;
            }
            if(this.guidePos == null) {
                this.guidePos = this.mob.guidePos;
            }
            if(this.guidePos == null){
                this.moveToTarget(targetPos, mobPos);
                return;
            }
            Vec3 guidePos = Vec3.atCenterOf(this.guidePos);
            Vec3 delta = guidePos.subtract(targetPos);
            double dist = delta.length();
            if(dist > 10){
                guidePos = targetPos.add(delta.normalize().scale(10));
                if(dist > 60){
                    // 距离过远，重新寻找
                    this.guidePos = null;
                }
            }
            this.moveToTarget(guidePos, mobPos);

        }

        protected void moveToTarget(Vec3 targetPos, Vec3 mobPos){
            double distance = mobPos.distanceTo(targetPos);
            Vec3 pos = targetPos.add(new Vec3(Math.sin(this.angle) * this.radius, 0, Math.cos(this.angle) * this.radius));
            this.mob.getNavigation().moveTo(pos.x, pos.y + height, pos.z, 0.5 + distance * 0.3);

            if(distance < 3f){
                this.isFollowing = true;
            }
        }
    }

}
