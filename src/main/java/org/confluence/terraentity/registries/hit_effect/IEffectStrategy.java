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
 * <h1>自定义效果</h1>
 */
public interface IEffectStrategy {

    /**
     * 应用效果
     */
    BiConsumer<LivingEntity, LivingEntity> getEffect();


    String getName();

    default String getTranslationKey() {
        try {
            return TerraEntity.MODID + ".effect.strategy." + getName();
        }catch (Exception e) {
            return TerraEntity.MODID + ".effect.strategy.unknown";
        }
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
     * 获取编解码器
     * @return 编解码器
     */
    DeferredHolder<EffectStrategyProvider,EffectStrategyProvider> getCodec();

    Codec<IEffectStrategy> TYPED_CODEC = TERegistries.EffectStrategyProviders.REGISTRY
            .byNameCodec()
            .dispatch(p->p.getCodec().get(), EffectStrategyProvider::codec);
}
