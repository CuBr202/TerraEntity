package org.confluence.terraentity.network.s2c;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.entity.npc.misc.NPCDialogs;
import org.confluence.terraentity.entity.npc.mood.NPCMood;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SyncDataS2C {
    private static final Map<Integer, Handler<Object>> handlers = new HashMap<>();
    public static final int NPC_DIALOGS = register(NPCDialogs.Loader.CODEC, NPCDialogs.Loader::handle);
    public static final int NPC_MOODS = register(NPCMood.Loader.CODEC, NPCMood.Loader::handle);

    private static <T> int register(Codec<T> codec, Consumer<T> consumer) {
        int id = handlers.size();
        handlers.put(id, (Handler<Object>) new Handler<>(codec, consumer));
        return id;
    }

    private final int dataId;
    private final Object data;

    private SyncDataS2C(int dataId, Object data) {
        this.dataId = dataId;
        this.data = data;
    }

    private SyncDataS2C(FriendlyByteBuf buffer) {
        dataId = buffer.readVarInt();
        this.data = buffer.readJsonWithCodec(handlers.get(dataId).codec);
    }


    public static SyncDataS2C decode(FriendlyByteBuf buffer) {
        return new SyncDataS2C(buffer);
    }

    public static void encode(SyncDataS2C value, FriendlyByteBuf buffer) {
        buffer.writeVarInt(value.dataId);
        buffer.writeJsonWithCodec(handlers.get(value.dataId).codec, value.data);
    }

    public static void handle(SyncDataS2C packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handlers.get(packet.dataId).consumer.accept(packet.data)).exceptionally(e -> null);
    }

    public static <T> void sync(ServerPlayer player, int dataId, T value) {
        AdapterUtils.sendToPlayer(player, new SyncDataS2C(dataId, value));
    }

    public static void syncNpcDialogs(ServerPlayer player) {
        sync(player, NPC_DIALOGS, NPCDialogs.Loader.getInstance().getDialogs());
    }

    public static void syncNpcMoods(ServerPlayer player) {
        sync(player, NPC_MOODS, NPCMood.Loader.getInstance().getByType());
    }


    public record Handler<T>(Codec<T> codec, Consumer<T> consumer) {}
}
