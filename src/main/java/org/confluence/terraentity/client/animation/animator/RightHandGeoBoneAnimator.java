package org.confluence.terraentity.client.animation.animator;

import net.minecraft.util.Mth;
import net.minecraft.world.item.CrossbowItem;
import org.confluence.terraentity.client.animation.api.animator.AbstractGeoBoneAnimator;
import org.confluence.terraentity.client.animation.api.context.AnimatorContext;
import org.confluence.terraentity.client.animation.api.state.BoneStates;
import org.confluence.terraentity.client.animation.api.state.GeoBoneState;
import org.confluence.terraentity.client.animation.api.state.HandGeoBoneState;
import org.confluence.terraentity.entity.ai.motion.BoneStateMachine;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import software.bernie.geckolib.cache.object.GeoBone;

import static org.confluence.terraentity.utils.TEUtils.lerpMotion;


// 右手动画处理器
public class RightHandGeoBoneAnimator<T extends AbstractTerraNPC> extends AbstractGeoBoneAnimator<T> {

    @Override
    protected void init() {
        addState(BoneStates.IDLE, new IdleState());
        addState(BoneStates.CROSSBOW_CHARGING, new CrossbowChargingState());
        addState(BoneStates.PROJECTILE_USING, new ProjectileUsingState());
        addState(BoneStates.SWINGING, new SwingingState());
        addState(BoneStates.CROSSBOW_IDLE, new CrossbowIdleState());
        addState(BoneStates.HAND_ITEM_IDLE, new HandItemIdleState());
    }

    @Override
    public void updateState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
        if(state.getState() == null){
            state.setState(BoneStates.IDLE);
        }
        GeoBoneState<T> currentState = this.states.get(state.getState());
        if (currentState != null) {
            currentState.handle(state, animatable, partialTick, bone, context);
        }else{
            state.setState(BoneStates.IDLE);
        }
    }

    private class CrossbowChargingState extends HandGeoBoneState<T> {
        @Override
        protected void updateTransition(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            state.updateState(5, 0.6f, 0.8f, bone.getRotZ());
        }

        @Override
        protected boolean shouldTransition(T animatable, AnimatorContext context) {
            return !(animatable.getUseItem().getItem() instanceof CrossbowItem) ||
                   !animatable.isChargingCrossbow();
        }

        @Override
        protected void transitionState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            state.setState(BoneStates.PROJECTILE_USING);
        }
    }

    private class ProjectileUsingState extends HandGeoBoneState<T> {
        @Override
        protected void updateTransition(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            double lerpx = lerpMotion(context.usingTime, 5, 0, 1.5 - Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot()) * 0.017453292F);
            float lerpy = Mth.lerp(partialTick, animatable.yBodyRotO - animatable.yHeadRotO, animatable.yBodyRot - animatable.yHeadRot) * 0.017453292F;
            state.updateState( 5, lerpx, lerpy, bone.getRotZ());
        }

        @Override
        protected boolean shouldTransition(T animatable, AnimatorContext context) {
            return !animatable.isUsingItem();
        }

        @Override
        protected void transitionState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            if(animatable.isChargingCrossbow()){
                state.setState(BoneStates.CROSSBOW_CHARGING);
            }else{
                state.setState(BoneStates.IDLE);
            }
        }
    }

    private class SwingingState extends HandGeoBoneState<T> {
        @Override
        protected void updateTransition(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            float swingTime = animatable.swingTime + partialTick;
            float swingTicks = animatable.getCurrentSwingDuration();
            double lerpx = lerpMotion(swingTime, swingTicks, 0, 1f );

            double f = lerpx * (1 - lerpx) * 4 * 1.3f;
            float half = swingTicks * 0.5f;
            if(swingTime > half){
                f = lerpMotion(swingTime - half, half, f, bone.getRotX()); // rightArmRotX
            }

            double f2 = lerpx * (1 - lerpx) * 4;
            float half2 = swingTicks * 0.5f;
            if(swingTime > half2){
                f2 = lerpMotion(swingTime - half2, half2, f, 0);
            }

            state.updateState(6, f, f2, bone.getRotZ());
        }

        @Override
        protected boolean shouldTransition(T animatable, AnimatorContext context) {
            return !animatable.swinging;
        }

        @Override
        protected void transitionState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            state.setState(BoneStates.IDLE);
        }
    }


    private class CrossbowIdleState extends HandGeoBoneState<T> {
        @Override
        protected void updateTransition(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            state.updateState(5,0.3f + 0.5F * bone.getRotX(), bone.getRotY(), bone.getRotZ());
        }

        @Override
        protected boolean shouldTransition(T animatable, AnimatorContext context) {
            return animatable.isChargingCrossbow() || !(animatable.getMainHandItem().getItem() instanceof CrossbowItem);
        }

        @Override
        protected void transitionState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            if(animatable.isChargingCrossbow()){
                state.setState(BoneStates.CROSSBOW_CHARGING);
            } else if (animatable.isUsingItem() && animatable.getUseItem().getItem() instanceof CrossbowItem) {
                state.setState(BoneStates.PROJECTILE_USING);
            }else{
                state.setState(BoneStates.IDLE);
            }
        }
    }

    private class HandItemIdleState extends HandGeoBoneState<T> {
        @Override
        protected void updateTransition(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            state.updateState(15, 0.3f + bone.getRotX() * 0.5F, bone.getRotY(), bone.getRotZ());
        }

        @Override
        protected boolean shouldTransition(T animatable, AnimatorContext context) {
            return animatable.getMainHandItem().isEmpty() || animatable.isUsingItem();
        }

        @Override
        protected void transitionState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            state.setState(BoneStates.IDLE);
        }
    }

    private class IdleState extends HandGeoBoneState<T> {
        @Override
        protected void updateTransition(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            state.updateState(5,bone.getRotX(), bone.getRotY(), bone.getRotZ());
        }

        @Override
        protected boolean shouldTransition(T animatable, AnimatorContext context) {
            return animatable.isUsingItem() || !animatable.getMainHandItem().isEmpty();
        }

        @Override
        protected void transitionState(BoneStateMachine<BoneStates> state, T animatable, float partialTick, GeoBone bone, AnimatorContext context) {
            if(animatable.isUsingItem()){
                state.setState(BoneStates.PROJECTILE_USING);
            }else{
                state.setState(BoneStates.HAND_ITEM_IDLE);
            }
        }
    }


}

// 6. 改进后的渲染方法
//public void renderRecursively(/* 参数列表 */) {
//    if (bone.getName().equals(RIGHT_HAND)) {
//        handleBone(animatable.rightArm, animatable, new RightHandGeoBoneAnimator(), partialTick);
//    } else if (bone.getName().equals(LEFT_HAND)) {
//        handleBone(animatable.leftArm, animatable, new LeftHandAnimator(), partialTick);
//    }
//    super.renderRecursively(/* 参数 */);
//}
//
//private void handleBone(BoneStateMachine stateMachine, Animatable animatable,
//                        BoneAnimator animator, float partialTick) {
//    animator.updateState(stateMachine, animatable, partialTick);
//    stateMachine.update(partialTick, bone);
//}
//
//
//
//// 8. 改进后的BoneStateMachine（添加状态管理）
//public class BoneStateMachine {
//    // 原有字段...
//    private BoneAnimator currentAnimator;
//
//    public void setAnimator(BoneAnimator animator) {
//        this.currentAnimator = animator;
//    }
//
//    // 原有方法...
//}