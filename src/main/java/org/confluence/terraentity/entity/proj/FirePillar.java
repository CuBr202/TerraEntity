package org.confluence.terraentity.entity.proj;

import com.google.common.collect.Lists;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class FirePillar extends BaseProj<FirePillar> {

    public FirePillar(EntityType<? extends Projectile> pEntityType, Level pLevel, List<MobEffectInstance> pEffects) {
        super(pEntityType, pLevel, pEffects);
        this.penetration = 999;
    }

    public FirePillar(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        this(pEntityType, pLevel, Lists.newArrayList());
    }

    @Override
    public int getLifetime() {
        return 150;
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    // 取消渲染模型
    @Override
    public ResourceLocation getTexture(){
        return null;
    }


    @Override
    public void tick() {
        super.tick();
        if(this.level() instanceof ServerLevel serverLevel) {

            if (this.tickCount < 50) {
                serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.2f, this.getZ(),4, 0.0D, 0.5D, 0.0D,0);
            } else {
                float f = (this.tickCount - 50) / 150f;
                f = f * ( 1f - f);
                serverLevel.sendParticles(ParticleTypes.LAVA, this.getX(), this.getY() + 1f, this.getZ(),
                        10, 0, f*12,0,0);
                if(this.tickCount % 20 == 0){
                    this.doHurt();
                }
            }
        }

    }

    private void doHurt(){
        List<LivingEntity> list = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().setMaxY(this.getY() + 3.0D), e->this.getOwner() == null ||
                this.getOwner() instanceof LivingEntity living && living.canAttack(e));
        for (LivingEntity livingentity : list) {
            livingentity.hurt(this.damageSources().source(DamageTypes.LAVA, this.getOwner()), this.damage);
        }
    }
}
