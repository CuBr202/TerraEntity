package org.confluence.terraentity.entity.ai.goal;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.motion.curve.Bezier3Curse;
import org.confluence.terraentity.entity.ai.motion.curve.Curve;

public class RandomWanderGoal extends Goal {
    private Vec3 randomTarget;
    private int tickToChangeTarget;
    private final int _tickToChangeTarget;
    private final Mob warm;

    Curve curve;

    public RandomWanderGoal(Mob warm, int tickToChangeTarget) {
        this.warm = warm;
        this._tickToChangeTarget = tickToChangeTarget;
        this.tickToChangeTarget = _tickToChangeTarget;
    }

    protected Vec3 findWanderTarget() {
        randomTarget = warm.position().add(Math.random() * 20 - 10, Math.random() * 20 - 8, Math.random() * 20 - 10)
                .add(warm.getLookAngle().normalize().scale(10)); // 防止寻路到背后导致突然转向

        BlockPos pos = new BlockPos((int) randomTarget.x, (int) randomTarget.y, (int) randomTarget.z);
        int delta = 0;
        while (warm.level().getBlockState(pos).isAir() && pos.getY() > -65) {
            pos = pos.below();
            delta++;
        }
        float f0 = (float) (randomTarget.y - delta) + warm.getRandom().nextIntBetweenInclusive(-3,5); // 控制高度起伏
        float f1 = f0 < -65 ? -130 - f0 : f0;
        randomTarget = new Vec3(randomTarget.x, f1, randomTarget.y);

        return randomTarget;
    }

    @Override
    public boolean canUse() {
        return warm.getTarget() == null && warm.tickCount > 5;
    }


    @Override
    public void start() {
        findWanderTarget();
        tickToChangeTarget = _tickToChangeTarget;
        Vec3 dir = warm.getLookAngle().normalize();
        Vec3 mid = warm.position().add(dir.scale(warm.getRandom().nextIntBetweenInclusive(5,10))); // 切线

        // 调整控制点落差
        double dy =  mid.y - randomTarget.y;
        mid = mid.add(0, -dy + (dy > 0? 1 : -1) * warm.getRandom().nextIntBetweenInclusive(5,8), 0);

        // 调整开口方向
        if(warm.position().y > mid.y){
            // 开口方向向上
            if(warm.position().y > randomTarget.y){
                randomTarget = randomTarget.add(0, warm.position().y  -  randomTarget.y, 0) ;
                mid = mid.add(0, warm.position().y - 2  -  mid.y, 0);
            }
        }else{
            // 开口方向向下
            if(warm.position().y < randomTarget.y){
                randomTarget = randomTarget.add(0, warm.position().y - 1  -  randomTarget.y, 0) ;
                mid = mid.add(0, warm.getRandom().nextIntBetweenInclusive(5,8), 0);
            }
        }

        this.curve = new Bezier3Curse(warm.position(),
                mid,
                randomTarget
        );
    }

    public boolean canContinueToUse() {
        return warm.distanceToSqr(randomTarget) > 3f && tickToChangeTarget > 0;
    }


    public void stop() {
        tickToChangeTarget = _tickToChangeTarget;
    }

    public void tick() {
        --tickToChangeTarget;
        if(curve != null){
            Vec3 target = curve.cal( (_tickToChangeTarget - tickToChangeTarget) * 1.0f / _tickToChangeTarget);
            Vec3 lookPos = target.subtract(warm.position()).scale(20).add(warm.position());
            warm.lookAt(EntityAnchorArgument.Anchor.EYES, lookPos);
            warm.getLookControl().setLookAt(lookPos.x, lookPos.y, lookPos.z, 10, 10);
            warm.setDeltaMovement(target.subtract(warm.position()).scale(0.9f)); // * 0.9防止头抽搐
        }
    }
}
