package org.confluence.terraentity.registries.npc_trade_task;

import com.github.edg_thexu.cafelib.data.codec.LazyVarMapCodecProvider;
import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.api.npc.trade.ITradeTask;

import java.util.function.Supplier;

/**
 * 用于提供NPC交易类型编解码器
 */
public class TradeTaskProvider extends LazyVarMapCodecProvider<ITradeTask> {

    public TradeTaskProvider(Supplier<MapCodec<? extends ITradeTask>> codecSupplier){
        super(codecSupplier);
    }
}
