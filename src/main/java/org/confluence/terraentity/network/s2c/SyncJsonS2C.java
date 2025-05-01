package org.confluence.terraentity.network.s2c;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.misc.NPCDialogs;
import org.confluence.terraentity.entity.npc.mood.NPCMoods;
import org.confluence.terraentity.utils.AdapterUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class SyncJsonS2C implements CustomPacketPayload {

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
    public static final CodecEnum<Map<EntityType<?>, NPCMoods.EntityMood>> NPC_MOODS_S2C_CODEC = registerHandler(NPCMoods.MAP_LIST_CODEC, (self, json)->{
        NPCMoods.loadFromServer(json);
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

    public static final Type<SyncJsonS2C> TYPE = new Type<>(TerraEntity.space("sync_json_packet_s2c"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncJsonS2C> STREAM_CODEC = CustomPacketPayload.codec(SyncJsonS2C::encode, SyncJsonS2C::decode);


    @Override
    public @NotNull Type<SyncJsonS2C> type() {
        return TYPE;
    }

    public static SyncJsonS2C decode(FriendlyByteBuf buffer) {
        return new SyncJsonS2C(buffer);
    }

    public static void encode(SyncJsonS2C packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.type);
        buf.writeUtf(packet.json.toString());
    }

    public void handle(IPayloadContext context) {
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
    public static void syncNpcMoods(ServerPlayer player){
        sync(player, NPC_MOODS_S2C_CODEC, NPCMoods.BY_ENTITY_TYPE);
    }
}
