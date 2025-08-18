package org.confluence.terraentity.api.entity.ai;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.confluence.lib.mixed.SelfGetter;
import org.confluence.terraentity.entity.ai.fsm.CircleMobSkills;
import org.confluence.terraentity.entity.ai.fsm.MobSkill;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DataTickets;

/**
 * 适配Geo动画的状态机接口
 */
public interface IFSMGeoMob<T extends Mob> extends GeoEntity , SelfGetter<T> {

    CircleMobSkills<T> getSkills();

    ClientBoundAnimationMessage getAnimationMessage();

    void addSkills();

    default void addSkill(MobSkill mobSkill) {
        getSkills().pushSkill(mobSkill);
    }

    default void syncSkills(EntityDataAccessor<?> key) {
        CircleMobSkills<T> skills = getSkills();
        if(confluence$self().level().isClientSide() && skills!= null && key == skills.skillIndexData){
            skills.index = confluence$self().getEntityData().get(skills.skillIndexData);
            getAnimationMessage().lastSkillTick = confluence$self().tickCount;
            skills.tick = 0;
        }
    }

    default AnimationController<IFSMGeoMob<T>> fsmAnimationController() {
        return new AnimationController<>(this, 0, state -> {
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
