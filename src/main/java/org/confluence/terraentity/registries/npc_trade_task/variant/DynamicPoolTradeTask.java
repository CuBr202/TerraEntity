package org.confluence.terraentity.registries.npc_trade_task.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.entity.npc.ITradeHolder;
import org.confluence.terraentity.entity.npc.TradeParams;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItemList;
import org.confluence.terraentity.registries.npc_trade_task.ITradeTask;
import org.confluence.terraentity.registries.npc_trade_task.TradeTaskProvider;
import org.confluence.terraentity.registries.npc_trade_task.TradeTaskProviderTypes;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 动态交易表，resultPool存放每个等级对应的固定奖励List，若不含这个等级，则使用默认奖励
 */
public class DynamicPoolTradeTask implements ITradeTask {

    private ITrade dynamicTrade;

    private final ITrade defaultTrade;
    private final Map<Integer, List<ItemStack>> resultPool;
    private final List<ItemStack> costPool;

    public static final MapCodec<DynamicPoolTradeTask> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ITrade.TYPED_CODEC.fieldOf("default_trade").forGetter(DynamicPoolTradeTask::getDefaultTrade),
            Codec.unboundedMap(Codec.STRING, ItemStack.CODEC.listOf()).fieldOf("result_pool").forGetter(
                    task -> task.resultPool.entrySet().stream()
                            .map(entry->new AbstractMap.SimpleEntry<>(entry.getKey().toString(), entry.getValue()))
                            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue))
            ),
            ItemStack.CODEC.listOf().fieldOf("cost_pool").forGetter(DynamicPoolTradeTask::getCostPool),
            ITrade.TYPED_CODEC.optionalFieldOf("dynamic_trade").forGetter(DynamicPoolTradeTask::getDynamicTrade)
    ).apply(instance, (defaultTrade, solid_rewards, costPool, dynamicTrade)->{
        return new DynamicPoolTradeTask(
                defaultTrade,
                solid_rewards.entrySet()
                        .stream()
                        .map(entry->new AbstractMap.SimpleEntry<>(Integer.parseInt(entry.getKey()), entry.getValue()))
                        .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)),
                costPool,
                dynamicTrade.orElse(null)
        );
    }));

    private Optional<ITrade> getDynamicTrade() {
        return Optional.ofNullable(dynamicTrade);
    }

    private List<ItemStack> getCostPool() {
        return costPool;
    }

    private ITrade getDefaultTrade() {
        return defaultTrade;
    }

    /**
     * 用于数据生成
     * @param defaultTrade 默认奖励
     * @param resultPool 等级对应的固定奖励池
     * @param costPool 消耗物品池
     */
    public DynamicPoolTradeTask(ITrade defaultTrade, Map<Integer, List<ItemStack>> resultPool, List<ItemStack> costPool) {
        this(defaultTrade, resultPool, costPool, null);
    }

    public DynamicPoolTradeTask(ITrade defaultTrade, Map<Integer, List<ItemStack>> resultPool, List<ItemStack> costPool, ITrade dynamicTrade) {
        this.defaultTrade = defaultTrade;
        this.resultPool = resultPool;
        this.costPool = costPool;
        this.dynamicTrade = dynamicTrade;
    }

    @Override
    public @Nullable ITrade getSelected(ITradeHolder npc, int index) {
        int cur = npc.getTradeParams().getLevel(index);
        if(resultPool.containsKey(cur)){
            return dynamicTrade == null ? defaultTrade:dynamicTrade;
        }
        return defaultTrade;
    }

    @Override
    public void setNext(ITradeHolder npc, int index) {
        int cur = npc.getTradeParams().getLevel(index)+1;
        if(resultPool.containsKey(cur)){
            int maxCost = costPool.size();
            int random = npc.getRandom().nextInt(maxCost);

            dynamicTrade = ItemTradeItemList.of(costPool.get(random), resultPool.get(cur));
//            npc.syncTrades();
            npc.getTradeManager().addToBeSync(index);
        }
        npc.getTradeParam(index).ifPresent(TradeParams.Param::increaseLevel);
        npc.syncTradeTasksParams();
    }



    @Override
    public boolean canTrade(ITradeHolder npc, int index) {
        return true;
    }

    @Override
    public TradeTaskProvider getCodec() {
        return TradeTaskProviderTypes.DYNAMIC_POOL_MAP_TRADE_TASK.get();
    }
}
