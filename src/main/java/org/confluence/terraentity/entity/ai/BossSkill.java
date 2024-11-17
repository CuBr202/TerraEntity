package org.confluence.terraentity.entity.ai;

import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.function.Consumer;

public class BossSkill<T extends Mob> {


    public int timeContinue;
    public int timeTrigger;
    public RawAnimation anim;

    public Consumer<T> stateInit;
    public Consumer<T> stateTick;
    public Consumer<T> stateOver;

    /**
     *
     * @param anim 动画名称
     * @param timeContinue 状态持续时间
     * @param timeTrigger 逻辑触发时间
     */
    public BossSkill(RawAnimation anim, int timeContinue, int timeTrigger){
        this.anim = anim;
        this.timeContinue = timeContinue;
        this.timeTrigger = timeTrigger;
    }

    public BossSkill(RawAnimation anim, int timeContinue, int timeTrigger,
                     Consumer<T> stateInit,
                     Consumer<T> stateTick,
                     Consumer<T> stateOver
    ){
        this.anim = anim;
        this.timeContinue = timeContinue;
        this.timeTrigger = timeTrigger;
        this.stateInit = stateInit;
        this.stateTick = stateTick;
        this.stateOver = stateOver;
    }

    public void addStateReset(Consumer<T> stateTick){
        this.stateTick = stateTick;
    };
    public void addStateInit(Consumer<T> stateInit){
        this.stateInit = stateInit;
    };
    public void addStateOver(Consumer<T> stateOver){
        this.stateOver = stateOver;
    };
}
