package org.confluence.terraentity.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.mood.MoodInfo;
import org.confluence.terraentity.init.TEEffectStrategies;
import org.confluence.terraentity.registries.generation.GenerationProviderTypes;
import org.confluence.terraentity.registries.hit_effect.EffectStrategy;
import org.confluence.terraentity.registries.generation.GenerationProvider;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProvider;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProviderTypes;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade.TradeProviderTypes;
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
        event.register(TradeProviders.REGISTRY);
//        event.register(MoodInfoRegistry.REGISTRY);
    }

    public static void register(IEventBus bus) {
        EffectStrategyProviderTypes.TYPES.register(bus);
        GenerationProviderTypes.TYPES.register(bus);
        TrackTypeProviderTypes.TYPES.register(bus);
        TradeProviderTypes.TYPES.register(bus);
        TEEffectStrategies.EFFECT_STRATEGY.register(bus);
//        org.confluence.terraentity.entity.npc.mood.MoodInfos.TYPES.register(bus);

    }

    /**
     * 跟踪类型编解码器注册表
     */
    public static class GenerationProviders{
        public static final ResourceKey<Registry<GenerationProvider>> KEY = createRegistryKey(TerraEntity.space("generation_provider"));
        public static final Registry<GenerationProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    /**
     * 命中效果注册类型表
     */
    public static class EffectStrategyProviders{
        public static final ResourceKey<Registry<EffectStrategyProvider>> KEY = createRegistryKey(TerraEntity.space("effect_strategy_type"));
        public static final Registry<EffectStrategyProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    /**
     * 命中效果注册表
     */
    public static class EffectStrategies{
        public static final ResourceKey<Registry<EffectStrategy>> KEY = createRegistryKey(TerraEntity.space("effect_strategy"));
        public static final Registry<EffectStrategy> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    /**
     * 跟踪类型编解码器注册表
     */
    public static class TrackTypeProviders{
        public static final ResourceKey<Registry<TrackTypeProvider>> KEY = createRegistryKey(TerraEntity.space("track_type_provider"));
        public static final Registry<TrackTypeProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }


    /**
     * NPC交易注册表
     */
    public static class TradeProviders{
        public static final ResourceKey<Registry<TradeProvider>> KEY = createRegistryKey(TerraEntity.space("trade_provider"));
        public static final Registry<TradeProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    /**
     * NPC心情注册表
     */
    public static class MoodInfoRegistry {
//        public static final ResourceKey<Registry<MoodInfo>> KEY = createRegistryKey(TerraEntity.space("mood_info"));
//        public static final Registry<MoodInfo> REGISTRY = new RegistryBuilder<>(KEY).create();
    }
}
