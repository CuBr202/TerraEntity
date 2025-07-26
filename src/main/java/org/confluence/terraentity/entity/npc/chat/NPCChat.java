package org.confluence.terraentity.entity.npc.chat;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.confluence.terraentity.api.npc.chat.IChatElement;

import java.util.List;

/**
 * 一次对话
 */
public class NPCChat {

    public static Codec<NPCChat> CODEC = IChatElement.TYPED_CODEC.listOf().xmap(NPCChat::new, NPCChat::getChatElement);
    public static StreamCodec<RegistryFriendlyByteBuf, NPCChat> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    /**
     * 对话内容
     */
    public List<IChatElement> chatElement;

    public NPCChat(List<IChatElement> chatElement) {
        this.chatElement = chatElement;
    }

    public List<IChatElement> getChatElement() {
        return chatElement;
    }


}
