package org.confluence.terraentity.init;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.chat.NPCChat;
import org.confluence.terraentity.entity.npc.house.House;
import org.confluence.terraentity.entity.npc.mood.NPCMood;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.entity.npc.trade.TradeParams;
import org.confluence.terraentity.entity.util.KeyframeAnimationCounter;

import java.util.List;
import java.util.function.Supplier;


public final class TEEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, TerraEntity.MODID);

    public static final Supplier<EntityDataSerializer<NPCTradeManager>> NPC_TRADES_SERIALIZER = SERIALIZERS.register(NPCTradeManager.Loader.KEY, () -> EntityDataSerializer.forValueType(NPCTradeManager.STREAM_CODEC));
    public static final Supplier<EntityDataSerializer<House>> NPC_HOUSE_SERIALIZER = SERIALIZERS.register(House.KEY, () -> EntityDataSerializer.forValueType(House.STREAM_CODEC));
    public static final Supplier<EntityDataSerializer<NPCMood>> NPC_MOOD_SERIALIZER = SERIALIZERS.register(NPCMood.KEY, () -> EntityDataSerializer.forValueType(NPCMood.STREAM_CODEC));
    public static final Supplier<EntityDataSerializer<TradeParams>> NPC_TRADE_PARAMS_SERIALIZER = SERIALIZERS.register(TradeParams.KEY, () -> EntityDataSerializer.forValueType(TradeParams.STREAM_CODEC));

    public static final Supplier<EntityDataSerializer<KeyframeAnimationCounter>> KEYFRAME_ANIMATION_SERIALIZER = SERIALIZERS.register("keyframe_animation", () -> EntityDataSerializer.forValueType(KeyframeAnimationCounter.STREAM_CODEC));
    public static final Supplier<EntityDataSerializer<NPCChat>> NPC_CHAT_SERIALIZER = SERIALIZERS.register("npc_chat", () -> EntityDataSerializer.forValueType(NPCChat.STREAM_CODEC));
    public static final Supplier<EntityDataSerializer<List<Vec3>>> VEC3_LIST_SERIALIZER = SERIALIZERS.register("vec3_list", () -> EntityDataSerializer.forValueType(ByteBufCodecs.fromCodec(Vec3.CODEC.listOf())));


}
