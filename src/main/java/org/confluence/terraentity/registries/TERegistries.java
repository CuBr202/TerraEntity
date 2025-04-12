package org.confluence.terraentity.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.init.TEEffectStrategies;
import org.confluence.terraentity.registries.datacomponent.DataComponentProvider;
import org.confluence.terraentity.registries.datacomponent.IDataComponentType;
import org.confluence.terraentity.registries.generation.GenerationProvider;
import org.confluence.terraentity.registries.generation.GenerationProviderTypes;
import org.confluence.terraentity.registries.hit_effect.EffectStrategy;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProvider;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProviderTypes;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade.TradeProviderTypes;
import org.confluence.terraentity.registries.track.TrackTypeProvider;
import org.confluence.terraentity.registries.track.TrackTypeProviderTypes;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

public class TERegistries {

    public static void register(IEventBus bus) {
        EffectStrategyProviderTypes.TYPES.register(bus);
        GenerationProviderTypes.TYPES.register(bus);
        TrackTypeProviderTypes.TYPES.register(bus);
        TEEffectStrategies.EFFECT_STRATEGY.register(bus);
        TradeProviderTypes.TYPES.register(bus);
        TEDataComponentTypes.register(bus);

    }

    public static class DataComponentProviders{
        public static final ResourceKey<Registry<DataComponentProvider<? extends IDataComponentType<?>>>> KEY = createRegistryKey(TerraEntity.space("data_component"));

    }

    /**
     * 跟踪类型编解码器注册表
     */
    public static class GenerationProviders {
        public static final ResourceKey<Registry<GenerationProvider>> KEY = createRegistryKey(TerraEntity.space("generation_provider"));

    }

    /**
     * 命中效果注册类型表
     */
    public static class EffectStrategyProviders{
        public static final ResourceKey<Registry<EffectStrategyProvider>> KEY = createRegistryKey(TerraEntity.space("effect_strategy_type"));

    }

    /**
     * 命中效果注册表
     */
    public static class EffectStrategies{
        public static final ResourceKey<Registry<EffectStrategy>> KEY = createRegistryKey(TerraEntity.space("effect_strategy"));

    }

    /**
     * 跟踪类型编解码器注册表
     */
    public static class TrackTypeProviders{
        public static final ResourceKey<Registry<TrackTypeProvider>> KEY = createRegistryKey(TerraEntity.space("track_type_provider"));

    }

    public static class TradeProviders{
        public static final ResourceKey<Registry<TradeProvider>> KEY = createRegistryKey(TerraEntity.space("trade_provider"));

    }
}
