package org.confluence.terraentity.entity.npc;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易清单
 * @param trades 交易列表
 */
public record NPCTrades(List<ITrade> trades) {
    public static final String KEY = "npc_shop";
    public static final Codec<NPCTrades> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ITrade.TYPED_CODEC.listOf().fieldOf("trades").forGetter(NPCTrades::trades)
    ).apply(instance, NPCTrades::new));

    public static final StreamCodec<ByteBuf, NPCTrades> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public static final Codec<Map<ResourceLocation,NPCTrades>> MAP_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, CODEC);
    public static final StreamCodec<ByteBuf, Map<ResourceLocation, NPCTrades>> MAP_STREAM_CODEC = ByteBufCodecs.fromCodec(MAP_CODEC);

    private static final Map<ResourceLocation, NPCTrades> TRADE_MAP = new HashMap<>();

    public static void reset(Map<ResourceLocation, NPCTrades> tradeMap){
        TRADE_MAP.clear();
        TRADE_MAP.putAll(tradeMap);
    }

    public static Map<ResourceLocation, NPCTrades> getTradeMap() {
        return TRADE_MAP;
    }
    /**
     * 获取NPC商店的交易列表
     * @param id 交易表id
     */
    @Nullable
    public static NPCTrades getTrade(ResourceLocation id) {
        return TRADE_MAP.get(id);
    }

    public static void readTradesFromJson(ResourceManager manager) {
        Map<ResourceLocation, Resource> jsons = manager.listResources(KEY, r -> r.getPath().endsWith(".json"));
        jsons.forEach((k, v) -> {
            try {
                ResourceLocation id = TerraEntity.fromSpaceAndPath(k.getNamespace(),
                        k.getPath().replace(".json", "").replace(KEY + "/", ""));
                Reader reader = manager.openAsReader(k);
                JsonObject jsonobject = GsonHelper.parse(reader);
                TRADE_MAP.put(id, NPCTrades.CODEC.decode(JsonOps.INSTANCE, jsonobject).result().get().getFirst());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
