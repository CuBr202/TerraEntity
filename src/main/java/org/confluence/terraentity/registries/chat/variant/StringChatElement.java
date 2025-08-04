package org.confluence.terraentity.registries.chat.variant;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import org.confluence.terraentity.api.npc.chat.IChatRenderer;
import org.confluence.terraentity.client.gui.renderer.chat.element.ChatComponentRenderer;
import org.confluence.terraentity.registries.chat.ChatElementProvider;
import org.confluence.terraentity.registries.chat.ChatProviderTypes;
import org.confluence.terraentity.api.npc.chat.IChatElement;

public class StringChatElement implements IChatElement<Component> {

    public static MapCodec<StringChatElement> MAPCODEC = ExtraCodecs.COMPONENT.xmap(StringChatElement::new, IChatElement::getContent).fieldOf("content");
    Component component;
    public StringChatElement(Component component) {
        this.component = component;
    }

    @Override
    public Component getContent() {
        return component;

    }

    @Override
    public ChatElementProvider getProvider() {
        return ChatProviderTypes.COMPONENT.get();
    }

    @Override
    public IChatRenderer<StringChatElement> getRenderer() {
        return ChatComponentRenderer.INSTANCE;
    }

    @Override
    public int wrapWidth(int input, Font font) {
        return input + font.width(component.getString());
    }

    @Override
    public int warpHeight(int input, Font font) {
        return input;
    }
}
