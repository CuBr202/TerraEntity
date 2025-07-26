package org.confluence.terraentity.registries.chat_condition;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.npc.chat.IChatCondition;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.chat_condition.variant.ChatVanillaCondition;
import org.confluence.terraentity.registries.chat_condition.variant.MemoryStateCondition;
import org.confluence.terraentity.registries.chat_condition.variant.WeatherChatCondition;

import java.util.function.Supplier;

public class ChatConditionProviderTypes {

    public static final DeferredRegister<ChatConditionProvider> TYPES = DeferredRegister.create(TERegistries.ChatConditionProviderRegistry.REGISTRY, TerraEntity.MODID);

    public static final Supplier<ChatConditionProvider> WEATHER = register("weather", WeatherChatCondition.CODEC);
    public static final Supplier<ChatConditionProvider> VANILLA = register("vanilla", ChatVanillaCondition.CODEC);
    public static final Supplier<ChatConditionProvider> MEMORY_STATES = register("memory_states", MemoryStateCondition.CODEC);


    private static Supplier<ChatConditionProvider> register(String name, MapCodec<? extends IChatCondition> codec) {
        return TYPES.register(name, ()->new ChatConditionProvider(codec));
    }


}
