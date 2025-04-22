package org.confluence.terraentity.registries.npc_trade_lock;

import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.registries.LazyVarMapCodecProvider;
import org.confluence.terraentity.registries.npc_trade_task.ITradeTask;

import java.util.function.Supplier;

/**
 * 用于提供NPC交易类型编解码器
 */
public class TradeLockProvider extends LazyVarMapCodecProvider<ITradeLock> {

    public TradeLockProvider(Supplier<MapCodec<? extends ITradeLock>> mapCodecSupplier) {
        super(mapCodecSupplier);
    }
}
