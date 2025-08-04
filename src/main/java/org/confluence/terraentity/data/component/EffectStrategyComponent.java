package org.confluence.terraentity.data.component;

import com.github.edg_thexu.cafelib.api.datacomponent.IDataComponentType;
import com.github.edg_thexu.cafelib.data.codec.DataComponentProvider;
import com.mojang.serialization.Codec;

import net.minecraft.world.entity.LivingEntity;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.registries.hit_effect.EffectStrategy;
import org.confluence.terraentity.registries.hit_effect.IEffectStrategy;
import org.confluence.terraentity.registries.hit_effect.variant.PrefabEffect;

import java.util.List;
import java.util.function.Supplier;

/**
 * 命中效果的数据生成器组件
 *
 * @param effects 命中效果
 */
public record EffectStrategyComponent(List<IEffectStrategy> effects) implements IDataComponentType<EffectStrategyComponent> {

    public static final Supplier<Codec<EffectStrategyComponent>> CODEC = ()->
            IEffectStrategy.TYPED_CODEC.get().codec().listOf().xmap(EffectStrategyComponent::new, EffectStrategyComponent::effects);

    public void applyAll(LivingEntity owner, LivingEntity target){
        for (IEffectStrategy effect : effects) {

            effect.getEffect().accept(owner, target);
        }
    }

    public static EffectStrategyComponent of(IEffectStrategy effect) {
        return new EffectStrategyComponent(List.of(effect));
    }

    public static EffectStrategyComponent ofPrefab(String name, Supplier<EffectStrategy> effect) {
        return of(PrefabEffect.of(name, effect));
    }

    @Override
    public DataComponentProvider<EffectStrategyComponent> provider() {
        return TEDataComponentTypes.EFFECT_STRATEGY.get();
    }


//    public static EffectStrategyComponent of(EffectStrategy effect) {
//        return new EffectStrategyComponent(List.of(effect.getProvider()));
//    }


}
