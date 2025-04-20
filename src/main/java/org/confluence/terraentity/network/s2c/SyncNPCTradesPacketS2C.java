package org.confluence.terraentity.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.NPCTradeManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record SyncNPCTradesPacketS2C(Map<ResourceLocation, NPCTradeManager> tradesMap) implements CustomPacketPayload {
    public static final Type<SyncNPCTradesPacketS2C> TYPE = new Type<>(TerraEntity.space("npc_trades_packet_s2c"));
    public static final StreamCodec<ByteBuf, SyncNPCTradesPacketS2C> STREAM_CODEC = NPCTradeManager.MAP_STREAM_CODEC.map(SyncNPCTradesPacketS2C::new, SyncNPCTradesPacketS2C::tradesMap);

    @Override
    public @NotNull Type<SyncNPCTradesPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            NPCTradeManager.reset(tradesMap);
        }).exceptionally(e -> null);
    }

    public static void sync(ServerPlayer player){
        PacketDistributor.sendToPlayer(player, new SyncNPCTradesPacketS2C(NPCTradeManager.getTradeMap()));
    }
}
