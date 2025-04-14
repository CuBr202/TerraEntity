package org.confluence.terraentity.entity.npc.brain.behavior;

import org.confluence.terraentity.entity.ai.brain.behavior.panic.PanicCalmDownBrain;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;

public class NPCPanicCalmDownBrain<T extends AbstractTerraNPC> extends PanicCalmDownBrain<T> {

    public NPCPanicCalmDownBrain() {
        super();
    }

    public NPCPanicCalmDownBrain(float chance) {
        super(chance);
    }

    public float getCalmDownChance(T living){
        return living.canPerformerAttack()? super.getCalmDownChance(living) : 0f;
    }

}
