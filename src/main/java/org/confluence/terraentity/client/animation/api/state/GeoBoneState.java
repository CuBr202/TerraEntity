package org.confluence.terraentity.client.animation.api.state;

import org.confluence.terraentity.client.animation.api.context.AnimatorContext;
import org.confluence.terraentity.entity.ai.animation.BoneStates;
import software.bernie.geckolib.cache.object.GeoBone;

public interface GeoBoneState <T> extends BoneState<T, GeoBone, AnimatorContext, BoneStates> {
}
