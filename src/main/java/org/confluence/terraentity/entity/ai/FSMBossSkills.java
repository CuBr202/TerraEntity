package org.confluence.terraentity.entity.ai;

import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;

import java.util.function.Consumer;

public class FSMBossSkills extends CircleBossSkills {


    public FSMBossSkills(AbstractTerraBossBase owner) {
        super(owner);
    }

    public boolean pushSkill(String skillID,String skill,int timeContinue,int timeTrigger,
                             Consumer<AbstractTerraBossBase> stateInit,
                             Consumer<AbstractTerraBossBase> stateTick,
                             Consumer<AbstractTerraBossBase> stateOver
                             ){
        BossSkill skill1 = new BossSkill(skillID,skill,timeContinue,timeTrigger,stateInit,stateTick,stateOver);
        bossSkills.add(skill1);
        stateIndexMap.put(skillID,bossSkills.size()-1);
        if(bossSkills.size()==1) tick = 0;
        return true;
    }


}
