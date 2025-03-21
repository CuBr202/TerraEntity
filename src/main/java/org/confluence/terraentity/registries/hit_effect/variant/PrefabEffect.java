package org.confluence.terraentity.registries.hit_effect.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.registries.hit_effect.EffectStrategy;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProvider;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProviderTypes;
import org.confluence.terraentity.registries.hit_effect.IEffectStrategy;

import java.util.function.BiConsumer;

/**
 * 用于引用预制的效果策略的数据生成器
 */
public class PrefabEffect implements IEffectStrategy {

    private final EffectStrategy effect;
    BiConsumer<LivingEntity, LivingEntity> effectConsumer;

    public PrefabEffect(EffectStrategy effect, BiConsumer<LivingEntity, LivingEntity> effectConsumer) {
        this.effect = effect;
        this.effectConsumer = effectConsumer;
    }

    public static MapCodec<PrefabEffect> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            EffectStrategy.CODEC.fieldOf("effect").forGetter(PrefabEffect::getEffectStrategy)
    ).apply(instance, (effect)->new PrefabEffect(effect, effect.getEffect())));


    @Override
    public BiConsumer<LivingEntity, LivingEntity> getEffect() {
        return effectConsumer;
    }

    @Override
    public DeferredHolder<EffectStrategyProvider, EffectStrategyProvider> getCodec() {
        return EffectStrategyProviderTypes.PREFAB_EFFECT_PROVIDER;
    }

    public EffectStrategy getEffectStrategy() {
        return effect;
    }
}
