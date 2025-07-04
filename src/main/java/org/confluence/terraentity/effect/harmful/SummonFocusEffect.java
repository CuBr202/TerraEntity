package org.confluence.terraentity.effect.harmful;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/**
 * 召唤物标记伤害
 */
public class SummonFocusEffect extends MobEffect {
    public SummonFocusEffect() {
        super(MobEffectCategory.HARMFUL, 0xABAB11);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {


    }
}
