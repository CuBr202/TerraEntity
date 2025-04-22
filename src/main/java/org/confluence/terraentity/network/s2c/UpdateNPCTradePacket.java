package org.confluence.terraentity.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.mixin.accessor.LevelAccessor;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * 更新npc交易的单个列表，以节省网络流量
 */
public class UpdateNPCTradePacket {

    private final int index;
    private final UUID npcId;
    private final ITrade trade;

    private UpdateNPCTradePacket(int index, UUID npcId, ITrade trade) {
        this.index = index;
        this.npcId = npcId;
        this.trade = trade;
    }

    public UpdateNPCTradePacket(FriendlyByteBuf buffer) {
       this.index = buffer.readInt();
       this.npcId = buffer.readUUID();
       this.trade = buffer.readJsonWithCodec(ITrade.TYPED_CODEC);
    }


    public static UpdateNPCTradePacket decode(FriendlyByteBuf buffer) {
        return new UpdateNPCTradePacket(buffer);
    }

    public static void encode(UpdateNPCTradePacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.index);
        buf.writeUUID(packet.npcId);
        buf.writeJsonWithCodec(ITrade.TYPED_CODEC, packet.trade);
        
    }

    public static void handle(UpdateNPCTradePacket packet, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        var player = context.getSender();
        UUID npcId = player.getUUID();
        int index = packet.index;
        ITrade trade = packet.trade;

        context.enqueueWork(() -> {
            if(((LevelAccessor)(player.level())).callGetEntities().get(npcId) instanceof AbstractTerraNPC npc){
                npc.getTradeManager().trades().set(index, trade);
            }
            
        }).exceptionally(e -> null);
    }



    public static void syncNpcTrade(int index, AbstractTerraNPC npc){
        AdapterUtils.sendToAllPlayers(new UpdateNPCTradePacket(index, npc.getUUID(), npc.getTradeManager().trades().get(index)));
    }
}
