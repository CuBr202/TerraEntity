package org.confluence.terraentity.entity.npc.trade;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易清单
 */
public class NPCTradeManager {

    private final List<ITrade> trades;
    private ITradeHolder owner;
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
        if(owner != null) {
            owner.syncNpcTrade(index);
        }
    }

    public NPCTradeManager(List<ITrade> trades) {
        this.trades = new ArrayList<>(trades);
    }


    public void setOwner(ITradeHolder npc) {
        this.owner = npc;
    }

    public List<ITrade> trades() {
        return trades;
    }

    public boolean isEmpty(){
        return trades.isEmpty();
    }


    public static final String KEY = "npc_shop";
    public static final Codec<NPCTradeManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ITrade.TYPED_CODEC.listOf().fieldOf("trades").forGetter(NPCTradeManager::trades)
    ).apply(instance, NPCTradeManager::new));

    public static final StreamCodec<ByteBuf, NPCTradeManager> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public static final Codec<Map<ResourceLocation, NPCTradeManager>> MAP_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, CODEC);
    public static final StreamCodec<ByteBuf, Map<ResourceLocation, NPCTradeManager>> MAP_STREAM_CODEC = ByteBufCodecs.fromCodec(MAP_CODEC);

    private static final Map<ResourceLocation, NPCTradeManager> TRADE_MAP = new HashMap<>();

    public static void reset(Map<ResourceLocation, NPCTradeManager> tradeMap){
        TRADE_MAP.clear();
        TRADE_MAP.putAll(tradeMap);
    }

    public static Map<ResourceLocation, NPCTradeManager> getTradeMap() {
        return TRADE_MAP;
    }
    /**
     * 获取NPC商店的交易列表
     * @param id 交易表id
     */
    @Nullable
    public static NPCTradeManager getTradeById(ResourceLocation id) {
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
    public static NPCTradeManager getCopy(ResourceLocation id) {
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
                TRADE_MAP.put(id, NPCTradeManager.CODEC.decode(JsonOps.INSTANCE, jsonobject).result().get().getFirst());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
