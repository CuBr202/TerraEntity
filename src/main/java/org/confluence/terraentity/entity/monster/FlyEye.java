package org.confluence.terraentity.entity.monster;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.boss.BrainOfCthulhu;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;

public class FlyEye extends AbstractMonster{

    private BrainOfCthulhu owner;
    public Vec3 homePos;
    public LivingEntity target;
    public boolean ready = true;
    private static final float MOVE_SPEED = 0.5f;

    // 0 为攻击， 1 为返回
    public int state = 1;
    public FlyEye(EntityType<? extends Monster> type, Level level) {
        super(type, level, new AbstractPrefab(5,0,1,0,0,0.1f)
                .getPrefab().setNoGravity());
        this.noPhysics = true;
    }

    public void setOwner(BrainOfCthulhu owner) {
        this.owner = owner;
    }

    public BrainOfCthulhu getOwner() {
        return owner;
    }

    public void attack(LivingEntity target){
        this.target = target;
        state = 0;
    }

    public void tick(){
        super.tick();
        if(owner == null) return;

        if(!level().isClientSide && isAlive()){
            if(state == 0){
                ready = false;
                if(target != null && target.isAlive()) {
                    this.setDeltaMovement(target.position().subtract(position()).normalize().scale(MOVE_SPEED));

                }else {
                    state = 1;
                }
            }else {
                if(homePos != null){
                    if(position().distanceToSqr(homePos) < 0.1f)
                        ready = true;
                    addDeltaMovement(homePos.subtract(position()).normalize().scale(MOVE_SPEED / 10));
                }
            }
        }
    }

    public boolean isReady() {
        return ready && state == 1;
    }

    public boolean hurt(DamageSource source, float amount) {
        if(state == 0) {
            state = 1;
            setDeltaMovement(owner.position().subtract(position()).normalize());
        }
        return super.hurt(source, amount);
    }

    public void doAttack(LivingEntity entity) {
        if(state == 1) return;
        super.doAttack(entity);
        if(state == 0) state = 1;
    }
}
