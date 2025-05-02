package org.confluence.terraentity.entity.npc.trade;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade_list.ITradeGenerator;
import org.confluence.terraentity.registries.npc_trade_lock.ITradeLock;
import org.confluence.terraentity.utils.AdapterUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * 交易清单
 */
public class NPCTradeManager {

    public static DynamicOps<JsonElement> ops;

    private List<ITrade> trades;
    private List<ITrade> availableTrades;
    private ITradeHolder owner;
    protected List<Integer> toBeSync = new ArrayList<>();
    ITradeGenerator tradeList;
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

    /**
     * 用于传输数据
     * @param trade 当为空时，表示未初始化
     */
    public NPCTradeManager(List<ITrade> trade) {
        this.trades = new ArrayList<>(trade);

    }

    /**
     * 用于初始化
     * @param tradeList 交易列表的生成方式
     */
    public NPCTradeManager(ITradeGenerator tradeList) {
        this.tradeList = tradeList;
    }

    /**
     * 初始化交易列表，将未生成的表生成子表，同时设置owner
     */
    public void initTrades(ITradeHolder holder){
        if(tradeList!= null){
            this.trades = new ArrayList<>(tradeList.generateTrades());
            this.tradeList = null;
        }
        this.setOwner(holder);
    }


    private void setOwner(ITradeHolder npc) {
        this.owner = npc;
    }

    /**
     * 获取所有的交易列表
     */
    public List<ITrade> trades() {
        return this.trades;
    }

    /**
     * 获取可用的交易列表
     */
    public List<ITrade> availableTrades() {
        if(this.availableTrades == null){
            return this.trades;
        }
        return this.availableTrades;
    }

    /**
     * 设置可用的交易列表，在玩家打开商店时，服务端调用
     * @param player 玩家
     */
    public void reCheckAvailableTrades(Player player){
        int index = 0;
        TradeParams params = this.owner.getTradeParams();
        if (params == null) {
            this.availableTrades = this.trades;
            return;
        }
        BitMask bitMask = params.bitMask();
        boolean dirty = false;
        this.availableTrades = new ArrayList<>();
        for (ITrade trade : this.trades) {
            ITradeLock lock = trade.lock();
            if (lock == null || lock.canTrade(player, owner, index)) {
                this.availableTrades.add(trade);
                if (bitMask.remove(index)) {
                    dirty = true;
                }
            } else {
                if (bitMask.add(index)) {
                    dirty = true;
                }
            }
            index++;
        }
        if (dirty) {
            this.owner.syncTradeTasksParams();
        }
    }

    /**
     * 刷新可用的交易列表，在同步参数时，客户端调用
     */
    public void refreshAvailableTrades(){
        this.availableTrades = new ArrayList<>();
        TradeParams params = this.owner.getTradeParams();
        if (params == null) {
            this.availableTrades = this.trades;
            return;
        }
        BitMask bitMask = params.bitMask();
        int index = 0;
        for (ITrade trade : this.trades) {
            if (!bitMask.contains(index)) {
                this.availableTrades.add(trade);
            }
            index++;
        }
    }



    public boolean isEmpty(){
        return this.trades.isEmpty();
    }


    public static final String KEY = "npc_shop";
//    public static final Codec<NPCTradeManager> CODEC =Codec.withAlternative(
//            RecordCodecBuilder.create(instance -> instance.group(
//            ITrade.TYPED_CODEC.listOf().fieldOf("trades").forGetter(NPCTradeManager::trades)
//    ).apply(instance, NPCTradeManager::new)),
//            RecordCodecBuilder.create(instance -> instance.group(
//                    ITradeList.TYPED_CODEC.fieldOf("trades_generator").forGetter(i->i.tradeList)
//            ).apply(instance, NPCTradeManager::new))
//            );

    public static final Codec<NPCTradeManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ITrade.TYPED_CODEC.listOf().optionalFieldOf("trades").forGetter(i-> Optional.ofNullable(i.trades)),
            ITradeGenerator.TYPED_CODEC.optionalFieldOf("trades_generator").forGetter(i-> Optional.ofNullable(i.tradeList))
    ).apply(instance, (trades, tradeList)->{
        if(trades.isPresent()){
            return new NPCTradeManager(trades.get());
        }else if(tradeList.isPresent()){
            return new NPCTradeManager(tradeList.get());
        }else{
            return new NPCTradeManager(List.of());
        }
    }));

    public static FriendlyByteBuf.Writer<NPCTradeManager> WRITER = AdapterUtils.CodecWriter(CODEC);
    public static FriendlyByteBuf.Reader<NPCTradeManager> READER = AdapterUtils.CodecReader(CODEC);

    public static final Codec<Map<ResourceLocation, NPCTradeManager>> MAP_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, CODEC);

    public static FriendlyByteBuf.Writer<Map<ResourceLocation, NPCTradeManager>> MAP_WRITER = AdapterUtils.CodecWriter(MAP_CODEC);
    public static FriendlyByteBuf.Reader<Map<ResourceLocation, NPCTradeManager>> MAP_READER = AdapterUtils.CodecReader(MAP_CODEC);


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
        var encode = CODEC.encodeStart(ops, TRADE_MAP.get(id));
        if(encode.result().isPresent()){
            var result = CODEC.decode(ops, encode.result().get());
            if(result.result().isPresent()){
                return result.result().get().getFirst();
            }else{
                if(result.error().isPresent()){
                    TerraEntity.LOGGER.error("Failed to decode trade list {} : {}", id, result.error().get());
                }
            }
            return null;
        }else{
            if(encode.error().isPresent()){
                TerraEntity.LOGGER.error("Failed to encode trade list {} : {}", id, encode.error().get());
            }
            return null;
        }
    }

    public static void readTradesFromJson(ResourceManager manager) {
        Map<ResourceLocation, Resource> jsons = manager.listResources(KEY, r -> r.getPath().endsWith(".json"));
        ops = JsonOps.INSTANCE;

        jsons.forEach((k, v) -> {
            try {
                ResourceLocation id = TerraEntity.fromSpaceAndPath(k.getNamespace(),
                        k.getPath().replace(".json", "").replace(KEY + "/", ""));
                Reader reader = manager.openAsReader(k);
                JsonObject jsonobject = GsonHelper.parse(reader);
                DataResult<Pair<NPCTradeManager, JsonElement>> result = NPCTradeManager.CODEC.decode(JsonOps.INSTANCE, jsonobject);
                if(result.error().isPresent()){
                    throw new RuntimeException("Failed to read trade list " + k + " :" + result.error().get());
                }
                TRADE_MAP.put(id, result.result().get().getFirst());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchElementException e){
                throw new RuntimeException("Failed to read trade list " + k, e);
            }
        });
    }

}
