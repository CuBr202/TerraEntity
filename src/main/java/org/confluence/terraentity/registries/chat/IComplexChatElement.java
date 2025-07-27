package org.confluence.terraentity.registries.chat;

import net.minecraft.client.gui.Font;
import org.confluence.terraentity.api.npc.chat.IChatElement;
import org.confluence.terraentity.api.npc.chat.IChatRenderer;

/**
 * 作为组合模式的复合结点
 */
public interface IComplexChatElement extends IChatElement {

    @Override
    default Object getContent() {
        throw new UnsupportedOperationException("Disable to get complex content");
    }

    @Override
    default IChatRenderer<? extends IChatElement> getRenderer() {
        throw new UnsupportedOperationException("Disable to get complex renderer");
    }

    @Override
    default int wrapWidth(int input, Font font) {
        throw new UnsupportedOperationException("Disable to get complex width");
    }

    @Override
    default int warpHeight(int input, Font font) {
        throw new UnsupportedOperationException("Disable to get complex height");
    }
}
