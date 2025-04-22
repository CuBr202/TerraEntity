package org.confluence.terraentity.registries.track;

import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.registries.LazyVarMapCodecProvider;

import java.util.function.Supplier;

/**
 * 用于提供轨迹类型编解码器
 */
public class TrackTypeProvider  extends LazyVarMapCodecProvider<ITrackType> {

    public TrackTypeProvider(Supplier<MapCodec<? extends ITrackType>> mapCodecSupplier) {
        super(mapCodecSupplier);
    }
}
