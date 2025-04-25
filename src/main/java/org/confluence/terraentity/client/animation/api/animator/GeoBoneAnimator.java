package org.confluence.terraentity.client.animation.api.animator;

import org.confluence.terraentity.client.animation.api.state.BoneStates;
import org.confluence.terraentity.client.animation.api.context.AnimatorContext;
import software.bernie.geckolib.cache.object.GeoBone;

public interface GeoBoneAnimator<T> extends BoneAnimator<T, GeoBone, AnimatorContext, BoneStates> {
}
