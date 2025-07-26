package org.confluence.terraentity.registries.chat.variant;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.client.gui.renderer.chat.element.ChatItemRenderer;
import org.confluence.terraentity.registries.chat.ChatElementProvider;
import org.confluence.terraentity.registries.chat.ChatProviderTypes;
import org.confluence.terraentity.api.npc.chat.IChatElement;
import org.confluence.terraentity.api.npc.chat.IChatRenderer;

public class ItemChatElement implements IChatElement<ItemStack> {
    public static final MapCodec<ItemChatElement> MAPCODEC = ItemStack.CODEC.xmap(ItemChatElement::new, ItemChatElement::getContent).fieldOf("content");
    ItemStack content;
    public ItemChatElement(ItemStack content) {
        this.content = content;
    }

    @Override
    public ItemStack getContent() {
        return content;
    }

    @Override
    public ChatElementProvider getProvider() {
        return ChatProviderTypes.ITEM.get();
    }

    @Override
    public IChatRenderer<ItemChatElement> getRenderer() {
        return ChatItemRenderer.INSTANCE;
    }

    @Override
    public int wrapWidth(int input, Font font) {
        return input + 12;
    }

    @Override
    public int warpHeight(int input, Font font) {
        return input;
    }
}
