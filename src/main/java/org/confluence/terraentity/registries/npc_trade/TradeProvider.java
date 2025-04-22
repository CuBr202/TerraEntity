package org.confluence.terraentity.registries.npc_trade;

import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.registries.LazyVarMapCodecProvider;

import java.util.function.Supplier;

/**
 * 用于提供NPC交易类型编解码器
 */
public class TradeProvider extends LazyVarMapCodecProvider<ITrade> {

    public TradeProvider(Supplier<MapCodec<? extends ITrade>> codecSupplier) {
        super(codecSupplier);
    }
}
