package org.confluence.terraentity.network.s2c;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncNPCTradesPacketS2C {
    private final Map<ResourceLocation, Tag> tradesMap;


    public SyncNPCTradesPacketS2C(Map<ResourceLocation, Tag> tradesMap) {
        this.tradesMap = tradesMap;
    }

    public SyncNPCTradesPacketS2C(FriendlyByteBuf buffer) {
        this.tradesMap = new HashMap<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            tradesMap.put(buffer.readResourceLocation(), buffer.readNbt(NbtAccounter.UNLIMITED));
        }
    }

    public static SyncNPCTradesPacketS2C decode(FriendlyByteBuf buffer) {
        return new SyncNPCTradesPacketS2C(buffer);
    }

    public static void encode(SyncNPCTradesPacketS2C value, FriendlyByteBuf buffer) {
        buffer.writeVarInt(value.tradesMap.size());
        for (Map.Entry<ResourceLocation, Tag> entry : value.tradesMap.entrySet()) {
            buffer.writeResourceLocation(entry.getKey());
            buffer.writeNbt((CompoundTag) entry.getValue());
        }

    }

    public static void handle(SyncNPCTradesPacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            NPCTradeManager.reset(packet.tradesMap);
        }).exceptionally(e -> null);
        ctx.get().setPacketHandled(true);

    }

    public static void sync(ServerPlayer player){
        AdapterUtils.sendToPlayer(player, new SyncNPCTradesPacketS2C(NPCTradeManager.getTagMap()));
    }
}
