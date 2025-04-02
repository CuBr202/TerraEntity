package org.confluence.terraentity.data.enchantment;


import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.item.BaseWhipItem;
import org.confluence.terraentity.item.Boomerang;

import java.util.function.Supplier;

// 自定义附魔类，用于定义和注册新的附魔
public class TEEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, TerraEntity.MODID);

    public static final EnchantmentCategory WHIP = EnchantmentCategory.create("whip", item -> item instanceof BaseWhipItem);
    public static final EnchantmentCategory BOOMERANG = EnchantmentCategory.create("boomerang", item -> item instanceof Boomerang);



    public static final RegistryObject<Enchantment> WHIP_SWEEP = register("whip_sweep", () -> new Enchantment(Enchantment.Rarity.COMMON, WHIP, new EquipmentSlot[]{EquipmentSlot.MAINHAND}) {
        public int getMinCost(int pLevel) {
            return 25 + pLevel * 10;
        }
        public int getMaxCost(int pLevel) {
            return this.getMinCost(pLevel) + 25;
        }
        public Rarity getRarity() {
            return Rarity.RARE;
        }
    });
    public static final RegistryObject<Enchantment> MULTI_BOOMERANG = register("multi_boomerang", () -> new Enchantment(Enchantment.Rarity.COMMON, BOOMERANG, new EquipmentSlot[]{EquipmentSlot.MAINHAND}) {
        public int getMinCost(int pLevel) {
            return 25 + pLevel * 10;
        }
        public int getMaxCost(int pLevel) {
            return this.getMinCost(pLevel) + 25;
        }
        public Rarity getRarity() {
            return Rarity.RARE;
        }
    });



    private static RegistryObject<Enchantment> register(String pIdentifier, Supplier<Enchantment> pEnchantment) {
        return ENCHANTMENTS.register(pIdentifier, pEnchantment);
    }
    // *********************

    // 引导方法，用于初始化附魔注册
    public static void bootstrap(BootstapContext<Enchantment> context)
    {
        // 获取各种注册表的持有者获取器
        HolderGetter<net.minecraft.world.damagesource.DamageType> holdergetter = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<Enchantment> holdergetter1 = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> holdergetter2 = context.lookup(Registries.ITEM);
        HolderGetter<Block> holdergetter3 = context.lookup(Registries.BLOCK);

//        // 注册自定义附魔
//        register(context, WHIP_SWEEP, Enchantment.enchantment(Enchantment.definition(holdergetter2.getOrThrow(
//                TETags.Items.WHIP_ENCHANTABLE),
//                2,
//                1,
//                Enchantment.constantCost(25),
//                Enchantment.constantCost(50),
//                8,
//                EquipmentSlotGroup.MAINHAND
//        )));
//        register(context, MULTI_BOOMERANG, Enchantment.enchantment(Enchantment.definition(holdergetter2.getOrThrow(
//                TETags.Items.BOOMERANG_ENCHANTABLE),
//                2,
//                3,
//                Enchantment.constantCost(25),
//                Enchantment.constantCost(50),
//                8,
//                EquipmentSlotGroup.MAINHAND
//        )));

    }
}
