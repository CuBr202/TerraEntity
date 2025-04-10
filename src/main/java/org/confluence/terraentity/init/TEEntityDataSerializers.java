package org.confluence.terraentity.init;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.House;
import org.confluence.terraentity.entity.npc.NPCTrades;

import java.util.function.Supplier;


public final class TEEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, TerraEntity.MODID);

    public static final Supplier<EntityDataSerializer<NPCTrades>> DAVE_TRADES_SERIALIZER = SERIALIZERS.register(NPCTrades.KEY, () -> EntityDataSerializer.forValueType(NPCTrades.STREAM_CODEC));
    public static final Supplier<EntityDataSerializer<House>> DAVE_HOUSE_SERIALIZER = SERIALIZERS.register(House.KEY, () -> EntityDataSerializer.forValueType(House.STREAM_CODEC));



}
