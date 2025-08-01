package org.confluence.terraentity.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;

public class Ghost extends AbstractMonster {

    public Ghost(EntityType<? extends Monster> type, Level level, AttributeBuilder builder) {
        super(type, level, builder.setController((c, e)->{
            c.add(new AnimationController<>(e, "Walk", 0, state ->
                    state.setAndContinue(DefaultAnimations.WALK)));
        }));

        this.noPhysics = true;
        this.getAttribute(Attributes.GRAVITY).setBaseValue(0);
    }

    @Override
    protected void registerTargetGoal(GoalSelector targetSelector){

        targetSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 5));
    }

    @Override
    public void tick(){
        super.tick();
        this.addDeltaMovement(new Vec3(0, Math.sin(this.tickCount * 0.2f) * 0.008f, 0));

        if(this.getTarget() == null || this.hurtTime > 0){
            return;
        }

        LivingEntity target = this.getTarget();
        this.lookAt(target, 10, 10);
        if(target.isAlive()){
            this.setDeltaMovement(target.position().subtract(this.position()).normalize().scale(0.2f));
        }
    }

}

