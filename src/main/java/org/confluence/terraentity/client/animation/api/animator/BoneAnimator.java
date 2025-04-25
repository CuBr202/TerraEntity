package org.confluence.terraentity.client.animation.api.animator;

import org.confluence.terraentity.entity.ai.motion.BoneStateMachine;

// 定义骨骼动画处理器接口
public interface BoneAnimator<T, B, C, S> {
    void updateState(BoneStateMachine<S> stateMachine, T animatable, float partialTick, B bone, C context);


}