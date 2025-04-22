package org.confluence.terraentity.utils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class TEItemUtil {
    public static ItemStack make(Item item, int count, Consumer<ItemStack> modifier){
        ItemStack stack = new ItemStack(item, count);
        modifier.accept(stack);
        return stack;
    }
}
