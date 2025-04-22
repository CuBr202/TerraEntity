package org.confluence.terraentity.registries.hit_effect;

import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.registries.LazyVarMapCodecProvider;

import java.util.function.Supplier;

/**
 * 用于提供轨迹类型编解码器
 */
public class EffectStrategyProvider extends LazyVarMapCodecProvider<IEffectStrategy> {


    public EffectStrategyProvider(Supplier<MapCodec<? extends IEffectStrategy>> mapCodecSupplier) {
        super(mapCodecSupplier);
    }
}
