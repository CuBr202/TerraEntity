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
import org.confluence.terraentity.network.s2c.UpdateNPCTradePacket;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易清单
 */
public class NPCTrades{

    private final List<ITrade> trades;
    private AbstractTerraNPC npc;
    protected List<Integer> toBeSync = new ArrayList<>();

    /**
     * 记录需要更新的交易表
     * @param index 交易表索引
     */
    public void addToBeSync(int index){
        toBeSync.add(index);
    }

    /**
     * 局部更新所有交易表
     */
    public void syncDirtyTrade(){
        for(int index : toBeSync){
            syncTradeTasks(index);
        }
        toBeSync.clear();
    }

    private void syncTradeTasks(int index){
        if(npc != null) {
            UpdateNPCTradePacket.syncNpcTrade(index, npc);
        }
    }

    public NPCTrades(List<ITrade> trades) {
        this.trades = new ArrayList<>(trades);
    }


    public void setOwner(AbstractTerraNPC npc) {
        this.npc = npc;
    }

    public List<ITrade> trades() {
        return trades;
    }


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
    public static NPCTrades getTradeById(ResourceLocation id) {
        if(!TRADE_MAP.containsKey(id)){
            return null;
        }
        return TRADE_MAP.get(id);
    }

    /**
     * 用于初始化npc时给出不同的交易列表防止影响全局
     * @param id 交易表id
     * @return 交易列表的拷贝
     */
    @Nullable
    public static NPCTrades getCopy(ResourceLocation id) {
        if(!TRADE_MAP.containsKey(id)){
            return null;
        }
        return CODEC.decode(JsonOps.INSTANCE, CODEC.encodeStart(JsonOps.INSTANCE, TRADE_MAP.get(id)).getOrThrow()).result().get().getFirst();
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
