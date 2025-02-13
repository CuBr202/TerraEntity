package org.confluence.terraentity.entity.ai.goal.summon;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.PathType;
import org.confluence.terraentity.entity.summon.ISummonMob;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SummonFollowOwnerGoal<T extends Mob & ISummonMob<?>> extends Goal {
    private final T tamable;
    @Nullable
    private LivingEntity owner;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;

    public SummonFollowOwnerGoal(T tamable, double speedModifier, float startDistance, float stopDistance) {
        this.tamable = tamable;
        this.speedModifier = speedModifier;
        this.navigation = tamable.getNavigation();
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!(tamable.getNavigation() instanceof GroundPathNavigation) && !(tamable.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    public boolean canUse() {
        LivingEntity livingentity = this.tamable.summon_getOwner();
        if (livingentity == null) {
            return false;
        } else if (this.tamable.summon_unableToMoveToOwner()) {
            return false;
        } else if (this.tamable.distanceToSqr(livingentity) < (double)(this.startDistance * this.startDistance)) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else if (owner!=null) {
            return !this.tamable.summon_unableToMoveToOwner() && !(this.tamable.distanceToSqr(this.owner) <= (double) (this.stopDistance * this.stopDistance));
        }
        return false;
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.tamable.getPathfindingMalus(PathType.WATER);
        this.tamable.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.tamable.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
    }

    public void tick() {
        boolean flag = this.tamable.summon_shouldTryTeleportToOwner();
        if (!flag && owner!=null) {
            this.tamable.getLookControl().setLookAt(this.owner, 10.0F, (float)this.tamable.getMaxHeadXRot());
        }

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (flag) {
                this.tamable.summon_tryToTeleportToOwner();
            } else {
                this.navigation.moveTo(this.owner, this.speedModifier);
            }
        }

    }

}
