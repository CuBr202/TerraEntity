package org.confluence.terraentity.entity.proj;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DemonScytheProj extends LineProj {
    public DemonScytheProj(EntityType<? extends LineProj> pEntityType, Level pLevel, MobEffectInstance effect) {
        super(pEntityType, pLevel, effect);

    }


    @Override
    protected Vec3 warpSpeed(Vec3 speed) {
        return super.warpSpeed(speed).scale(Math.max(Math.min(1.0, this.tickCount / 40.0 - 0.5), 0.1f));
    }


    @Override
    public void onAddedToLevel(){
        super.onAddedToLevel();
    }
}
