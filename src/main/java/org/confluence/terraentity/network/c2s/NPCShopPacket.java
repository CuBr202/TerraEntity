package org.confluence.terraentity.network.c2s;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.api.event.NPCEvent;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.mixed.IPlayer;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.function.Supplier;


public class NPCShopPacket {
    int tradeIndex;

    public NPCShopPacket(int tradeIndex) {
        this.tradeIndex = tradeIndex;
    }

    public NPCShopPacket(FriendlyByteBuf buf) {
        tradeIndex = buf.readInt();

    }

    public static NPCShopPacket decode(FriendlyByteBuf buffer) {
        return new NPCShopPacket(buffer);
    }

    public static void encode(NPCShopPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.tradeIndex);
    }


    public static void handle(NPCShopPacket packet, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {

                AbstractTerraNPC npc;
                ServerPlayer sp = context.getSender();
                ITrade trade;
                int tradeIndex = packet.tradeIndex;
                if(((IPlayer)sp).terra_entity$getTradeHolder() instanceof AbstractTerraNPC npc1){
                    npc = npc1;
                    if(tradeIndex < 0 ){
                        return;
                    }
                    trade = npc.getTradeManager().trades().get(tradeIndex);

                    NPCEvent.NPCTradeEvent event = new NPCEvent.NPCTradeEvent(npc, trade, sp);
                    AdapterUtils.postEvent(event);
                    if (event.isCanceled()) {
                        return;
                    }
                    if(event.isAlwaysPass() || trade.canTradeWithLock(sp, npc, tradeIndex)
                    ) {
                        if(event.getRedirection()!=null){
                            event.getRedirection().accept(sp, trade );
                        }else{
                            trade.onTrade(sp, npc, tradeIndex);
                        }
                    }
                }

//                context.getSender().sendSystemMessage(Component.translatable("message.terra_entity.trade.not_enough_items"));


        }).exceptionally(e -> null);
        context.setPacketHandled(true);
    }
}
