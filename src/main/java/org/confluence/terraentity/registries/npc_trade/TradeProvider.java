package org.confluence.terraentity.registries.npc_trade;

import com.mojang.serialization.MapCodec;


/**
 * 用于提供NPC交易类型编解码器
 * @param codec
 */
public record TradeProvider(MapCodec<? extends ITrade> codec){

}
