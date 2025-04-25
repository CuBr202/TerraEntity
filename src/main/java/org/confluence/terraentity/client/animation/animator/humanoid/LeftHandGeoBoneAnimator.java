package org.confluence.terraentity.client.animation.animator.humanoid;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import org.confluence.terraentity.client.animation.api.animator.AbstractGeoBoneAnimator;
import org.confluence.terraentity.client.animation.api.context.AnimatorContext;
import org.confluence.terraentity.client.animation.api.state.HandGeoBoneState;
import org.confluence.terraentity.entity.ai.animation.BoneStates;
import org.confluence.terraentity.client.animation.api.state.GeoBoneState;
import org.confluence.terraentity.entity.ai.animation.BoneStateMachine;
import org.confluence.terraentity.entity.ai.animation.IUseItemAnimatable;
import software.bernie.geckolib.cache.object.GeoBone;

import static org.confluence.terraentity.utils.TEUtils.lerpMotion;

/**
 * 人形怪左手骨骼动画控制器
 * @param <T> 实体类型
 */
public class LeftHandGeoBoneAnimator <T extends LivingEntity & IUseItemAnimatable> extends AbstractGeoBoneAnimator<T> {

    @Override
    protected void init() {
        addState(BoneStates.CROSSBOW_CHARGING, new CrossbowChargingState());
        addState(BoneStates.PROJECTILE_USING, new UsingItemState());
        addState(BoneStates.IDLE, new IdleState());
    }


    @Override
    public void updateState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
        GeoBoneState<T> currentState = this.states.get(state.getState());
        if (currentState != null) {
            currentState.handle(state, animatable, partialTick, bone, context);
        }else{
            state.setState(BoneStates.IDLE);
        }
    }


    class UsingItemState extends HandGeoBoneState<T> {
        @Override
        protected void updateTransition(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            double lerpx = lerpMotion(context.usingTime, 5, 0, 1.3 - Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot()) * 0.017453292F);
            float lerpy = Mth.lerp(partialTick, animatable.yBodyRotO - animatable.yHeadRotO, animatable.yBodyRot - animatable.yHeadRot) * 0.017453292F;
            // 防止转太多穿模
            lerpy = Math.max(lerpy - 0.5F, -1.4f);

            state.updateState(5, (float) lerpx, lerpy, bone.getRotZ());
        }

        @Override
        protected boolean shouldTransition(T animatable, AnimatorContext context) {
            return !animatable.isUsingItem() || animatable.isChargingCrossbow();
        }

        @Override
        protected void transitionState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            if (animatable.isChargingCrossbow()) {
                state.setState(BoneStates.CROSSBOW_CHARGING);
            } else if (!animatable.isUsingItem()) {
                state.setState(BoneStates.IDLE);
            }
        }
    }

    class IdleState extends HandGeoBoneState<T> {
        @Override
        protected void updateTransition(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            state.updateState(10, bone.getRotX(), bone.getRotY(), bone.getRotZ());
        }

        @Override
        protected boolean shouldTransition(T animatable, AnimatorContext context) {
            return animatable.isUsingItem() || animatable.isChargingCrossbow();
        }

        @Override
        protected void transitionState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            if (animatable.isChargingCrossbow()) {
                state.setState(BoneStates.CROSSBOW_CHARGING);
            } else if (animatable.isUsingItem()) {
                state.setState(BoneStates.PROJECTILE_USING);
            }
        }
    }

    class CrossbowChargingState extends HandGeoBoneState<T> {
        @Override
        protected void updateTransition(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            double lerpx = lerpMotion(animatable.getChargingTicks() + partialTick, 20, 1, 1.2);
            double lerpy = -lerpMotion(animatable.getChargingTicks() + partialTick, 20, 0.8, 1.2);

            state.updateState(20, (float) lerpx, (float) lerpy, bone.getRotZ());
        }

        @Override
        protected boolean shouldTransition(T animatable, AnimatorContext context) {
            return !(animatable.getUseItem().getItem() instanceof CrossbowItem) ||
                    !animatable.isChargingCrossbow();
        }

        @Override
        protected void transitionState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            if (animatable.isUsingItem()) {
                state.setState(BoneStates.PROJECTILE_USING);
            } else {
                state.setState(BoneStates.IDLE);
            }
        }
    }
}
