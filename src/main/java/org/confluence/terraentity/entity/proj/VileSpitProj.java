package org.confluence.terraentity.entity.proj;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.confluence.terraentity.entity.boss.EaterOfWorlds;
import org.confluence.terraentity.entity.boss.EaterOfWorldsSegment;
import org.confluence.terraentity.init.TEParticles;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 世吞体节口水
 */
public class VileSpitProj extends LineProj {

    public ParticleOptions particleOptions = TEParticles.SPIT_GLOW.get();

    public VileSpitProj(EntityType<? extends LineProj> pEntityType, Level pLevel, MobEffectInstance effect) {
        super(pEntityType, pLevel, effect);
    }

    public VileSpitProj(EntityType<? extends LineProj> pEntityType, Level pLevel, List<MobEffectInstance> effects) {
        super(pEntityType, pLevel, effects);
    }

    public VileSpitProj(EntityType<? extends LineProj> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick(){
        super.tick();
        if(level().isClientSide) {
            for (int i = 0; i < 3; i++) {
                float random = (this.random.nextFloat() - 0.5f) * 0.5F;
                float random2 = (this.random.nextFloat() - 0.5f) * 0.5F;
                float random3 = (this.random.nextFloat() - 0.5f) * 0.5F;
                this.level().addParticle(particleOptions, this.getX() + random, this.getY() + random2, this.getZ() + random3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    // 取消渲染模型
    @Override
    public ResourceLocation getTexture(){
        return null;
    }

    public VileSpitProj setParticleOptions(ParticleOptions particleOptions) {
        this.particleOptions = particleOptions;
        return this;
    }


    @Override
    protected boolean canHitEntity(@NotNull Entity target) {
        return super.canHitEntity( target) && !(target instanceof EaterOfWorlds) && !(target instanceof EaterOfWorldsSegment);
    }
}
