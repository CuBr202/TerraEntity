package org.confluence.terraentity.entity.ai.motion;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DashComponent {
    public Vec3 direction;
    public Vec3 targetPos;

    public Entity owner;

    public DashComponent(Entity owner) {
        this.owner = owner;
        this.direction = Vec3.ZERO;
        this.targetPos = owner.position();
    }


    public void setDirection(Vec3 direction) {
        this.direction = direction;
    }

    /**
     * 悬挂在目标实体目标位置
     * @param target 目标实体
     * @param distance xz距离
     * @param height 高度
     * @param speed 速度
     */
    public void hangOn(LivingEntity target, float distance, float height, float speed){
        if(target!=null){
            setNearestTargetPos(target, distance, height);
            direction = targetPos.subtract(owner.position());
            owner.addDeltaMovement(direction.scale(speed * 0.01f));
        }
    }

    public void accelerate(float speed) {
        owner.addDeltaMovement(direction.scale(speed));
    }
    public void uniformMove(float speed) {
        owner.setDeltaMovement(direction.normalize().scale(speed));
    }

    /**
     * 预判冲刺方向
     * @param target 目标实体
     * @return 冲刺方向
     */
    public void setPredictDirection(Entity target){
        direction = target.position().add(0, 1, 0).add(target.getKnownMovement().scale(10)).subtract(owner.position());
    }

    /**
     * 获取目标相对直线位置
     * @param target   目标实体
     * @param distance xz距离
     * @param height   高度
     */
    public void setNearestTargetPos(Entity target, float distance, float height){
        targetPos = owner.position().subtract(target.position()).multiply(1, 0, 1).normalize().scale(distance).add(0, height, 0).add(target.position());
    }

    public void lookAtDirection(){
        owner.lookAt(EntityAnchorArgument.Anchor.EYES, direction.add(owner.position()));
    }
}
