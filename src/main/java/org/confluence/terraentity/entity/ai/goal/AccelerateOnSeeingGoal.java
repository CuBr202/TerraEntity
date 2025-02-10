package org.confluence.terraentity.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

public class AccelerateOnSeeingGoal extends Goal {
    protected final Mob mob;
    protected float speedModifier;
    private AttributeModifier modifier;
    public AccelerateOnSeeingGoal(Mob mob,float speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        modifier = new AttributeModifier("accelerate_on_seeing_goal",speedModifier,AttributeModifier.Operation.ADDITION);

    }

    @Override
    public boolean canUse() {
        boolean canUse = mob.getTarget()!= null && mob.getTarget().isAlive();
        if (canUse) {
            if (!mob.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(modifier)) {
                mob.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(modifier);
                mob.setSprinting(true);
            }
        }else if(mob.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(modifier)){
            mob.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(modifier);
            mob.setSprinting(false);
        }
        return false;
    }
}
