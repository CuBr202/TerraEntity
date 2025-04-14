package org.confluence.terraentity.entity.npc.brain;

import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.brain.behavior.NPCRangeAttackBrain;

/**
 * 爆破专家的AI
 */
public class ArmDealerNPCAi extends NPCAi {

    public ArmDealerNPCAi(AbstractTerraNPC npc) {
        super(npc);
    }

    protected NPCRangeAttackBrain<? super AbstractTerraNPC> createRangeAttackBrain() {
        return new NPCRangeAttackBrain<>(10, npc.getAttackRange());
    }
}
