package org.confluence.terraentity.registries.npc_trade_task;

import com.mojang.serialization.MapCodec;

/**
 * 用于提供NPC交易类型编解码器
 * @param codec
 */
public record TradeTaskProvider(MapCodec<? extends ITradeTask> codec){

}
