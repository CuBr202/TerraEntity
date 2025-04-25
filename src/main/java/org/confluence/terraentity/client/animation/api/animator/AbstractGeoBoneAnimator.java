package org.confluence.terraentity.client.animation.api.animator;

import org.confluence.terraentity.client.animation.api.state.BoneStates;
import org.confluence.terraentity.client.animation.api.context.AnimatorContext;
import org.confluence.terraentity.client.animation.api.state.GeoBoneState;
import org.confluence.terraentity.entity.ai.motion.BoneStateMachine;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractGeoBoneAnimator<T> implements GeoBoneAnimator<T>{

    protected final Map<BoneStates, GeoBoneState<T>> states = new EnumMap<>(BoneStates.class);
    public AbstractGeoBoneAnimator() {
        init();
    }

    protected abstract void init();

    protected void addState(BoneStates state, GeoBoneState<T> geoBoneState) {
        this.states.put(state, geoBoneState);
    }

    @Override
    public void updateState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
        if(state.getState() == null){
            state.setState(BoneStates.IDLE);
        }
        GeoBoneState<T> currentState = this.states.get(state.getState());
        if (currentState != null) {
            currentState.handle(state, animatable, partialTick, bone, context);
        }else{
            state.setState(BoneStates.IDLE);
        }
    }
}
