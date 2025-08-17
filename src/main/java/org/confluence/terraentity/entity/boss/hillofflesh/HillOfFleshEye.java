package org.confluence.terraentity.entity.boss.hillofflesh;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.confluence.terraentity.entity.proj.LineProj;
import org.confluence.terraentity.init.entity.TEProjectileEntities;

public class HillOfFleshEye extends HillOfFleshPart implements RangedAttackMob {

    int _shootDelay = 10;
    int _shootInterval = 40;
    int _shootCount = 3;
    int shootDelay;
    int shootCount;

    public HillOfFleshEye(HillOfFlesh parentMob, String name, float width, float height) {
        super(parentMob, name, width, height);
    }

    @Override
    protected void tickPart(double offsetX, double offsetY, double offsetZ, int index) {
        this.findTarget();
        this.getParent().setTarget(index, this.target);
        if(this.target!= null && !this.level().isClientSide() && this.stareCount >= 10){
            this.shoot(this.target);
        }
    }

    private void shoot(LivingEntity target){
        if(--this.shootDelay <= 0){
            --this.shootCount;

            if(this.shootCount <= 0){
                this.shootCount = _shootCount;
                this.shootDelay = _shootInterval + this.getRandom().nextInt(20);

                this.performRangedAttack(target, 1.0f); // 最后一击增加射速
            }else{
                this.shootDelay = _shootDelay;
                this.performRangedAttack(target, 0.5f);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float v) {
        LineProj proj = TEProjectileEntities.VILE_SPIT_PROJ.get().create(this.level());
        if (proj != null) {
            proj.setOwner(this.parentMob);
            proj.setPos(this.getX(), this.getY() + 0.5f, this.getZ());
            double x = target.getX() - this.getX();
            double y = target.getY() - this.getY();
            double z = target.getZ() - this.getZ();
            proj.shoot(x,y,z,v,1);
            proj.setDamage(5);
            this.level().addFreshEntity(proj);
        }
    }
}
