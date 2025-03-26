package org.confluence.terraentity.registries.track;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.registries.TERegistries;

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

    MapCodec<ITrackType> TYPED_CODEC = TERegistries.TrackTypeProviders.REGISTRY.get()
            .getCodec()
            .dispatchMap(ITrackType::getCodec, c->c.codec().codec());

}
