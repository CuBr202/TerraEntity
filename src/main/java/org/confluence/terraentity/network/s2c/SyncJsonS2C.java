package org.confluence.terraentity.network.s2c;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terraentity.entity.npc.misc.NPCDialogs;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SyncJsonS2C{

    /**
     * 用于序列化指定枚举类型
     * @param <T> 指定类型
     */
    public static class CodecEnum<T>{
        private final Codec<T> codec;
        int type;

        /**
         * 用于发包指定枚举类型
         * @param codec codec
         * @param type 枚举类型
         */
        private CodecEnum(int type, Codec<T> codec, BiConsumer<CodecEnum<T>,JsonElement> handle){
            this.codec = codec;
            this.type = type;
            this.handle = handle;
        }
        BiConsumer<CodecEnum<T>,JsonElement> handle;
        private JsonElement encode(T value){
            return codec.encodeStart(JsonOps.INSTANCE, value).result().get();
        }
        private T decode(JsonElement json){
            return codec.decode(JsonOps.INSTANCE, json).result().get().getFirst();
        }
    }

    static int idIndex = 0;

    static Map<Integer, CodecEnum<?>> handlers = new HashMap<>();


    public static final CodecEnum<Map<ResourceLocation, NPCDialogs>> NPC_DIALOGS_S2C_CODEC = registerHandler(NPCDialogs.MAP_CODEC, (self, json)->{
        NPCDialogs.loadFromServer(json);
    });


    // 指定codec对应的枚举
    static <T> CodecEnum<T> registerHandler(Codec<T> codec, BiConsumer<CodecEnum<T>,JsonElement> handle){
        CodecEnum<T> handler = new CodecEnum<>(idIndex++, codec, handle);
        handlers.put(handler.type, handler);
        return handler;
    }


    private final int type;
    private final JsonElement json;

    private SyncJsonS2C(int type, JsonElement json) {
        this.type = type;
        this.json = json;
    }

    public SyncJsonS2C(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.json = GsonHelper.parse(buffer.readUtf());
    }



    public static SyncJsonS2C decode(FriendlyByteBuf buffer) {
        return new SyncJsonS2C(buffer);
    }

    public static void encode(SyncJsonS2C packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.type);
        buf.writeUtf(packet.json.toString());
    }

    public static void handle(SyncJsonS2C packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        int type = packet.type;
        JsonElement json = packet.json;
        context.enqueueWork(() -> {
//            Object value = handlers.get(type).decode(json);
            CodecEnum handler = handlers.get(type);
            var handle = handler.handle;
            handle.accept(handler, json);
        }).exceptionally(e -> null);
    }


    public static <T> void sync(ServerPlayer player, CodecEnum<T> handler, T value){
        AdapterUtils.sendToPlayer(player, new SyncJsonS2C(handler.type, handler.encode(value)));
    }
    public static void syncNpcDialogs(ServerPlayer player){
        sync(player, NPC_DIALOGS_S2C_CODEC, NPCDialogs.getDialog_map());
    }
}
