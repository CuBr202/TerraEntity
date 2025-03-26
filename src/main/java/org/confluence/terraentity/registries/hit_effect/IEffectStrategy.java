package org.confluence.terraentity.registries.hit_effect;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terraentity.TerraEntity;

import org.confluence.terraentity.data.component.EffectStrategyComponent;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * <h1> 自定义效果 </h1>
 * <p> 定义不同的编解码器，方便数据生成效果的参数
 */
public interface IEffectStrategy {

    /**
     * 具体的应用效果
     */
    BiConsumer<LivingEntity, LivingEntity> getEffect();

    /**
     * 翻译键后缀
     */
    String getName();

    default String getTranslationKey() {
        return TerraEntity.MODID + ".effect.strategy." + getName();
//        return "effect.strategy." + TERegistries.EffectStrategyProviders.REGISTRY.getKey(getCodec().get());
    }

    default MutableComponent getDescription() {
        return Component.translatable(getTranslationKey());
    }

    /**
     * 效果描述
     */
    static void appendDescription(List<Component> tooltipComponents, List<? extends IEffectStrategy> effectStrategy, Component title) {
        int size = effectStrategy.size();
        if(size == 0) return;
        tooltipComponents.add(title);
        for(int i = 0; i < size; i++) {
            IEffectStrategy effect = effectStrategy.get(i);
            tooltipComponents.add(Component.literal(" - ").append(effect.getDescription()).withStyle(style -> style.withColor(0xFF00FF)));
        }
    }

    /**
     * 多组件复合效果描述
     */
    static void appendDescriptions(List<Component> tooltipComponents, List<EffectStrategyComponent> components, Component title) {
        List<IEffectStrategy> effectStrategy = components.stream().flatMap(e->e.effects().stream()).toList();
        int size = effectStrategy.size();
        if(size == 0) return;
        tooltipComponents.add(title);
        for(int i = 0; i < size; i++) {
            IEffectStrategy effect = effectStrategy.get(i);
            tooltipComponents.add(Component.literal(" - ").append(effect.getDescription()).withStyle(style -> style.withColor(0xFF00FF)));
        }
    }

    /**
     * 获取编解码器
     * @return 编解码器
     */
    Supplier<EffectStrategyProvider> getCodec();

    Supplier<MapCodec<IEffectStrategy>> TYPED_CODEC = ()->EffectStrategyProviderTypes.REGISTRY.get()
            .getCodec()
            .dispatchMap(c->c.getCodec().get(), c->c.codec().codec());
}
