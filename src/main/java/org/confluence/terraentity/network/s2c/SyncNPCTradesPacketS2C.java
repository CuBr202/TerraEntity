package org.confluence.terraentity.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.utils.AdapterUtils;
import java.util.Map;
import java.util.function.Supplier;

public class SyncNPCTradesPacketS2C {
    private Map<ResourceLocation, NPCTradeManager> tradesMap;


    public SyncNPCTradesPacketS2C(Map<ResourceLocation, NPCTradeManager> tradesMap) {
        this.tradesMap = tradesMap;
    }

    public SyncNPCTradesPacketS2C(FriendlyByteBuf buf) {
        this.tradesMap = NPCTradeManager.MAP_READER.apply(buf);

    }

    public static SyncNPCTradesPacketS2C decode(FriendlyByteBuf buffer) {
        return new SyncNPCTradesPacketS2C(buffer);
    }

    public static void encode(SyncNPCTradesPacketS2C packet, FriendlyByteBuf buf) {
        NPCTradeManager.MAP_WRITER.accept(buf, packet.tradesMap);

    }

    public static void handle(SyncNPCTradesPacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            NPCTradeManager.reset(packet.tradesMap);
        }).exceptionally(e -> null);
    }

    public static void sync(ServerPlayer player){
        AdapterUtils.sendToPlayer(player, new SyncNPCTradesPacketS2C(NPCTradeManager.getTradeMap()));
    }
}
