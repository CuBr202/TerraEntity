package org.confluence.terraentity.data.gen.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.crafting.Ingredient;

import org.confluence.lib.common.recipe.AmountIngredient;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.gen.AbstractExistCodecProvider;
import org.confluence.terraentity.data.gen.loot.TENPCLoot;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.init.item.TESpawnEggItems;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade.TradeProperties;
import org.confluence.terraentity.registries.npc_trade.variant.*;
import org.confluence.terraentity.registries.npc_trade_list.ITradeGenerator;
import org.confluence.terraentity.registries.npc_trade_list.variant.WeightMapGenerator;
import org.confluence.terraentity.registries.npc_trade_lock.variant.TimeLock;
import org.confluence.terraentity.registries.npc_trade_task.variant.DynamicAnglerTradeTask;
import org.confluence.terraentity.registries.npc_trade_task.variant.DynamicPoolTradeTask;
import org.confluence.terraentity.utils.TEItemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 生成单个NPC单个配方
 *
 * @see org.confluence.terraentity.registries.npc_trade.ITrade
 */
public class TENPCShopProvider extends AbstractExistCodecProvider<NPCTradeManager> {

    private final PackOutput.PathProvider npcShopPathProvider;

    public TENPCShopProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
        this.npcShopPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, NPCTradeManager.KEY);

    }

    @Override
    protected void run() {
        this.buildRecipes(null, this.lookupProvider);
    }

    @Override
    protected Codec<NPCTradeManager> getCodec() {
        return NPCTradeManager.CODEC;
    }

