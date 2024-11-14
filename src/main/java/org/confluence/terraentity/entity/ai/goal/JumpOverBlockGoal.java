package org.confluence.terraentity.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.pathfinder.Path;

public class JumpOverBlockGoal extends JumpGoal {
    protected final Mob mob;
    protected final float speedModifier;
    public JumpOverBlockGoal(Mob mob) {
        this(mob, 1.0f);
    }
    public JumpOverBlockGoal(Mob mob,float speedModifier) {
        super();
        this.mob = mob;
        this.speedModifier = speedModifier;
    }
    public boolean isInterruptable(){
        return false;
    }
    @Override
    public boolean canUse() {
        Path path = mob.getNavigation().getPath();
        if(path==null) return false;
        boolean flag =path.getNodeCount() ==1 &&  path.getTarget().getY() > mob.getY() + mob.getAttributeBaseValue(Attributes.JUMP_STRENGTH) * 3;

        return flag;
    }
    @Override
    public void start() {
        super.start();
        mob.jumpFromGround();
        mob.addDeltaMovement(mob.getForward().normalize().scale(mob.getSpeed() * speedModifier));
    }
    @Override
    public void stop() {
        super.stop();
    }
}
