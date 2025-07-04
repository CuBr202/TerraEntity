package org.confluence.terraentity.client.entity.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animation.AnimationState;

public abstract class AnimatorModel<T extends GeoEntity> extends GeoNormalModel<T>{

    public AnimatorModel(ResourceLocation path) {
        super(path);
    }

    public AnimatorModel(ResourceLocation path, boolean turnsHead) {
        super(path, turnsHead);
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        float partialTick = animationState.getPartialTick();
        customAnimations(animatable, instanceId, animationState, partialTick);
    }

    public abstract void initBoneAnimators(T animatable, BakedGeoModel model);

    public abstract void customAnimations(T animatable, long instanceId, AnimationState<T> animationState,  float partialTick);


}
