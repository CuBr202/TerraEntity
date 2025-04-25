package org.confluence.terraentity.client.animation.api.animator;

import org.confluence.terraentity.entity.ai.animation.BoneStates;
import org.confluence.terraentity.client.animation.api.context.AnimatorContext;
import org.confluence.terraentity.client.animation.api.state.GeoBoneState;
import org.confluence.terraentity.entity.ai.animation.BoneStateMachine;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.EnumMap;
import java.util.Map;

/**
 * Geo骨骼硬编码动画控制器
 * @param <T> 实体类型
 */
public abstract class AbstractGeoBoneAnimator<T> implements GeoBoneAnimator<T>{

    protected final Map<BoneStates, GeoBoneState<T>> states = new EnumMap<>(BoneStates.class);
    public AbstractGeoBoneAnimator() {
        init();
    }

    /**
     * 初始化状态机
     */
    protected abstract void init();

    /**
     * 添加状态
     * @param state 骨骼状态枚举
     * @param geoBoneState 骨骼状态
     */
    protected void addState(BoneStates state, GeoBoneState<T> geoBoneState) {
        this.states.put(state, geoBoneState);
    }


    @Override
    public void updateState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
        GeoBoneState<T> currentState = this.states.get(state.getState());
        if (currentState != null) {
            currentState.handle(state, animatable, partialTick, bone, context);
        }else{
            state.setState(BoneStates.IDLE);
        }
    }
}
