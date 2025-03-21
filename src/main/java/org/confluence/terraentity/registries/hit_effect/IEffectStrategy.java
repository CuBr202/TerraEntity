package org.confluence.terraentity.registries.hit_effect;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.registries.TERegistries;

import java.util.function.BiConsumer;

/**
 * <h1>自定义效果</h1>
 */
public interface IEffectStrategy {

    /**
     * 应用效果
     */
    BiConsumer<LivingEntity, LivingEntity> getEffect();


    /**
     * 获取编解码器
     * @return 编解码器
     */
    DeferredHolder<EffectStrategyProvider,EffectStrategyProvider> getCodec();

    Codec<IEffectStrategy> TYPED_CODEC = TERegistries.EffectStrategyProviders.REGISTRY
            .byNameCodec()
            .dispatch(p->p.getCodec().get(), EffectStrategyProvider::codec);
}
