package org.confluence.terraentity.registries.track;

import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.track.variant.BasisTrack;
import org.confluence.terraentity.registries.track.variant.SimpleTrack;


import java.util.function.Supplier;

/**
 * 注册追踪编解码器的类型
 */
public class TrackTypeProviderTypes {

    public static final Supplier<TrackTypeProvider> SIMPLE_TRACK_TYPE = register("simple_track_type", SimpleTrack.CODEC);
    public static final Supplier<TrackTypeProvider> BASIS_TRACK_TYPE = register("basis_track_type", BasisTrack.CODEC);


    private static Supplier<TrackTypeProvider> register(String name, MapCodec<? extends ITrackType> codec) {
        return TERegistries.TrackTypeProviders.TYPES.register(name, ()->new TrackTypeProvider(codec));
    }
}
