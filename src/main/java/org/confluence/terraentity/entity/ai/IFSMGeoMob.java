package org.confluence.terraentity.entity.ai;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.confluence.terraentity.mixinauxiliary.SelfGetter;
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

    int getLastAnimIndex();

    void setLastAnimIndex(int lastAnimIndex);

    int getLastSkillTick();

    void setLastSkillTick(int lastSkillTick);

    void addSkills();

    default void addSkill(MobSkill mobSkill) {
        getSkills().pushSkill(mobSkill);
    }

    default void syncSkillIndex(){
        te$getSelf().getEntityData().set(getSkills().skillIndexData, getSkills().index);
    }

    default void syncSkills(EntityDataAccessor<?> key) {
        if(te$getSelf().level().isClientSide() && getSkills()!= null && key == getSkills().skillIndexData){
            getSkills().index = te$getSelf().getEntityData().get(getSkills().skillIndexData);
            setLastSkillTick(te$getSelf().tickCount);
            getSkills().tick = 0;
        }
    }

    default void addToLevel(){
        addSkills();
    }

    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 20, state -> {
            Entity entity =  state.getData(DataTickets.ENTITY);
            if (!entity.isAlive()) return PlayState.STOP;
            if (getSkills().count() == 0) return PlayState.STOP;

            RawAnimation pose = getSkills().getCurAnim();
            if(pose == null) return PlayState.STOP;
            state.setAnimation(pose);
            if (getLastAnimIndex() != getSkills().index) {
                setLastAnimIndex(getSkills().index);
                state.resetCurrentAnimation();

                return PlayState.STOP;
            }
            return PlayState.CONTINUE;
        }));
    }
}
