package org.confluence.terraentity.data.enchantment;


import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.Block;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TETags;

// 自定义附魔类，用于定义和注册新的附魔
public class TEEnchantments {
    // 自定义附魔资源键
    public static final ResourceKey<Enchantment> WHIP_SWEEP = key("whip_sweep");
    public static final ResourceKey<Enchantment> MULTI_BOOMERANG = key("multi_boomerang");



    // *********************

    // 引导方法，用于初始化附魔注册
    public static void bootstrap(BootstrapContext<Enchantment> context)
    {
        // 获取各种注册表的持有者获取器
        HolderGetter<net.minecraft.world.damagesource.DamageType> holdergetter = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<Enchantment> holdergetter1 = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> holdergetter2 = context.lookup(Registries.ITEM);
        HolderGetter<Block> holdergetter3 = context.lookup(Registries.BLOCK);

        // 注册自定义附魔
        register(context, WHIP_SWEEP, Enchantment.enchantment(Enchantment.definition(holdergetter2.getOrThrow(
                TETags.Items.WHIP_ENCHANTABLE),
                2,
                1,
                Enchantment.constantCost(25),
                Enchantment.constantCost(50),
                8,
                EquipmentSlotGroup.MAINHAND
        )));
        register(context, MULTI_BOOMERANG, Enchantment.enchantment(Enchantment.definition(holdergetter2.getOrThrow(
                TETags.Items.BOOMERANG_ENCHANTABLE),
                2,
                3,
                Enchantment.constantCost(25),
                Enchantment.constantCost(50),
                8,
                EquipmentSlotGroup.MAINHAND
        )));

    }

    // 注册附魔的方法
    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }

    // 创建附魔资源键的方法
    private static ResourceKey<Enchantment> key(String name)
    {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(TerraEntity.MODID,name));
    }
}
