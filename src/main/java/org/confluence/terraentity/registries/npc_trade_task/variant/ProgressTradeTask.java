package org.confluence.terraentity.registries.npc_trade_task.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.TradeParams;
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

    protected final List<ITrade> trades;

    /**
     * @param trades 任务顺序列表
     */
    public ProgressTradeTask(List<ITrade> trades) {
        this.trades = trades;
    }


    public static MapCodec<ProgressTradeTask> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(ITrade.TYPED_CODEC).fieldOf("trades").forGetter(ProgressTradeTask::trades)
    ).apply(instance, ProgressTradeTask::new));

    public List<ITrade> trades() {
        return trades;
    }

    @Override
    public @Nullable ITrade getSelected(AbstractTerraNPC npc, int index) {
        TradeParams params = npc.getTradeParams();
        int target = params.getParam(index);;
        if (target >= trades.size()) {
            return null;
        }
        return trades.get(target);
    }

    @Override
    public void setNext(AbstractTerraNPC npc, int index) {
        npc.getTradeParams().increase(index);
        npc.syncTradeTasksParams();
    }

    @Override
    public boolean canTrade(AbstractTerraNPC npc, int index) {
        return npc.getTradeParams().getParam(index) < trades.size();
    }

    @Override
    public TradeTaskProvider getCodec() {
        return TradeTaskProviderTypes.PROGRESS_TRADE_TASK.get();
    }
}
