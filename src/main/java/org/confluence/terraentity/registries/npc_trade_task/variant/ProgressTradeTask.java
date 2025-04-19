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

public record ProgressTradeTask(List<ITrade> trades) implements ITradeTask {

    public static MapCodec<ProgressTradeTask> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(ITrade.TYPED_CODEC).fieldOf("trades").forGetter(ProgressTradeTask::trades)
    ).apply(instance, ProgressTradeTask::new));

    @Override
    public @Nullable ITrade getSelected(AbstractTerraNPC npc) {
        int size = trades.size();
        int target = npc.getTradeTaskCurrent(this);
        if (target >= size) {
            return null;
        }
        return trades.get(target);
    }

    @Override
    public void setNext(AbstractTerraNPC npc) {
        npc.setTradeTaskIndex(npc.getTradeTaskCurrent(this) + 1);
    }

    @Override
    public TradeTaskProvider getCodec() {
        return TradeTaskProviderTypes.PROGRESS_TRADE_TASK.get();
    }
}
