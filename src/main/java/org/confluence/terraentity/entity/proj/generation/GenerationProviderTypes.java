package org.confluence.terraentity.entity.proj.generation;

import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.GenerationProviders;

import java.util.function.Supplier;

/**
 * 注册追踪编解码器的类型
 */
public class GenerationProviderTypes {
    public static final GenerationProviders GENERATION_PROVIDERS = GenerationProviders.create(TerraEntity.MODID);

    public static final Supplier<GenerationProvider> FORWARD_GENERATION = register("forward", ForwardGeneration.CODEC);
    public static final Supplier<GenerationProvider> ABOVE_FALLEN = register("above_fallen", AboveFallenGeneration.CODEC);
    public static final Supplier<GenerationProvider> STILL = register("still", StillGeneration.CODEC);


    private static Supplier<GenerationProvider> register(String name, MapCodec<? extends IGeneration> codec) {
        return GENERATION_PROVIDERS.register(name, ()->new GenerationProvider(codec));
    }
}
