package org.confluence.terraentity.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import org.confluence.terraentity.entity.monster.demoneye.DemonEyeWanderGoal;

public class LookForwardWanderFlyGoal extends DemonEyeWanderGoal {


    float offsetY;
    public LookForwardWanderFlyGoal(Mob mob,float speed, float offsetY) {
        super(mob,speed);
        this.offsetY = offsetY;

    }
    public boolean canUse() {
        return mob.getTarget() == null;
    }
    public void start() {
        super.start();

    }
    public float getOffsetY(){
        float period = 10f;
        float radians = Mth.TWO_PI * (locateCount % period) / period;
        return 2.57f * Mth.cos(radians)-3 + this.offsetY;
    }
    public void tick(){
        super.tick();

        mob.getLookControl().setLookAt(mob.position().add(mob.getDeltaMovement().scale(20).add(0,1,0)));
        mob.setYRot(mob.getYHeadRot());


    }
}
