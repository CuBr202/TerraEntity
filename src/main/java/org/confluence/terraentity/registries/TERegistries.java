package org.confluence.terraentity.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.chat.ChatElementProvider;
import org.confluence.terraentity.init.TEEffectStrategies;
import org.confluence.terraentity.registries.chat.ChatProviderTypes;
import org.confluence.terraentity.registries.chat_condition.ChatConditionProvider;
import org.confluence.terraentity.registries.chat_condition.ChatConditionProviderTypes;
import org.confluence.terraentity.registries.chester.ChesterConditionalType;
import org.confluence.terraentity.registries.chester.ChesterConditionalTypes;
import org.confluence.terraentity.registries.chester.ChesterType;
import org.confluence.terraentity.registries.chester.ChesterTypes;
import org.confluence.terraentity.registries.generation.GenerationProviderTypes;
import org.confluence.terraentity.registries.hit_effect.EffectStrategy;
import org.confluence.terraentity.registries.generation.GenerationProvider;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProvider;
import org.confluence.terraentity.registries.hit_effect.EffectStrategyProviderTypes;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade.TradeProviderTypes;
import org.confluence.terraentity.registries.npc_trade_list.TradeGeneratorProvider;
import org.confluence.terraentity.registries.npc_trade_list.TradeGeneratorProviderTypes;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProvider;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProviderTypes;
import org.confluence.terraentity.registries.npc_trade_modify.TradeModifierProvider;
import org.confluence.terraentity.registries.npc_trade_modify.TradeModifierProviderTypes;
import org.confluence.terraentity.registries.npc_trade_task.TradeTaskProvider;
import org.confluence.terraentity.registries.npc_trade_task.TradeTaskProviderTypes;
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
        event.register(TradeTaskProviders.REGISTRY);
        event.register(TradeLockProviders.REGISTRY);
        event.register(TradeGeneratorProviders.REGISTRY);
        event.register(ChesterTypesProviders.REGISTRY);
        event.register(ChesterConditionalTypesProviders.REGISTRY);
        event.register(TradeModifierProviderRegistry.REGISTRY);
        event.register(ChatElementProviderRegistry.REGISTRY);
        event.register(ChatConditionProviderRegistry.REGISTRY);
//        event.register(MoodInfoRegistry.REGISTRY);
    }

    public static void register(IEventBus bus) {
        EffectStrategyProviderTypes.TYPES.register(bus);
        GenerationProviderTypes.TYPES.register(bus);
        TrackTypeProviderTypes.TYPES.register(bus);
        TradeProviderTypes.TYPES.register(bus);
        TEEffectStrategies.EFFECT_STRATEGY.register(bus);
        TradeTaskProviderTypes.TYPES.register(bus);
        TradeLockProviderTypes.TYPES.register(bus);
        TradeGeneratorProviderTypes.TYPES.register(bus);
        ChesterTypes.TYPES.register(bus);
        ChesterConditionalTypes.TYPES.register(bus);
        TradeModifierProviderTypes.TYPES.register(bus);
        ChatProviderTypes.TYPES.register(bus);
        ChatConditionProviderTypes.TYPES.register(bus);
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
     * NPC交易任务注册表
     */
    public static class TradeTaskProviders{
        public static final ResourceKey<Registry<TradeTaskProvider>> KEY = createRegistryKey(TerraEntity.space("trade_task_provider"));
        public static final Registry<TradeTaskProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }
    /**
     * NPC心情注册表
     */
    public static class TradeLockProviders {
        public static final ResourceKey<Registry<TradeLockProvider>> KEY = createRegistryKey(TerraEntity.space("trade_lock_provider"));
        public static final Registry<TradeLockProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    /**
     * NPC交易列表注册表
     */
    public static class TradeGeneratorProviders {
        public static final ResourceKey<Registry<TradeGeneratorProvider>> KEY = createRegistryKey(TerraEntity.space("trade_generator_provider"));
        public static final Registry<TradeGeneratorProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    /**
     * 切斯特全局存储器注册表，用来给切斯特添加可以打开的全局菜单
     */
    public static class ChesterTypesProviders {
        public static final ResourceKey<Registry<ChesterType>> KEY = createRegistryKey(TerraEntity.space("chester_type"));
        public static final Registry<ChesterType> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    /**
     * 切斯特方块容器记录器，给切斯特添加可以打开的方块容器
     */
    public static class ChesterConditionalTypesProviders {
        public static final ResourceKey<Registry<ChesterConditionalType>> KEY = createRegistryKey(TerraEntity.space("chester_conditional_type"));
        public static final Registry<ChesterConditionalType> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    public static class TradeModifierProviderRegistry {
        public static final ResourceKey<Registry<TradeModifierProvider>> KEY = createRegistryKey(TerraEntity.space("trade_modifier_provider"));
        public static final Registry<TradeModifierProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    public static class ChatElementProviderRegistry {
        public static final ResourceKey<Registry<ChatElementProvider>> KEY = createRegistryKey(TerraEntity.space("chat_element"));
        public static final Registry<ChatElementProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    public static class ChatConditionProviderRegistry {
        public static final ResourceKey<Registry<ChatConditionProvider>> KEY = createRegistryKey(TerraEntity.space("chat_condition"));
        public static final Registry<ChatConditionProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }
}
