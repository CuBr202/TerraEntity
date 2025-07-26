package org.confluence.terraentity.api.npc.chat;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.Font;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.chat.ChatElementProvider;

/**
 * NPC对话框元素绘制接口
 */
public interface IChatElement<T> {

    T getContent();

    ChatElementProvider getProvider();

    IChatRenderer<? extends IChatElement<T>> getRenderer();

    int wrapWidth(int input, Font font);

    int warpHeight(int input, Font font);


    Codec<IChatElement> TYPED_CODEC = TERegistries.ChatElementProviderRegistry.REGISTRY
            .byNameCodec()
            .dispatch(IChatElement::getProvider, ChatElementProvider::codec);

}
