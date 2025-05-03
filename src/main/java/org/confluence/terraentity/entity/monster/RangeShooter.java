package org.confluence.terraentity.entity.monster;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.monster.prefab.AttributeBuilder;
import org.confluence.terraentity.entity.proj.BaseProj;
import org.confluence.terraentity.entity.proj.LineProj;
import org.confluence.terraentity.init.entity.TEProjectileEntities;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

import java.util.function.Supplier;

public class RangeShooter extends AbstractMonster {

    int _phase = 200;
    int phase = _phase;
    Supplier<? extends EntityType<? extends BaseProj<?>>> projType;

    public RangeShooter(EntityType<? extends Monster> type, Level level, Supplier<? extends EntityType<? extends BaseProj<?>>> projType, AttributeBuilder builder) {
        super(type, level, builder);
        this.projType = projType;
    }

    public void tick() {
        super.tick();
        var att = this.getAttribute(Attributes.FOLLOW_RANGE);
        if(getTarget() != null){
            if(!att.hasModifier(TerraEntity.space("battle"))){
                att.addTransientModifier(new AttributeModifier(TerraEntity.space("battle"),1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            LivingEntity target = getTarget();
            lookAt(target, 10, 70);
            this.moveControl.strafe(0.01f, 0.01f);
            if(phase == 180 || phase == 130 || phase == 80){
                BaseProj proj = projType.get().create(level());
                proj.setOwner(this);
                proj.setPos(this.getEyePosition());
                proj.setDamage((float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                proj.shoot((float)(target.getX() - this.getX()), (float)(target.getY() - target.getBbHeight() * 0.3f - this.getY()), (float)(target.getZ() - this.getZ()), 0.3f, 0.8f);
                level().addFreshEntity(proj);
                this.swing(InteractionHand.MAIN_HAND, true);
            }

            if(--phase<= 0){
                phase = _phase;
                Vec3 pos;
                for(int i = 0; i < 4; i++) {
                    pos = LandRandomPos.getPosTowards(this, 20, 5, target.position());
                    if(pos!= null){
                        this.moveTo(pos);
                        break;
                    }
                }
            }
        }else{
            if(att.hasModifier(TerraEntity.space("battle"))){
                att.removeModifier(TerraEntity.space("battle"));
            }
            phase = _phase;
        }

    }

    public int getCurrentSwingDuration() {
        return 20;
    }

    RawAnimation attack = RawAnimation.begin().thenPlay("attack.range");
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Idle/Attack", 5, state ->{
            if(this.swingTime > 0){
                return state.setAndContinue(attack);
            }
            return state.setAndContinue(DefaultAnimations.IDLE);
        }
        ));
    }

}
