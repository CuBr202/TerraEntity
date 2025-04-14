package org.confluence.terraentity.entity.npc.brain.behavior;

import net.minecraft.server.level.ServerLevel;
import org.confluence.terraentity.entity.ai.brain.behavior.range.AttackTriggerBrain;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;

public class NPCAttackTriggerBrain<T extends AbstractTerraNPC> extends AttackTriggerBrain<T> {

    public NPCAttackTriggerBrain() {
        super(0);
    }

    protected boolean checkExtraStartConditions(ServerLevel level, T owner) {
        return owner.canPerformerAttack();
    }


    protected float getDetectDistanceSqr(T living) {
        float r = living.getAttackRange() + 5;// +5额外侦测距离
        return r * r;
    }
}
