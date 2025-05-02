package org.confluence.terraentity.entity.boss;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DungeonGuardian extends Skeletron {


    public DungeonGuardian(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setAttactDamage(999);
        this.baseArmor = 999;
        this.baseHealth = 10000;

    }

    @Override
    protected void registerGoals() {
        targetSelector.addGoal(1,new SpinGoal(){
            @Override
            public boolean canUse() {
                return getTarget() != null;
            }
            @Override
            public void tick() {
                Vec3 vec = getTarget().position().subtract(position());
                setDeltaMovement(vec.normalize().scale(0.8));
                lookAt(90);
            }
        });

    }

    @Override
    public void firstSpawn() {

    }
}
