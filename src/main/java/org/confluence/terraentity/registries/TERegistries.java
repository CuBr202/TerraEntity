package org.confluence.terraentity.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.datacomponent.DataComponentProvider;
import org.confluence.terraentity.registries.datacomponent.IDataComponentType;
import org.confluence.terraentity.registries.generation.GenerationProvider;
import org.confluence.terraentity.registries.hit_effect.EffectStrategy;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProvider;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProviderTypes;
import org.confluence.terraentity.registries.track.TrackTypeProvider;

import java.util.Optional;
import java.util.function.Supplier;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

public class TERegistries {

    // 注册监听
    public static void newRegistry(NewRegistryEvent event) {
//        GenerationProviders.REGISTER.register(bus);
//        EffectStrategyProviders.REGISTER.register(bus);
//        EffectStrategies.REGISTER.register(bus);
//        TrackTypeProviders.REGISTER.register(bus);
//        DataComponentProviders.REGISTER.register(bus);

    }

    public static void register(IEventBus bus) {

//        GenerationProviderTypes.TYPES.makeRegistry(RegistryBuilder::new);
//        TrackTypeProviderTypes.TYPES.makeRegistry(RegistryBuilder::new);
//        DataComponentProviders.TYPES.register(bus);
        EffectStrategies.TYPES.register(bus);
        EffectStrategyProviderTypes.TYPES.register(bus);
        GenerationProviders.TYPES.register(bus);
        TrackTypeProviders.TYPES.register(bus);
        DataComponentProviders.TYPES.register(bus);


//        TEDataComponentTypes.TYPES.register(bus);

    }

    /**
     * 跟踪类型编解码器注册表
     */
    public static class GenerationProviders {
        public static final ResourceKey<Registry<GenerationProvider>> KEY = createRegistryKey(TerraEntity.space("generation_provider"));
        public static final DeferredRegister<GenerationProvider> TYPES =  DeferredRegister.create(KEY, TerraEntity.MODID);
        public static final Supplier<IForgeRegistry<GenerationProvider>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);
    }

    /**
     * 命中效果注册类型表
     */
    public static class EffectStrategyProviders{
        public static final ResourceKey<Registry<EffectStrategyProvider>> KEY = createRegistryKey(TerraEntity.asResource("effect_strategy_type"));

    }

    /**
     * 命中效果注册表
     */
    public static class EffectStrategies{
        public static final ResourceKey<Registry<EffectStrategy>> KEY = createRegistryKey(TerraEntity.asResource("effect_strategy"));
        public static final DeferredRegister<EffectStrategy> TYPES =  DeferredRegister.create(KEY, TerraEntity.MODID);
        public static final Supplier<IForgeRegistry<EffectStrategy>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    }

    /**
     * 跟踪类型编解码器注册表
     */
    public static class TrackTypeProviders{
        public static final ResourceKey<Registry<TrackTypeProvider>> KEY = createRegistryKey(TerraEntity.space("track_type_provider"));
        public static final DeferredRegister<TrackTypeProvider> TYPES =  DeferredRegister.create(KEY, TerraEntity.MODID);
        public static final Supplier<IForgeRegistry<TrackTypeProvider>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    }

    public static class DataComponentProviders{
        public static final ResourceKey<Registry<DataComponentProvider<? extends IDataComponentType<?>>>> KEY = createRegistryKey(TerraEntity.space("data_component"));
        public static DeferredRegister<DataComponentProvider<? extends IDataComponentType<?>>> TYPES =  DeferredRegister.create(KEY, TerraEntity.MODID);
//        public static final Registry<DataComponentProvider<? extends IDataComponentType<?> >> REGISTRY = new RegistryBuilder<>(KEY).crea
        public static Supplier<IForgeRegistry<DataComponentProvider<? extends IDataComponentType<?>>>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);
//        public static Supplier<IForgeRegistry<DataComponentProvider<? extends IDataComponentType<?>>>> REGISTRY;


    }
}
