package org.confluence.terraentity.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.utils.ModUtils;

public class DashGoal extends Goal {

    private final AbstractMonster mob;

    int dashTime = 0;
    int _dashTime;

    int rootYSpeed;

    float speed;
    Vec3 lastDir = Vec3.ZERO;

    /**
     *
     * @param entity self
     * @param speed 冲刺加速度
     * @param additionTime 冲刺时间
     */
    public DashGoal(AbstractMonster entity,float speed,int additionTime,int rootSpeed) {
        super();
        this.mob = entity;
        this.speed = speed;
        this._dashTime = -additionTime;
        this.rootYSpeed = rootSpeed;
    }
    @Override
    public boolean canUse() {

        return mob.getTarget()!=null;
    }

    public void start() {

    }

    public void stop() {

    }

    public void tick() {
        LivingEntity target = mob.getTarget();

        //mob.getLookControl().setLookAt(target,0.01f,0.01f);
        mob.lookAt(target, rootYSpeed,85);
        //mob.setYRot(mob.yHeadRot);
        if(--dashTime > _dashTime){
            mob.setDeltaMovement(lastDir);
            lastDir.scale(0.95f);
            return;
        }

        float angle = (float) ModUtils.angleBetween(mob.getTarget().getEyePosition().subtract(mob.position()), mob.getForward());


        if(angle < 3.14159/6
        ) {//30度 向前方加速

            mob.addDeltaMovement(mob.getForward().normalize().scale(speed));
        }
        /*
        else if(angle < 3.14159/2){ //90度
            mob.addDeltaMovement(target.getEyePosition().subtract(mob.position()).normalize().scale(0.1f));

        }
        */
        else {//背向暂停行动
            dashTime = 0;
            lastDir = mob.getDeltaMovement();
        }


    }

}