//    @Override
    public void buildRecipes(RecipeOutput recipeOutput, CompletableFuture<HolderLookup.Provider>  holderLookup) {



        shop(TENpcEntities.DEMOLITIONIST.getId(),builder()
                .add(ItemTradeItemList.builder().addCost(Items.GUNPOWDER,2).addCost(Items.IRON_INGOT,1).addResult(Items.TNT, 1).build())
                .add(ItemTradeItemList.builder().addCost(Items.GUNPOWDER,1).addResult(TEItemUtil.make(Items.FIREWORK_ROCKET, 1, stack -> stack.set(DataComponents.FIREWORKS, new Fireworks(1, List.of())))).build())
                .add(ItemTradeItemList.builder().addCost(Items.GUNPOWDER,2).addResult(TEItemUtil.make(Items.FIREWORK_ROCKET, 1, stack -> stack.set(DataComponents.FIREWORKS, new Fireworks(2, List.of())))).build())
                .add(ItemTradeItemList.builder().addCost(Items.GUNPOWDER,2).addCost(Items.COAL, 1).addResult(TEItemUtil.make(Items.FIREWORK_ROCKET, 1, stack -> stack.set(DataComponents.FIREWORKS, new Fireworks(3, List.of())))).build())
                .build());

        shop(TENpcEntities.NURSE.getId(),builder()
                .add(new ItemTradeHealth(List.of(new AmountIngredient(Ingredient.of(Items.EMERALD.getDefaultInstance()),1 )), 10, null))
                .add(ItemTradeItemList.builder().addCost(Items.RED_MUSHROOM,1).addCost(Items.BROWN_MUSHROOM,1).addResult(TEItemUtil.make(Items.POTION, 1, stack -> stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withPotion(Potions.HEALING)))).build())
                .add(ItemTradeItemList.builder().addCost(Items.RED_MUSHROOM,1).addCost(Items.BROWN_MUSHROOM,1).addCost(Items.WARPED_FUNGUS, 1).addResult(TEItemUtil.make(Items.POTION, 1, stack -> stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withPotion(Potions.STRONG_HEALING)))).build())
                .build());

        shop(TENpcEntities.ANGLER.getId(),builder()
                .add(TradeTask.create(
                        DynamicAnglerTradeTask.builder(
                                ItemTradeLootTable.builder()
                                        .addCost(Items.COD, 1)
                                        .setLootTable(TENPCLoot.Angler.location())
                                        .setSprite(TerraEntity.space("random_gift"))
                                        .build(),
                                        List.of(Items.COD.getDefaultInstance(), Items.PUFFERFISH.getDefaultInstance(), Items.TROPICAL_FISH.getDefaultInstance(), Items.SALMON.getDefaultInstance(),Items.INK_SAC.getDefaultInstance(), Items.GLOW_INK_SAC.getDefaultInstance() )
                                )
                                .addResult(1, List.of(Items.FISHING_ROD.getDefaultInstance()))
//                                .addResult(1, List.of(TEItemUtil.make(Items.ENCHANTED_BOOK, 1, stack-> stack.enchant(enchantmentLookup.get(Enchantments.LURE).get(),1)), TEItemUtil.make(Items.ENCHANTED_BOOK, 1, stack-> stack.enchant(enchantmentLookup.get(Enchantments.LUCK_OF_THE_SEA).get(),1))))

                                .addResult(5, List.of(Items.BUCKET.getDefaultInstance()))
//                                .addResult(10, List.of(TEItemUtil.make(Items.ENCHANTED_BOOK, 1, stack-> stack.enchant(enchantmentLookup.get(Enchantments.LURE).get(),1)), TEItemUtil.make(Items.ENCHANTED_BOOK, 1, stack-> stack.enchant(enchantmentLookup.get(Enchantments.LUCK_OF_THE_SEA).get(),1))))
                                .addResult(15, List.of(TEItemUtil.make(Items.IRON_INGOT,20)))
//                                .addResult(20, List.of(TEItemUtil.make(Items.ENCHANTED_BOOK, 1, stack-> stack.enchant(enchantmentLookup.get(Enchantments.LURE).get(),2)),TEItemUtil.make(Items.ENCHANTED_BOOK, 1, stack-> stack.enchant(enchantmentLookup.get(Enchantments.LUCK_OF_THE_SEA).get(),2))))
                                .addResult(25, List.of(TEItemUtil.make(Items.GOLD_INGOT,20)))
//                                .addResult(30, List.of(TEItemUtil.make(Items.ENCHANTED_BOOK, 1, stack-> stack.enchant(enchantmentLookup.get(Enchantments.LURE).get(),3)),TEItemUtil.make(Items.ENCHANTED_BOOK, 1, stack-> stack.enchant(enchantmentLookup.get(Enchantments.LUCK_OF_THE_SEA).get(),3))))
                                .addResult(35, List.of(TEItemUtil.make(Items.DIAMOND,20)))
//                                .addResult(40, List.of(TEItemUtil.make(Items.ENCHANTED_BOOK, 1, stack-> stack.enchant(enchantmentLookup.get(Enchantments.MENDING).get(),1))))
                                .addResult(45, List.of(TEItemUtil.make(Items.EMERALD,20)))
//                                .addResult(50, List.of(TEItemUtil.make(Items.NETHERITE_INGOT,5), TEItemUtil.make(TEBoomerangItems.FLAMARANG.get(), 1, stack->stack.enchant(enchantmentLookup.get(TEEnchantments.MULTI_BOOMERANG).get(),1))))
                                .addResult(55, List.of(TEItemUtil.make(Items.NETHERITE_INGOT,5)))
                                .setTitle("title.terra_entity.npc_trade.task.fishman")
                                .build()
                ))
                .build());

        shop(TENpcEntities.DRYAD.getId(),builder()
                .add(ItemTradeItemList.builder().addCost(Items.OAK_SAPLING, 2).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build())
                .add(ItemTradeItemList.builder().addCost(Items.SPRUCE_SAPLING, 2).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build())
                .add(ItemTradeItemList.builder().addCost(Items.BIRCH_SAPLING, 2).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build())
                .add(ItemTradeItemList.builder().addCost(Items.JUNGLE_SAPLING, 2).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build())
                .add(ItemTradeItemList.builder().addCost(Items.ACACIA_SAPLING, 2).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build())
                .add(ItemTradeItemList.builder().addCost(Items.DARK_OAK_SAPLING, 2).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build())
                .build());

        shop(TENpcEntities.MERCHANT.getId(),builder()
                .add(ItemTradeItemList.builder().addCost(Items.IRON_INGOT, 1).addResult(Items.COAL, 2).build())
                .add(ItemTradeItemList.builder().addCost(Items.GOLD_INGOT, 1).addResult(Items.IRON_INGOT, 2).build())
                .add(ItemTradeItemList.builder().addCost(Items.DIAMOND, 1).addResult(Items.GOLD_INGOT, 2).build())
                .add(ItemTradeItemList.builder().addCost(Items.EMERALD, 1).addResult(Items.DIAMOND, 2).build())
                .add(ItemTradeItemList.builder().addCost(Items.NETHERITE_INGOT, 1).addResult(Items.EMERALD, 4).build())

                .add(ItemTradeItemList.builder().addCost(Items.COAL, 3).addResult(Items.IRON_INGOT, 1).build())
                .add(ItemTradeItemList.builder().addCost(Items.IRON_INGOT, 3).addResult(Items.GOLD_INGOT, 1).build())
                .add(ItemTradeItemList.builder().addCost(Items.GOLD_INGOT, 3).addResult(Items.DIAMOND, 1).build())
                .add(ItemTradeItemList.builder().addCost(Items.DIAMOND, 3).addResult(Items.EMERALD, 1).build())
                .add(ItemTradeItemList.builder().addCost(Items.EMERALD, 5).addResult(Items.NETHERITE_INGOT, 1).build())

                .add(ItemTradeItemList.builder().addCost(Items.DIAMOND, 5).addCost(Items.EGG, 1).addResult(TESpawnEggItems.KING_SLIME_SPAWN_EGG.get().getDefaultInstance()).build())
                .add(ItemTradeItemList.builder().addCost(Items.REDSTONE, 20).addCost(Items.EGG, 1).addResult(TESpawnEggItems.EYE_OF_CTHULHU_SPAWN_EGG.get().getDefaultInstance()).build())

                .build());

        shop(TENpcEntities.ARMS_DEALER.getId(),builder()
                .add(ItemTradeItemList.builder().addCost(Items.ROTTEN_FLESH, 1).addCost(Items.STRING, 2).addResult(Items.BOW.getDefaultInstance()).build())
                .add(ItemTradeItemList.builder().addCost(Items.BONE, 3).addCost(Items.STRING, 2).addResult(Items.CROSSBOW.getDefaultInstance()).build())
                .add(ItemTradeItemList.builder().addCost(Items.ARROW, 4).addCost(Items.SPIDER_EYE, 1).addResult(TEItemUtil.make(Items.TIPPED_ARROW, 4, stack -> stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withPotion(Potions.POISON)))).build())
                .add(ItemTradeItemList.builder().addCost(Items.ARROW, 4).addCost(Items.SPIDER_EYE, 1).addResult(TEItemUtil.make(Items.TIPPED_ARROW, 4, stack -> stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withPotion(Potions.SLOWNESS)))).build())
                .add(ItemTradeItemList.builder().addCost(Items.ARROW, 4).addCost(Items.PHANTOM_MEMBRANE, 1).addResult(TEItemUtil.make(Items.TIPPED_ARROW, 4, stack -> stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withPotion(Potions.SLOW_FALLING)))).build())
                .build());


        shop(TENpcEntities.GUIDE.getId(),builder()

                .add(TradeTask.create(new DynamicPoolTradeTask(
                        ItemTradeLootTable.builder()
                                .addCost(Items.APPLE, 1)
                                .setLootTable(TerraEntity.fromSpaceAndPath("minecraft", "entities/zombie"))
                                .setSprite(TerraEntity.space("random_gift"))
                                .setTranslationKey("angler_gift")
                                .setProperties(TradeProperties.builder()
                                        .setLock(new TimeLock(0,10000, true))
                                        .build())
                                .build()
                        ,
                        Map.of(
                                1, List.of(Items.DIAMOND.getDefaultInstance(), Items.EMERALD.getDefaultInstance()),
                                3, List.of(Items.ICE.getDefaultInstance(), Items.EMERALD.getDefaultInstance())
                        ),
                        List.of(
                                Items.DIRT.getDefaultInstance(),
                                Items.ICE.getDefaultInstance()
                        )
                )))

                .add(new ItemTradeItemList(
                        List.of(
                                new AmountIngredient(Ingredient.of(Items.APPLE),5),
                                new AmountIngredient(Ingredient.of(ItemTags.PLANKS),10)
                        ),
                        List.of(
                                new ItemStack(Items.APPLE),
                                new ItemStack(Items.DIAMOND,3),
                                new ItemStack(Items.EMERALD,5)
                        ),
                        new TradeProperties(new TimeLock(0, 12000, true)))
                )

                .build());

        shop(TENpcEntities.PAINTER.getId(),new NPCTradeManager(
                WeightMapGenerator.builder(3)

                        .addTrade(ItemTradeItemList.builder().addCost(Items.JUNGLE_SAPLING, 2).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .addTrade(ItemTradeItemList.builder().addCost(Items.ACACIA_SAPLING, 2).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .addTrade(ItemTradeItemList.builder().addCost(Items.DARK_OAK_SAPLING, 2).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .addTrade(ItemTradeItemList.builder().addCost(Items.GRASS_BLOCK, 1).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .addTrade(ItemTradeItemList.builder().addCost(Items.SAND, 1).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .addTrade(ItemTradeItemList.builder().addCost(Items.RED_SAND, 1).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .build()
        ));

        shop(TENpcEntities.DYE_TRADER.getId(),new ComplexBuilder(
                WeightMapGenerator.builder(3)

                        .addTrade(ItemTradeItemList.builder().addCost(Items.ACACIA_WOOD, 1).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .addTrade(ItemTradeItemList.builder().addCost(Items.ACACIA_SAPLING, 2).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .addTrade(ItemTradeItemList.builder().addCost(Items.DARK_OAK_SAPLING, 2).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .addTrade(ItemTradeItemList.builder().addCost(Items.GRASS_BLOCK, 1).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .addTrade(ItemTradeItemList.builder().addCost(Items.SAND, 1).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .addTrade(ItemTradeItemList.builder().addCost(Items.RED_SAND, 1).addCost(Items.COAL, 1).addResult(Items.EMERALD, 1).build(), 1)
                        .build()
        ).build());

    }

//    protected Appender<NPCTradeManager> shop(ResourceLocation id) {
//        return recipe(NPCTradeManager.CODEC, pathProvider().json(id));
//    }

    protected void shop(ResourceLocation id, NPCTradeManager manager) {
        this.gen(id, manager);
    }
    protected Builder builder() {
        return new Builder();
    }

    @Override
    public String getName() {
        return "npc_shop";
    }

    public static class Builder {
        private final List<ITrade> trades;

        public Builder() {
            this.trades = new ArrayList<>();
        }

        public Builder add(ITrade trade) {
            trades.add(trade);
            return this;
        }

        public NPCTradeManager build() {
            return new NPCTradeManager(trades);
        }
    }

    /**
     * 生成未初始化的NPCTradeManager，用于生成随机的交易表
     */
    public static class ComplexBuilder {

        ITradeGenerator list;
        public ComplexBuilder(ITradeGenerator list) {
            this.list = list;
        }

        public NPCTradeManager build() {
            return new NPCTradeManager(list);
        }
    }

//    @Override
    protected PackOutput.PathProvider pathProvider() {
        return npcShopPathProvider;
    }

}