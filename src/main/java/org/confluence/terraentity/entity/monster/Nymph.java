package org.confluence.terraentity.entity.monster;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.ai.goal.AccelerateOnSeeingGoal;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;


/**
 * 芙宁
 */
public class Nymph extends AbstractMonster{

    int delayTime = 0;
    public Nymph(EntityType<? extends Monster> type, Level level) {
        super(type, level, new AbstractPrefab(156,3,15,5,1,1).getPrefab());

    }

    private static final EntityDataAccessor<Boolean> DATA_TRIGGER =  SynchedEntityData.defineId(Nymph.class, EntityDataSerializers.BOOLEAN);
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TRIGGER, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 0.6D, true){
            @Override
            public boolean canUse() {
                return super.canUse() && delayTime > 10;
            }
        });

        this.targetSelector.addGoal(1,new AccelerateOnSeeingGoal(this,0.25f){
            @Override
            public boolean canUse() {
                return super.canUse() && delayTime > 10;
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class,false, LivingEntity::canBeSeenAsEnemy){
            @Override
            public boolean canUse() {
                return super.canUse() && tickCount > 50;
            }
        });
    }

    public boolean isTrigger() {
        return this.entityData.get(DATA_TRIGGER);
    }

    public void setTrigger(boolean trigger) {
        this.entityData.set(DATA_TRIGGER, trigger);
    }

    public void tick(){
        super.tick();
        if(getTarget() != null && tickCount > 50){
            if(!isTrigger()){
                this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
            }else{
                delayTime++;
            }
            setTrigger(true);
        }
    }


    RawAnimation sit = RawAnimation.begin().thenLoop("sit");
    RawAnimation dash = RawAnimation.begin().thenLoop("dash");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller", 20, state->{
            if(!isTrigger()){
                return state.setAndContinue(sit);
            }
            return state.setAndContinue(dash);
        }));
    }
}
