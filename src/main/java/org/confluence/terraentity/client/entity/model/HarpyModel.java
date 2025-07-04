package org.confluence.terraentity.client.entity.model;

import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.entity.monster.Harpy;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animation.AnimationState;

public class HarpyModel extends AnimatorModel<Harpy> {

    public HarpyModel(ResourceLocation path) {
        super(path);
    }

    @Override
    public void initBoneAnimators(Harpy animatable, BakedGeoModel model) {

    }

    @Override
    public void customAnimations(Harpy animatable, long instanceId, AnimationState<Harpy> animationState, float partialTick) {

    }
}
