package org.confluence.terraentity.registries.npc_trade;

import com.github.edg_thexu.cafelib.data.codec.LazyVarMapCodecProvider;
import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.api.npc.trade.ITrade;

import java.util.function.Supplier;

/**
 * 用于提供NPC交易类型编解码器
 */
public class TradeProvider extends LazyVarMapCodecProvider<ITrade> {

    public TradeProvider(Supplier<MapCodec<? extends ITrade>> codecSupplier) {
        super(codecSupplier);
    }
}
