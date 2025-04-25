package org.confluence.terraentity.client.animation.api.state;

import org.confluence.terraentity.client.animation.api.context.AnimatorContext;
import org.confluence.terraentity.entity.ai.animation.BoneStateMachine;
import org.confluence.terraentity.entity.ai.animation.BoneStates;
import software.bernie.geckolib.cache.object.GeoBone;

// 右手状态基类
public abstract class HandGeoBoneState<T> implements GeoBoneState<T> {

    protected abstract boolean shouldTransition(T animatable, AnimatorContext context);

    protected abstract void transitionState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context);

    protected abstract void updateTransition(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context);

    @Override
    public void handle(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
        if (shouldTransition(animatable, context)) {
            transitionState(state, animatable, partialTick, bone, context);
        } else {
            updateTransition(state, animatable, partialTick, bone, context);
        }
    }


}
