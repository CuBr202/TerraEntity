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

public record RandomTradeTask(List<ITrade> trades) implements ITradeTask {

    public static MapCodec<RandomTradeTask> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(ITrade.TYPED_CODEC).fieldOf("trades").forGetter(RandomTradeTask::trades)
    ).apply(instance, RandomTradeTask::new));

    @Override
    public @Nullable ITrade getSelected(AbstractTerraNPC npc) {
        int size = trades.size();
        int target = npc.getTradeTaskCurrent(this);
        if (target >= size) {
            target = npc.getRandom().nextInt(size);
            npc.setTradeTaskIndex(target);
        }
        return trades.get(target);
    }

    @Override
    public void setNext(AbstractTerraNPC npc) {
        int size = trades.size();
        int target = npc.getRandom().nextInt(size);
        npc.setTradeTaskIndex(target);
    }

    @Override
    public TradeTaskProvider getCodec() {
        return TradeTaskProviderTypes.RANDOM_TRADE_TASK.get();
    }
}
