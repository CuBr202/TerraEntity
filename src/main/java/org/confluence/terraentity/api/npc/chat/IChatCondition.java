package org.confluence.terraentity.api.npc.chat;

import com.mojang.serialization.Codec;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.chat.ChatHolder;
import org.confluence.terraentity.registries.chat_condition.ChatConditionProvider;
import org.confluence.terraentity.registries.chat_condition.ChatConditionProviderTypes;
import org.confluence.terraentity.registries.chat_condition.variant.NotChatCondition;

/**
 * npc 触发对话条件
 */
public interface IChatCondition {

    boolean canChat(AbstractTerraNPC npc, ChatHolder chatHolder);

    ChatConditionProvider getProvider();

    Codec<IChatCondition> TYPE_CODEC = ChatConditionProviderTypes.REGISTRY.get()
            .getCodec()
            .dispatch(IChatCondition::getProvider, i->i.codec().codec());

    static NotChatCondition not(IChatCondition condition) {
        return new NotChatCondition(condition);
    }


}
