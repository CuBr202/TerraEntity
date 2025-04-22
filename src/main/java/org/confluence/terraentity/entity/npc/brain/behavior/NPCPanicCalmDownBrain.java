package org.confluence.terraentity.entity.npc.brain.behavior;

import org.confluence.terraentity.entity.ai.brain.behavior.panic.PanicCalmDownBrain;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;

/**
 * npc的恐慌行为清除器，当npc不能攻击敌怪的时候会始终逃跑
 */
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
