package org.confluence.terraentity.client.animation.api.state;

import org.confluence.terraentity.entity.ai.animation.BoneStateMachine;

// 状态接口
interface BoneState<T, B, C, S> {
    void handle(BoneStateMachine<S> state, T animatable, float partialTick, B bone, C context);
}