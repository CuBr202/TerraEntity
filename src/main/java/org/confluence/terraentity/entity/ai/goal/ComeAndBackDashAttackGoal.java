package org.confluence.terraentity.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.utils.TEUtils;

public class ComeAndBackDashAttackGoal extends Goal {
    private boolean randomDirection;
    private final Mob warm;
    private final float _distanceToTurn;
    int soundTick = 0;
    int soundInternal = 50;

    public ComeAndBackDashAttackGoal(Mob warm, float distanceToTurn) {
        this.warm = warm;
        this._distanceToTurn = distanceToTurn * distanceToTurn;
    }

    @Override
    public boolean canUse() {
        return warm.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return this.canUse() && warm.getTarget()!=null&&warm.getTarget().isAlive();
    }

    public void tick() {
        LivingEntity target = warm.getTarget();
        if (target == null) return;
        double distance = warm.distanceToSqr(target);
        double angle = TEUtils.angleBetween(warm.getLookAngle(), target.position().subtract(warm.position()));

        if (distance > _distanceToTurn) {
            // 转向
            warm.getLookControl().setLookAt(target);
            warm.lookAt(target, 5, 30);
            if (angle > Math.PI / 6.0D) {
                warm.addDeltaMovement(warm.getLookAngle().scale(0.04f).add(0, 0.01f * (randomDirection ? 1 : -1), 0));
                return;
            }
        }
        // 攻击
        if (angle < Math.PI / 2){
            if(distance < 400){

                soundInternal = (int) (Math.pow( distance, 0.3f) * 0.5f);
                if(++soundTick >= soundInternal){
                    warm.playSound(TESounds.DIG_SOUND.get(), 2f, 1f);
                    soundTick = 0;
                }
            }
            warm.getLookControl().setLookAt(target);
            warm.lookAt(target, 2f, 30f);
        }

        warm.addDeltaMovement(warm.getLookAngle().scale(0.1f));

        randomDirection = !randomDirection;

    }
}
