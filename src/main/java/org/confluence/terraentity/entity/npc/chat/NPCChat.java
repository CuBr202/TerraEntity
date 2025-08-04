package org.confluence.terraentity.entity.npc.chat;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import org.confluence.terraentity.api.npc.chat.IChatElement;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.Collection;
import java.util.List;

/**
 * 一次对话
 */
public class NPCChat {

    public static Codec<NPCChat> CODEC = IChatElement.TYPED_CODEC.listOf().xmap(NPCChat::new, NPCChat::getChatElement);
    public static FriendlyByteBuf.Writer<NPCChat> WRITER = AdapterUtils.CodecWriter(CODEC);
    public static FriendlyByteBuf.Reader<NPCChat> READER = AdapterUtils.CodecReader(CODEC);

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

    /**
     * 为复合结点生成对话内容
     */
    public NPCChat generateChat(RandomSource random){
        return new NPCChat(chatElement.stream()
                .map(c->c.generate(random))
                .flatMap(Collection::stream)
                .toList());
    }


}
