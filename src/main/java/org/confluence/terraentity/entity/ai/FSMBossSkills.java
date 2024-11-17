package org.confluence.terraentity.entity.ai;

import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.function.Consumer;

public class FSMBossSkills extends CircleBossSkills {


    public FSMBossSkills(AbstractTerraBossBase owner) {
        super(owner);
    }

    public boolean pushSkill(RawAnimation anim, int timeContinue, int timeTrigger,
                             Consumer<AbstractTerraBossBase> stateInit,
                             Consumer<AbstractTerraBossBase> stateTick,
                             Consumer<AbstractTerraBossBase> stateOver
                             ){
        BossSkill skill1 = new BossSkill(anim,timeContinue,timeTrigger,stateInit,stateTick,stateOver);
        bossSkills.add(skill1);
        if(bossSkills.size()==1) tick = 0;
        return true;
    }


}
