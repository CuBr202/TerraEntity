package org.confluence.terraentity.entity.proj.generation;

import com.mojang.serialization.MapCodec;

/**
 * 用于提供轨迹类型编解码器
 * @param codec
 */
public record GenerationProvider(MapCodec<? extends IGeneration> codec) {


}
