package org.confluence.terraentity.registries.npc_trade;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItem;
import org.confluence.terraentity.registries.track.ITrackType;

import java.util.List;

/**
 * 用于提供NPC交易类型编解码器
 * @param codec
 */
public record TradeProvider(MapCodec<? extends ITrade> codec){

}
