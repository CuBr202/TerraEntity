package org.confluence.terraentity.registries.npc_trade_list;

import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.registries.LazyVarMapCodecProvider;

import java.util.function.Supplier;

/**
 * 用于提供NPC交易类型编解码器
 */
public class  TradeGeneratorProvider extends LazyVarMapCodecProvider<ITradeGenerator>{

    public TradeGeneratorProvider(Supplier<MapCodec<? extends ITradeGenerator>> mapCodecSupplier) {
        super(mapCodecSupplier);
    }
}
