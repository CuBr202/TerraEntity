package org.confluence.terraentity.client.animation.api.animator;

import org.confluence.terraentity.entity.ai.animation.BoneStates;
import org.confluence.terraentity.client.animation.api.context.AnimatorContext;
import software.bernie.geckolib.cache.object.GeoBone;

/**
 * Geo骨骼硬编码动画控制器
 * @param <T>
 */
public interface GeoBoneAnimator<T> extends BoneAnimator<T, GeoBone, AnimatorContext, BoneStates> {
}
