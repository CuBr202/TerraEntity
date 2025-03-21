package org.confluence.terraentity.registries.hit_effect.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.init.TEEffects;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.hit_effect.EffectStrategy;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProvider;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProviderTypes;
import org.confluence.terraentity.registries.hit_effect.IEffectStrategy;
import org.confluence.terraentity.utils.TEUtils;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RandomWeightEffect implements IEffectStrategy {
    Supplier<Map<EffectStrategy, Float>> effectMap;
    Map<EffectStrategy, Float> cache;
    public Map<EffectStrategy, Float> getEffectMap(){
        return effectMap.get();
    }
    public static MapCodec<RandomWeightEffect> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.unboundedMap(EffectStrategy.CODEC.xmap(i->i,i->i), Codec.FLOAT).fieldOf("effectMap").forGetter(RandomWeightEffect::getEffectMap)
    ).apply(instance, map->new RandomWeightEffect((() -> map))));

    public RandomWeightEffect(Supplier<Map<EffectStrategy, Float>> effectMap){
        this.effectMap = effectMap;
    }

    @Override
    public BiConsumer<LivingEntity, LivingEntity> getEffect() {
        if(cache == null){
            cache = effectMap.get();
        }
        return TEUtils.getRandomByWeight(cache).getEffect();
    }

    @Override
    public DeferredHolder<EffectStrategyProvider, EffectStrategyProvider> getCodec() {
        return EffectStrategyProviderTypes.RANDOM_EFFECT_PROVIDER;
    }
}
