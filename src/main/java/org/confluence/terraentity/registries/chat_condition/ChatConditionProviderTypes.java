package org.confluence.terraentity.registries.chat_condition;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.npc.chat.IChatCondition;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.chat_condition.variant.*;

import java.util.function.Supplier;

public class ChatConditionProviderTypes {

    public static final DeferredRegister<ChatConditionProvider> TYPES = DeferredRegister.create(TERegistries.Keys.CHAT_CONDITION_PROVIDER, TerraEntity.MODID);
    public static final Supplier<IForgeRegistry<ChatConditionProvider>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    public static final Supplier<ChatConditionProvider> WEATHER = register("weather", ()->WeatherChatCondition.CODEC);
//    public static final Supplier<ChatConditionProvider> VANILLA = register("vanilla", ChatVanillaCondition.CODEC);
    public static final Supplier<ChatConditionProvider> MEMORY_STATES = register("memory_states", ()->MemoryStateCondition.CODEC);
    public static final Supplier<ChatConditionProvider> ITEM_IN_HAND = register("item_in_hand", ()->ItemInHandChatCondition.CODEC);
    public static final Supplier<ChatConditionProvider> NOT = register("not", ()->NotChatCondition.CODEC);


    private static Supplier<ChatConditionProvider> register(String name, Supplier<MapCodec<? extends IChatCondition>> codec) {
        return TYPES.register(name, ()->new ChatConditionProvider(codec));
    }


}
