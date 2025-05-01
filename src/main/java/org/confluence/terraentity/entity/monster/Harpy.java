package org.confluence.terraentity.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class Harpy extends AbstractMonster {

    public Harpy(EntityType<? extends Monster> type, Level level, AttributeBuilder builder) {
        super(type, level, builder);
    }

    public void tick(){
        super.tick();


    }

    RawAnimation fly = RawAnimation.begin().thenLoop("fly");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Idle/Attack", 5, state ->{
            return state.setAndContinue(fly);
        }));
    }
}
