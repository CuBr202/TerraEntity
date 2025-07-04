package org.confluence.terraentity.registries.track;

import com.github.edg_thexu.cafelib.data.codec.LazyVarMapCodecProvider;
import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

/**
 * 用于提供轨迹类型编解码器
 */
public class TrackTypeProvider  extends LazyVarMapCodecProvider<ITrackType> {

    public TrackTypeProvider(Supplier<MapCodec<? extends ITrackType>> mapCodecSupplier) {
        super(mapCodecSupplier);
    }
}
