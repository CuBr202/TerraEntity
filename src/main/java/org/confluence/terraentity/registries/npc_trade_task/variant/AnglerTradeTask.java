package org.confluence.terraentity.registries.npc_trade_task.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade_task.ITradeTask;
import org.confluence.terraentity.registries.npc_trade_task.TradeTaskProvider;
import org.confluence.terraentity.registries.npc_trade_task.TradeTaskProviderTypes;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 渔夫的交易任务，固定次数会获得固定的物品，其他时候获得指定战利品表
 */
public class AnglerTradeTask implements ITradeTask  {

    private int current;
    private final Map<Integer, ITrade> solid_Rewards;
    private final ITrade defaultTrade;

    public static final MapCodec<AnglerTradeTask> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, ITrade.TYPED_CODEC).fieldOf("solid_rewards").forGetter(
                    task -> task.solid_Rewards.entrySet().stream()
                            .map(entry->new AbstractMap.SimpleEntry<>(entry.getKey().toString(), entry.getValue()))
                            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue))
            ),
            Codec.INT.fieldOf("current").forGetter(AnglerTradeTask::getCurrent),
            ITrade.TYPED_CODEC.fieldOf("default_trade").forGetter(AnglerTradeTask::getDefaultTrade)
    ).apply(instance, (solid_rewards, current, trade)->{
        return new AnglerTradeTask(solid_rewards.entrySet()
                .stream()
                .map(entry->new AbstractMap.SimpleEntry<>(Integer.parseInt(entry.getKey()), entry.getValue()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)),
                current,
                trade
        );
    }));

    private Integer getCurrent() {
        return current;
    }

    private ITrade getDefaultTrade() {
        return defaultTrade;
    }

    /**
     * 用于数据生成构造
     * @param solid_rewards 次数对应的物品奖励表
     * @param defaultTrade 默认交易
     */
    public AnglerTradeTask(Map<Integer, ITrade> solid_rewards, ITrade defaultTrade) {
        this(solid_rewards, 0, defaultTrade);
    }

    /**
     * 用于编解码器数据传输
     * @param solid_rewards 次数对应的物品奖励表
     * @param current 当前次数
     * @param defaultTrade 默认交易
     */
    public AnglerTradeTask(Map<Integer, ITrade> solid_rewards, int current, ITrade defaultTrade) {
        this.solid_Rewards = solid_rewards;
        this.current = current;
        this.defaultTrade = defaultTrade;
    }

    @Override
    public @Nullable ITrade getSelected(AbstractTerraNPC npc) {
        if(solid_Rewards.containsKey(current)){
            return solid_Rewards.get(current);
        }
        return defaultTrade;
    }

    @Override
    public void setNext(AbstractTerraNPC npc) {
        if(current < solid_Rewards.size()) {
            current++;
        }
    }

    @Override
    public boolean canTrade(AbstractTerraNPC npc) {
        return true;
    }

    @Override
    public TradeTaskProvider getCodec() {
        return TradeTaskProviderTypes.ANGLER_TRADE_TASK.get();
    }
}
