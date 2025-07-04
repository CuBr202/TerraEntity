package org.confluence.terraentity.registries.hit_effect;

import com.github.edg_thexu.cafelib.data.codec.LazyVarMapCodecProvider;
import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

/**
 * 用于提供轨迹类型编解码器
 */
public class EffectStrategyProvider extends LazyVarMapCodecProvider<IEffectStrategy> {


    public EffectStrategyProvider(Supplier<MapCodec<? extends IEffectStrategy>> mapCodecSupplier) {
        super(mapCodecSupplier);
    }
}
