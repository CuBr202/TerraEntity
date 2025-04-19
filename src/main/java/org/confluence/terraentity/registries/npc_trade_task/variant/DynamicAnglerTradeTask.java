package org.confluence.terraentity.registries.npc_trade_task.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
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
 * 渔夫动态交易表，resultPool存放每个等级对应的固定奖励List，若不含这个等级，则使用默认奖励
 */
public class DynamicAnglerTradeTask implements ITradeTask {

    private final List<ItemStack> costPool;
    private final ITrade defaultTrade;
    private final Map<Integer, List<ItemStack>> resultPool;
    private Optional<ITrade> dynamicTrade;

    public static final MapCodec<DynamicAnglerTradeTask> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ITrade.TYPED_CODEC.fieldOf("default_trade").forGetter(DynamicAnglerTradeTask::getDefaultTrade),
            Codec.unboundedMap(Codec.STRING, ItemStack.CODEC.listOf()).fieldOf("result_pool").forGetter(
                    task -> task.resultPool.entrySet().stream()
                            .map(entry->new AbstractMap.SimpleEntry<>(entry.getKey().toString(), entry.getValue()))
                            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue))
            ),
            ItemStack.CODEC.listOf().fieldOf("cost_pool").forGetter(DynamicAnglerTradeTask::getCostPool),
            ITrade.TYPED_CODEC.optionalFieldOf("dynamic_trade").forGetter(DynamicAnglerTradeTask::getDynamicTrade)
    ).apply(instance, (defaultTrade, solid_rewards, costPool, dynamicTrade)->{
        return new DynamicAnglerTradeTask(
                defaultTrade,
                solid_rewards.entrySet()
                        .stream()
                        .map(entry->new AbstractMap.SimpleEntry<>(Integer.parseInt(entry.getKey()), entry.getValue()))
                        .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)),
                costPool,
                dynamicTrade
        );
    }));

    private Optional<ITrade> getDynamicTrade() {
        return dynamicTrade;
    }

    private List<ItemStack> getCostPool() {
        return costPool;
    }

    private ITrade getDefaultTrade() {
        return defaultTrade;
    }

    /**
     * 用于数据生成
     * @param defaultTrade 默认奖励，渔夫建议使用{@link org.confluence.terraentity.registries.npc_trade.variant.ItemTradeLootTable 战利品池交易表}
     * @param resultPool 等级对应的固定奖励池
     * @param costPool 消耗物品池
     */
    public DynamicAnglerTradeTask(ITrade defaultTrade, Map<Integer, List<ItemStack>> resultPool, List<ItemStack> costPool) {
        this(defaultTrade, resultPool, costPool, Optional.empty());
    }

    public DynamicAnglerTradeTask(ITrade defaultTrade, Map<Integer, List<ItemStack>> resultPool, List<ItemStack> costPool, Optional<ITrade> dynamicTrade) {
        this.defaultTrade = defaultTrade;
        this.resultPool = resultPool;
        this.costPool = costPool;
        this.dynamicTrade = dynamicTrade;
    }

    @Override
    public @Nullable ITrade getSelected(AbstractTerraNPC npc, int index) {
        int cur = npc.getTradeParams().getParam(index);
        if(resultPool.containsKey(cur)){
            return dynamicTrade.orElse(defaultTrade);
        }
        return defaultTrade;
    }

    @Override
    public void setNext(AbstractTerraNPC npc, int index) {
        int cur = npc.getTradeParams().getParam(index)+1;
        if(resultPool.containsKey(cur)){
            int maxCost = costPool.size();
            int random = npc.getRandom().nextInt(maxCost);
            dynamicTrade = Optional.of(ItemTradeItemList.of(costPool.get(random), resultPool.get(cur)));
            npc.syncTradeTasks();
        }
        npc.getTradeParams().increase(index);

        npc.syncTradeTasksParams();
    }

    @Override
    public boolean canTrade(AbstractTerraNPC npc, int index) {
        return true;
    }

    @Override
    public TradeTaskProvider getCodec() {
        return TradeTaskProviderTypes.DYNAMIC_ANGLER_TRADE_TASK.get();
    }
}
