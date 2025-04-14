package org.confluence.terraentity.entity.npc.brain.behavior;

import net.minecraft.server.level.ServerLevel;
import org.confluence.terraentity.entity.ai.brain.behavior.range.RangeAttackBrain;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;

public class NPCRangeAttackBrain<T extends AbstractTerraNPC> extends RangeAttackBrain<T> {
    public NPCRangeAttackBrain(int prepareTime, float attackRange) {
        super(prepareTime, attackRange);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T owner) {
        return super.checkExtraStartConditions(level, owner) && owner.canPerformerAttack();
    }
}
