package org.confluence.terraentity.entity.ai.goal.summon;

import net.minecraft.world.entity.Mob;
import org.confluence.terraentity.entity.summon.ISummonMob;

public class SummonFlyFlowOwnerGoal<T extends Mob & ISummonMob<?>> extends SummonFollowOwnerGoal<T>{

    public SummonFlyFlowOwnerGoal(T tamable, double speedModifier, float startDistance, float stopDistance) {
        super(tamable, speedModifier, startDistance, stopDistance);
    }

    public int getInterval() {
        return 0;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void createPath(){
        this.tamable.getMoveControl().setWantedPosition(owner.position().x, owner.position().y + 2, owner.position().z, 10);
        super.createPath();
    }
}
