package org.confluence.terraentity.api.entity;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.registries.track.TrackTypeProvider;
import org.confluence.terraentity.registries.track.TrackTypeProviderTypes;

/**
 * <h1>跟踪方式</h1>
 */
public interface ITrackType {

    /**
     * 获取计算后的速度
     * @return 速度
     */
    Vec3 calDeltaMovement(Vec3 currentDir, Vec3 targetDir, double trackAngle);

    double trackAngle();

    String getName();

    /**
     * 获取编解码器
     * @return 编解码器
     */
    TrackTypeProvider getCodec();

    MapCodec<ITrackType> TYPED_CODEC = TrackTypeProviderTypes.REGISTRY.get()
            .getCodec()
            .dispatchMap(ITrackType::getCodec, c->c.codec().codec());

}
