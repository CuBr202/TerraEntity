package org.confluence.terraentity.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.monster.demoneye.DemonEyeWanderGoal;

public class LookForwardWanderFlyGoal extends DemonEyeWanderGoal {

    int distanceToGround = 0;
    int maxHeight;
    public LookForwardWanderFlyGoal(Mob mob,float speed,int maxHeight) {
        super(mob,speed);
        this.maxHeight = maxHeight;
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
        return 2.57f * Mth.cos(radians)-3;
    }
    public void tick(){
        super.tick();

        if(distanceToGround > maxHeight) mob.addDeltaMovement(new Vec3(0,-0.05f,0));
        mob.getLookControl().setLookAt(mob.position().add(mob.getDeltaMovement()));
        mob.setYRot(mob.getYHeadRot());
        distanceToGround--;

    }
}
