package org.confluence.terraentity.network.c2s;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.api.event.NPCEvent;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.trade.TradeParams;
import org.confluence.terraentity.mixed.IPlayer;
import org.confluence.terraentity.api.npc.trade.ITrade;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.function.Supplier;


public class NPCShopPacket {
    int tradeIndex;
    TradeParams params;

    public NPCShopPacket(int tradeIndex, TradeParams params) {
        this.tradeIndex = tradeIndex;
        this.params = params;
    }

    public NPCShopPacket(FriendlyByteBuf buf) {
        tradeIndex = buf.readInt();
        params = buf.readJsonWithCodec(TradeParams.CODEC);
    }

    public static NPCShopPacket decode(FriendlyByteBuf buffer) {
        return new NPCShopPacket(buffer);
    }

    public static void encode(NPCShopPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.tradeIndex);
        buf.writeJsonWithCodec(TradeParams.CODEC, packet.params);
    }


    public static void handle(NPCShopPacket packet, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {

                AbstractTerraNPC holder;
                ServerPlayer sp = context.getSender();
                ITrade trade;
                int tradeIndex = packet.tradeIndex;
                if(((IPlayer)sp).terra_entity$getTradeHolder() instanceof AbstractTerraNPC npc){
                    holder = npc;
                    TradeParams params = packet.params;
                    if(tradeIndex < 0 ){
                        return;
                    }
//                    trade = holder.getTradeManager().availableTrades().get(tradeIndex);
                    trade = holder.getTradeManager().targetTrade(params, tradeIndex);

                    NPCEvent.NPCTradeEvent event = new NPCEvent.NPCTradeEvent(holder, trade, sp);
                    AdapterUtils.postEvent(event);
                    if (event.isCanceled()) {
                        return;
                    }
                    if(event.isAlwaysPass() || trade.canTradeWithLock(sp, holder, tradeIndex)) {
                        if(event.getRedirection()!=null){
                            event.getRedirection().accept(sp, trade );
                        }else{
                            trade.onTrade(sp, holder, tradeIndex);
                        }
                    }
                }

//                context.getSender().sendSystemMessage(Component.translatable("message.terra_entity.trade.not_enough_items"));


        }).exceptionally(e -> null);
        context.setPacketHandled(true);
    }
}
