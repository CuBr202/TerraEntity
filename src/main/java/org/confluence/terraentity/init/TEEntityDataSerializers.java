package org.confluence.terraentity.init;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.house.House;
import org.confluence.terraentity.entity.npc.mood.NPCMood;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.entity.npc.trade.TradeParams;
import org.confluence.terraentity.entity.util.KeyframeAnimationCounter;

import java.util.function.Supplier;


public final class TEEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, TerraEntity.MODID);

    public static final Supplier<EntityDataSerializer<NPCTradeManager>> NPC_TRADES_SERIALIZER = SERIALIZERS.register(NPCTradeManager.KEY, () -> EntityDataSerializer.simple(NPCTradeManager.WRITER, NPCTradeManager.READER));
    public static final Supplier<EntityDataSerializer<House>> NPC_HOUSE_SERIALIZER = SERIALIZERS.register(House.KEY, () -> EntityDataSerializer.simple(House.WRITER, House.READER));
    public static final Supplier<EntityDataSerializer<NPCMood>> NPC_MOOD_SERIALIZER = SERIALIZERS.register(NPCMood.KEY, () -> EntityDataSerializer.simple(NPCMood.WRITER, NPCMood.READER));
    public static final Supplier<EntityDataSerializer<TradeParams>> NPC_TRADE_PARAMS_SERIALIZER = SERIALIZERS.register(TradeParams.KEY, () -> EntityDataSerializer.simple(TradeParams.WRITER, TradeParams.READER));

    public static final Supplier<EntityDataSerializer<KeyframeAnimationCounter>> KEYFRAME_ANIMATION_SERIALIZER = SERIALIZERS.register("keyframe_animation", () -> EntityDataSerializer.simple(KeyframeAnimationCounter.WRITER, KeyframeAnimationCounter.READER));


}
