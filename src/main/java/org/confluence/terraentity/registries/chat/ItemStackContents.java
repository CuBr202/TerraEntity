package org.confluence.terraentity.registries.chat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class ItemStackContents implements ComponentContents {

    public static final MapCodec<ItemStackContents> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
//            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemStackContents::getItem)
//            ItemStack.CODEC.fieldOf("item").forGetter(ItemStackComponent::getItem)
            ResourceLocation.CODEC.fieldOf("item").forGetter(ItemStackContents::getItem)
    ).apply(instance, ItemStackContents::create));

    public static final ComponentContents.Type<ItemStackContents> TYPE = new ComponentContents.Type<>(CODEC, "component_item");

    ResourceLocation item;
//    ItemStack item;

    public ItemStackContents(ResourceLocation item) {
        this.item = item;
    }

    public static ItemStackContents create(ResourceLocation item) {
        return new ItemStackContents(item);
    }


    private ResourceLocation getItem() {
        return this.item;
    }

    @Override
    public String toString() {
        return "{" +
                " id:" + this.item +
                '}';
    }

    @Override
    public ComponentContents.@NotNull Type<?> type() {
        return TYPE;
    }

}
