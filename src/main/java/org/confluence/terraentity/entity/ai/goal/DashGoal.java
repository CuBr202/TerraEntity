package org.confluence.terraentity.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.utils.ModUtils;

public class DashGoal extends Goal {

    protected final AbstractMonster mob;

    protected int dashTime = 0;
    protected int _dashTime;
    protected int rootYSpeed;
    protected float speed;
    protected Vec3 lastDir = Vec3.ZERO;
    protected  States state = States.idle;
    enum States{
        dashing,
        dashing_back,
        idle
    }
    /**
     * @param entity self
     * @param speed 冲刺加速度
     * @param additionTime 冲刺时间
     * @param rootSpeed y转向速度
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
        return mob.getTarget()!=null && mob.getTarget().isAlive();
    }

    public void start() {

    }

    public void stop() {

    }

    protected void lookAtTarget(LivingEntity target) {
        mob.getLookControl().setLookAt(target, 0, 85);
        mob.lookAt(target, rootYSpeed, 85);
    }

    protected void downSpeed(){
        lastDir = lastDir.scale(mob.builder.friction);
        mob.setDeltaMovement(lastDir);
    }

    protected float getAngle(LivingEntity target){
        return (float) ModUtils.angleBetween(target.position().subtract(mob.position()), mob.getForward());
    }



    public void tick() {
        LivingEntity target = mob.getTarget();
        if(target == null ||!target.isAlive())
            return;
        System.out.println(state);
        if(mob.hurtTime>0) {
            state = States.idle;

        }
        if(state == States.dashing_back){
            --dashTime;
            downSpeed();
            mob.addDeltaMovement( new Vec3(0,0.05f,0));
            if(dashTime <= _dashTime) state = States.idle;
            return;
        }
        else if(state == States.idle) {
            lookAtTarget(target);
            downSpeed();

            float angle = getAngle(target);
            if(angle < mob.builder.triggerAngle / 180 * 3.14159) {
                state = States.dashing;
            }
            return;
        }

        float angle = getAngle(target);

        if(angle < mob.builder.turnAngle / 180 * 3.14159) {//30度 向前方加速
            lookAtTarget(target);

            float speed = (float) Math.min(mob.builder.maxSpeed , mob.getDeltaMovement().add(mob.getDeltaMovement().normalize().scale(mob.builder.FLYING_SPEED)).length());
            if(speed < 0.1) {
                mob.setDeltaMovement(0, 0.1, 0);
                return;
            }

            Vec3 dir1 = mob.getForward().normalize();
            Vec3 dir2 = target.getEyePosition().subtract(mob.position()).normalize();
            Vec3 newSpeed = dir1.add(dir2).normalize().scale(speed);

            mob.setDeltaMovement(newSpeed);


        }
        else {//背向 往前冲刺一段时间
            dashTime = 0;
            state = States.dashing_back;
            lastDir = mob.getDeltaMovement();
        }

    }

}
