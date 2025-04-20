package org.confluence.terraentity.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.utils.AdapterUtils;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * 更新npc交易的单个列表，以节省网络流量
 */
public class UpdateNPCTradePacket implements CustomPacketPayload {

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

    public static final Type<UpdateNPCTradePacket> TYPE = new Type<>(TerraEntity.space("update_npc_trade_packet_s2c"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateNPCTradePacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateNPCTradePacket::encode, UpdateNPCTradePacket::decode);


    @Override
    public @NotNull Type<UpdateNPCTradePacket> type() {
        return TYPE;
    }

    public static UpdateNPCTradePacket decode(FriendlyByteBuf buffer) {
        return new UpdateNPCTradePacket(buffer);
    }

    public static void encode(UpdateNPCTradePacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.index);
        buf.writeUUID(packet.npcId);
        buf.writeJsonWithCodec(ITrade.TYPED_CODEC, packet.trade);
        
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if(context.player().level().getEntities().get(this.npcId) instanceof AbstractTerraNPC npc){
                npc.getTradeManager().trades().set(this.index, this.trade);
            }
            
        }).exceptionally(e -> null);
    }



    public static void syncNpcTrade(int index, AbstractTerraNPC npc){
        AdapterUtils.sendToAllPlayers(new UpdateNPCTradePacket(index, npc.getUUID(), npc.getTradeManager().trades().get(index)));
    }
}
