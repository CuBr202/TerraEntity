package org.confluence.terraentity.client.entity.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terraentity.client.animation.api.context.AnimatorContext;
import org.confluence.terraentity.client.animation.bone.GeoBoneAnimator;
import org.confluence.terraentity.client.animation.bone.animator.humanoid.LeftHandGeoBoneAnimator;
import org.confluence.terraentity.client.animation.bone.animator.humanoid.RightHandGeoBoneAnimator;
import org.confluence.terraentity.entity.animation.BoneStateMachine;
import org.confluence.terraentity.entity.animation.BoneStates;
import org.confluence.terraentity.entity.animation.IUseItemAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import static org.confluence.terraentity.client.entity.renderer.HumanoidRenderer.LEFT_HAND;
import static org.confluence.terraentity.client.entity.renderer.HumanoidRenderer.RIGHT_HAND;

public class GeoHumanoidModel<T extends LivingEntity & GeoEntity & IUseItemAnimatable<BoneStates>> extends GeoNormalModel<T>{

    protected GeoBoneAnimator<T> rightArmAnimator;
    protected GeoBoneAnimator<T> leftArmAnimator;

    public GeoHumanoidModel(ResourceLocation path) {
        super(path);
    }

    public GeoHumanoidModel(ResourceLocation path, boolean turnsHead) {
        super(path, turnsHead);

    }

    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        float partialTick = animationState.getPartialTick();
        float usingTime = animatable.getTicksUsingItem() + partialTick;

        AnimatorContext context = new AnimatorContext(usingTime);
        handleBone(animatable.getRightArmBoneStateMachine(), animatable, rightArmAnimator, partialTick, context);
        handleBone(animatable.getLeftArmBoneStateMachine(), animatable, leftArmAnimator, partialTick, context);
    }


    public void initBoneAnimators(T animatable, BakedGeoModel model) {
        if(rightArmAnimator == null || leftArmAnimator == null) {
            rightArmAnimator = new RightHandGeoBoneAnimator<>(model.searchForChildBone(model.topLevelBones().get(0), RIGHT_HAND));
            leftArmAnimator = new LeftHandGeoBoneAnimator<>(model.searchForChildBone(model.topLevelBones().get(0), LEFT_HAND));
        }
    }

    private void handleBone(BoneStateMachine<BoneStates> stateMachine, T animatable,
                            GeoBoneAnimator<T> animator, float partialTick, AnimatorContext context) {
        animator.updateState(stateMachine, animatable, partialTick, context);
        stateMachine.apply(partialTick, animator.getBone());
    }

}
