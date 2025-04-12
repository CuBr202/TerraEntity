package org.confluence.terraentity.network.c2s;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.event.NPCTradeEvent;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.utils.AdapterUtils;
import org.jetbrains.annotations.NotNull;


public record NPCShopPacket(ITrade trade) implements CustomPacketPayload {
    public static final Type<NPCShopPacket> TYPE = new Type<>(TerraEntity.space("npc_trade_packet_s2c"));
    public static final StreamCodec<RegistryFriendlyByteBuf, NPCShopPacket> STREAM_CODEC = StreamCodec.composite(
            ITrade.STREAM_CODEC,
            NPCShopPacket::trade,
            NPCShopPacket::new
    );

    @Override
    public @NotNull Type<NPCShopPacket> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {

            if(context.player() instanceof ServerPlayer sp){
                NPCTradeEvent event = new NPCTradeEvent(trade, sp);
                AdapterUtils.postEvent(event);
                if (event.isCanceled()) {
                    return;
                }
                if(event.isAlwaysPass() || trade.canTrade(sp)) {
                    if(event.getRedirection()!=null){
                        event.getRedirection().accept(sp, trade );
                    }else{
                        trade.onTrade(sp);
                    }
                }
            }else{
                context.player().sendSystemMessage(Component.translatable("message.terra_entity.trade.not_enough_items"));
            }

        }).exceptionally(e -> null);
    }
}
