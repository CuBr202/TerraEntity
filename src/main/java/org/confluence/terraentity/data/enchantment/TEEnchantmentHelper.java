package org.confluence.terraentity.data.enchantment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.function.Supplier;

/**
 * 辅助类，用于处理与附魔相关的逻辑。
 */
public class TEEnchantmentHelper {

    /**
     * 检查给定的物品堆栈的附魔等级。
     *
     * @param enchantments 附魔的类型
     * @param stack 要检查的物品堆栈。
     * @return 如果物品具有Adam附魔，则返回true，否则返回false。
     */
    public static int getEnchantmentLevel(Enchantment enchantments, ItemStack stack) {
        // 从物品堆栈中获取附魔信息，如果没有则使用空的附魔集合。
        var map = EnchantmentHelper.getEnchantments(stack);
        if(map.isEmpty())
            return 0;
        return map.get(enchantments);
    }

    public static int getEnchantmentLevel(Supplier<Enchantment> enchantments, ItemStack stack) {
        return getEnchantmentLevel(enchantments.get(), stack);
    }
}