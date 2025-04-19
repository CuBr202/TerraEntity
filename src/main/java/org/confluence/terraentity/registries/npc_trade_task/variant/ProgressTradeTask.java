package org.confluence.terraentity.registries.npc_trade_task.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade_task.ITradeTask;
import org.confluence.terraentity.registries.npc_trade_task.TradeTaskProvider;
import org.confluence.terraentity.registries.npc_trade_task.TradeTaskProviderTypes;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 按进度的任务
 */
public class ProgressTradeTask implements ITradeTask {
    // 由于是可变的，所以不能用record
    protected final List<ITrade> trades;
    protected int current;

    /**
     * @param trades 任务顺序列表
     * @param current 当前进度
     */
    public ProgressTradeTask(List<ITrade> trades, int current) {
        this.trades = trades;
        this.current = current;
    }

    public ProgressTradeTask(List<ITrade> trades) {
        this(trades, 0);
    }

    public static MapCodec<ProgressTradeTask> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(ITrade.TYPED_CODEC).fieldOf("trades").forGetter(ProgressTradeTask::trades),
            Codec.INT.fieldOf("current").forGetter(ProgressTradeTask::current)
    ).apply(instance, ProgressTradeTask::new));

    public List<ITrade> trades() {
        return trades;
    }

    public int current() {
        return current;
    }


    @Override
    public @Nullable ITrade getSelected(AbstractTerraNPC npc) {
        int target = current;
        if (target >= trades.size()) {
            return null;
        }
        return trades.get(target);
    }

    @Override
    public void setNext(AbstractTerraNPC npc) {
        this.current += 1;
        npc.syncTradeTasks();
    }

    @Override
    public boolean canTrade(AbstractTerraNPC npc) {
        return current < trades.size();
    }

    @Override
    public TradeTaskProvider getCodec() {
        return TradeTaskProviderTypes.PROGRESS_TRADE_TASK.get();
    }
}
