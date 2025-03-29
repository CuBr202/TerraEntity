package org.confluence.terraentity.registries.hit_effect;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.component.EffectStrategyComponent;
import org.confluence.terraentity.registries.TERegistries;

import java.util.List;
import java.util.function.BiConsumer;

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
            tooltipComponents.add(Component.literal(" - ").append(effect.getDescription()).withColor(0xFF00FF));
        }
    }
    /**
     * 效果描述
     */
    static void appendDescription(List<Component> tooltipComponents, List<? extends IEffectStrategy> effectStrategy, Component title, int textColor) {
        int size = effectStrategy.size();
        if(size == 0) return;
        tooltipComponents.add(title);
        for(int i = 0; i < size; i++) {
            IEffectStrategy effect = effectStrategy.get(i);
            tooltipComponents.add(Component.literal(" - ").append(effect.getDescription()).withColor(textColor));
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
            tooltipComponents.add(Component.literal(" - ").append(effect.getDescription()).withColor(0xFF00FF));
        }
    }

    /**
     * 获取编解码器
     * @return 编解码器
     */
    DeferredHolder<EffectStrategyProvider,EffectStrategyProvider> getCodec();

    Codec<IEffectStrategy> TYPED_CODEC = TERegistries.EffectStrategyProviders.REGISTRY
            .byNameCodec()
            .dispatch(p->p.getCodec().get(), EffectStrategyProvider::codec);
}
