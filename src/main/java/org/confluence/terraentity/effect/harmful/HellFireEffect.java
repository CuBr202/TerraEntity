package org.confluence.terraentity.effect.harmful;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.level.CustomExplodeCalculator;
import org.confluence.terraentity.mixed.IMobEffectExtension;

/**
 * 狱炎: 缓慢损失生命 每秒损失15点生命 停止生命再生
 */
public class HellFireEffect extends MobEffect implements IMobEffectExtension {
    public HellFireEffect() {
        super(MobEffectCategory.HARMFUL, 0xAB1122);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {

        living.hurt(TETags.DamageTypes.of(living.level(), DamageTypes.LAVA), 2.0F * (amplifier + 1));

    }

    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        livingEntity.setRemainingFireTicks(1);
        livingEntity.level().explode(
                livingEntity,
                livingEntity.level().damageSources().explosion(livingEntity, livingEntity),
                new CustomExplodeCalculator() {
                    @Override
                    public float getEntityDamageAmount(Explosion explosion, Entity entity, double original) {
                        if(!( entity instanceof Enemy || entity instanceof AbstractTerraBossBase<?>)) return 0;
                        return 3 + amplifier*3;
                    }

                    @Override
                    public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
                        return false;
                    }
                } ,
                livingEntity.getX(), livingEntity.getY(0.0625),livingEntity.getZ(),
                1, true, Level.ExplosionInteraction.MOB
        );

    }

    @Override
    public boolean isDurationEffectTick(int duration, int pAmplifier) {
        return duration % 20 == 0;
    }
}
