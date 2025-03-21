package org.confluence.terraentity.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.generation.GenerationProviderTypes;
import org.confluence.terraentity.registries.hit_effect.EffectStrategy;
import org.confluence.terraentity.registries.generation.GenerationProvider;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProvider;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProviderTypes;
import org.confluence.terraentity.registries.track.TrackTypeProvider;
import org.confluence.terraentity.registries.track.TrackTypeProviderTypes;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

public class TERegistries {

    // 注册监听
    public static void newRegistry(NewRegistryEvent event) {
        event.register(EffectStrategyProviders.REGISTRY);
        event.register(TrackTypeProviders.REGISTRY);
        event.register(GenerationProviders.REGISTRY);
        event.register(EffectStrategies.REGISTRY);
    }

    public static void register(IEventBus bus) {
        EffectStrategyProviderTypes.TYPES.register(bus);
        GenerationProviderTypes.TYPES.register(bus);
        TrackTypeProviderTypes.TYPES.register(bus);

    }

    /**
     * 跟踪类型编解码器注册表
     */
    public static class GenerationProviders extends DeferredRegister<GenerationProvider> {
        public static final ResourceKey<Registry<GenerationProvider>> KEY = createRegistryKey(TerraEntity.space("generation_provider"));
        public static final Registry<GenerationProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
        public static GenerationProviders create(String mod_id) {
            return new GenerationProviders(mod_id);
        }
        protected GenerationProviders(String namespace) {
            super(KEY, namespace);
        }
    }

    /**
     * 命中效果注册类型表
     */
    public static class EffectStrategyProviders extends DeferredRegister<EffectStrategyProvider> {
        public static final ResourceKey<Registry<EffectStrategyProvider>> KEY = createRegistryKey(TerraEntity.asResource("effect_strategy_type"));
        public static final Registry<EffectStrategyProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
        public static EffectStrategyProviders create(String mod_id) {
            return new EffectStrategyProviders(mod_id);
        }
        protected EffectStrategyProviders(String namespace) {
            super(KEY, namespace);
        }
    }

    /**
     * 命中效果注册表
     */
    public static class EffectStrategies extends DeferredRegister<EffectStrategy> {
        public static final ResourceKey<Registry<EffectStrategy>> KEY = createRegistryKey(TerraEntity.asResource("effect_strategy"));
        public static final Registry<EffectStrategy> REGISTRY = new RegistryBuilder<>(KEY).create();
        public static EffectStrategies create(String mod_id) {
            return new EffectStrategies(mod_id);
        }
        protected EffectStrategies(String namespace) {
            super(KEY, namespace);
        }
    }

    /**
     * 跟踪类型编解码器注册表
     */
    public static class TrackTypeProviders extends DeferredRegister<TrackTypeProvider> {
        public static final ResourceKey<Registry<TrackTypeProvider>> KEY = createRegistryKey(TerraEntity.space("track_type_provider"));
        public static final Registry<TrackTypeProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
        public static TrackTypeProviders create(String mod_id) {
            return new TrackTypeProviders(mod_id);
        }
        protected TrackTypeProviders(String namespace) {
            super(KEY, namespace);
        }
    }
}
