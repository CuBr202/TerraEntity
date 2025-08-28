package org.confluence.terraentity.entity.monster.humanoid;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.ai.goal.FloatAiGoal;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;

public class Wraith extends HumanoidMonster {

    public Wraith(EntityType<? extends Wraith> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    @Override
    public void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.addGoal(4, this.meleeGoal);
        }
    }

    @Override
    protected Goal createMeleeGoal() {
        return new FloatAiGoal(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Idle", 5, state ->{
            return state.setAndContinue(DefaultAnimations.IDLE);
        }));
    }
}
