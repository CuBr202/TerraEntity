package org.confluence.terraentity.api.entity.ai;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.confluence.terraentity.entity.ai.CircleMobSkills;
import org.confluence.terraentity.entity.ai.MobSkill;
import org.confluence.terraentity.entity.ai.goal.FSMGoal;
import org.confluence.terraentity.mixed.SelfGetter;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

/**
 * 适配Geo动画的状态机接口
 */
public interface IFSMGeoMob<T extends Entity> extends GeoEntity , SelfGetter<T> {

    CircleMobSkills<T> getSkills();

    ClientBoundAnimationMessage getAnimationMessage();

    /**
     * 当使用{@link FSMGoal}时可以忽略
     */
    void addSkills();

    /**
     * 当使用{@link FSMGoal}时可以忽略
     */
    default void addSkill(MobSkill mobSkill) {
        getSkills().pushSkill(mobSkill);
    }

    default void syncSkills(EntityDataAccessor<?> key) {
        CircleMobSkills<T> skills = getSkills();
        if(te$getSelf().level().isClientSide() && skills!= null && key == skills.skillIndexData){
            skills.index = te$getSelf().getEntityData().get(skills.skillIndexData);
            getAnimationMessage().lastSkillTick = te$getSelf().tickCount;
            skills.tick = 0;
        }
    }

    default AnimationController<IFSMGeoMob<T>> fsmAnimationController() {
        return new AnimationController<>(this, 10, state -> {
            Entity entity =  state.getData(DataTickets.ENTITY);
            if (!entity.isAlive()) return PlayState.STOP;
            if (getSkills().count() == 0) return PlayState.STOP;

            RawAnimation pose = getSkills().getCurAnim();
            if(pose == null) return PlayState.STOP;
            state.setAnimation(pose);
            if (getAnimationMessage().lastAnimIndex != getSkills().index) {
                getAnimationMessage().lastAnimIndex = getSkills().index;
                state.resetCurrentAnimation();

                return PlayState.STOP;
            }
            return PlayState.CONTINUE;
        });
    }

    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(fsmAnimationController());
    }



    class ClientBoundAnimationMessage {
        public int lastAnimIndex;
        public int lastSkillTick;
    }
}
