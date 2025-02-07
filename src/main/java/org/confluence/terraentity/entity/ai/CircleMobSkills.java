package org.confluence.terraentity.entity.ai;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.ArrayList;
import java.util.List;

public class CircleMobSkills<T extends Mob> {
    public T owner;
    protected final List<MobSkill> mobSkills = new ArrayList<>();

    public int tick = 0;
    public int index = 0;
    public boolean ifStateInit = false;
    public EntityDataAccessor<Integer> skillIndexData;
    public CircleMobSkills(T owner, EntityDataAccessor<Integer> skillIndexData){
        this.owner = owner;
        this.skillIndexData = skillIndexData;
    }
    public int count(){return mobSkills.size();};

    public boolean pushSkill(MobSkill skill){
        mobSkills.add(skill);
        if(mobSkills.size()==1) tick = 0;
        return true;
    }


    public void tick(){
        if(owner.level().isClientSide()) return ;
        if(mobSkills.isEmpty()) return ;
        this.tick++;

        if(mobSkills.get(index).stateTick !=null) {
            mobSkills.get(index).stateTick.accept(owner);
        }
        if(mobSkills.isEmpty())return;
        if(mobSkills.get(index).timeContinue < tick) {
            forceEnd();
            forceStartIndex(index);
        }
    }


    /** 强制结束当前状态 **/
    public void forceEnd(){
        tick = 0;
        int lastIndex = index;
        index = (index +1) % mobSkills.size();

        //状态结束
        if(mobSkills.get(lastIndex).stateOver!=null) mobSkills.get(lastIndex).stateOver.accept(owner);
        owner.getEntityData().set(skillIndexData, index);
    }
    /** 强制跳转状态 **/
    public void forceStartIndex(int index){
        tick = 0;
        this.index = index;

        //初次进入状态
        if(mobSkills.get(index).stateInit!=null) mobSkills.get(index).stateInit.accept(owner);
        owner.getEntityData().set(skillIndexData, index);
    }


    /** tick == triggerTime **/
    public boolean canTrigger(){
        if(mobSkills.isEmpty()) return false;
        return mobSkills.get(index).timeTrigger == this.tick;
    }
    /** tick > triggerTime **/
    public boolean canContinue(){
        if(mobSkills.isEmpty()) return false;
        return mobSkills.get(index).timeTrigger < this.tick;
    }
    public RawAnimation getCurAnim(){
        if(!mobSkills.isEmpty())
            return mobSkills.get(index).anim;
        return null;
    }
    public int getCurAnimFullTick(){
        if(!mobSkills.isEmpty())
            return mobSkills.get(index).timeContinue;
        return -1;
    }



}
