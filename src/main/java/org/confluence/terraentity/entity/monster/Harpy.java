package org.confluence.terraentity.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.ai.goal.DashGoal;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import org.confluence.terraentity.entity.proj.LineProj;
import org.confluence.terraentity.entity.proj.VileSpitProj;
import org.confluence.terraentity.init.entity.TEProjectileEntities;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class Harpy extends AbstractMonster {

    int _shootTick = 20;
    int shootTick = _shootTick;

    int _shootCooldown = 150;
    int shootCooldown = _shootCooldown;

    int _shootCount = 3;
    int shootCount = 3;


    public Harpy(EntityType<? extends Monster> type, Level level, AttributeBuilder builder) {
        super(type, level, builder);

    }

    public void tick(){
        super.tick();
        if(getTarget() != null) {
            this.lookControl.setLookAt(getTarget(),5, 80);
            this.lookAt(getTarget(), 5, 80);
            this.setDeltaMovement(getDeltaMovement().scale(0.95f));
            if (--shootCooldown < 0) {
                if (--shootTick < 0) {
                    shootTick = _shootTick;
                    shoot(getTarget());
                    if (--shootCount <= 0) {
                        shootCount = _shootCount;

                        shootCooldown = _shootCooldown;
                    }
                }

            }
        }
    }

    protected void shoot(LivingEntity living){
        LineProj proj = new VileSpitProj(TEProjectileEntities.VILE_SPIT_PROJ.get(), level());
        proj.setPos(this.getEyePosition());
        proj.setOwner(this);
        proj.shoot(living.getX() - this.getX(), living.getY() - this.getY(), living.getZ() - this.getZ(), 0.5f, 2f);
        proj.setDamage((float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        level().addFreshEntity(proj);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new DashGoal(this,
                0.95f, 0.5f, 30, 0.02f,
                10,90f,30f){
            @Override
            public void dashBackTick(){
                if(getTarget()!=null){
                    lookAt(getTarget(), 5, 80);
                }
            }
            @Override
            public boolean canUse() {
                return super.canUse() && shootCooldown > 0 || getState() != States.idle;
            }

        });
    }

    RawAnimation fly = RawAnimation.begin().thenLoop("fly");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Idle/Attack", 5, state ->{
            return state.setAndContinue(fly);
        }));
    }
}
