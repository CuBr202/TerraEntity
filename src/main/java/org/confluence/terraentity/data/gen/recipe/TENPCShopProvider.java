package org.confluence.terraentity.data.gen.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.block.Blocks;

import org.confluence.lib.common.data.gen.AbstractRecipeProvider;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.trade.NPCTradeManager;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.init.item.TESpawnEggItems;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade.TradeProperties;
import org.confluence.terraentity.registries.npc_trade.variant.*;
import org.confluence.terraentity.registries.npc_trade_lock.variant.TimeLock;
import org.confluence.terraentity.registries.npc_trade_task.variant.DynamicAnglerTradeTask;
import org.confluence.terraentity.registries.npc_trade_task.variant.FixedMapTradeTask;
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
public class TENPCShopProvider extends AbstractRecipeProvider {

    private final PackOutput.PathProvider npcShopPathProvider;

    public TENPCShopProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
        this.npcShopPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, NPCTradeManager.KEY);

    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider holderLookup) {

        shop(TENpcEntities.DEMOLITIONIST.getId()).addRecipe(builder()
                .add(ItemTradeItem.of(Blocks.TNT.asItem(), 1, Items.IRON_INGOT,1))
                .add(ItemTradeItem.builder()
                        .setCost(Items.IRON_INGOT.getDefaultInstance())
                        .setResult(TEItemUtil.make(Items.FIREWORK_ROCKET, 1, stack ->
                                        stack.set(DataComponents.FIREWORKS, new Fireworks(1, List.of()))
                        )).build()
                )
                .add(ItemTradeItem.builder()
                        .setCost(Items.IRON_INGOT.getDefaultInstance())
                        .setResult(TEItemUtil.make(Items.FIREWORK_ROCKET, 1, stack ->
                                stack.set(DataComponents.FIREWORKS, new Fireworks(1, List.of()))
                        )).build()
                )
                .build());

        shop(TENpcEntities.NURSE.getId()).addRecipe(builder()
                .add(ItemTradeHealth.of(Items.EMERALD.getDefaultInstance(), 10))
                .add(ItemListTradeItem.builder(TEItemUtil.make(Items.POTION, 1, stack -> stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withPotion(Potions.HEALING))))
                        .addCost(Items.RED_MUSHROOM,1)
                        .addCost(Items.BROWN_MUSHROOM,1)
                        .build()
                )
                .add(ItemListTradeItem.builder(TEItemUtil.make(Items.POTION, 1, stack -> stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withPotion(Potions.STRONG_HEALING))))
                        .addCost(Items.RED_MUSHROOM,1)
                        .addCost(Items.BROWN_MUSHROOM,1)
                        .addCost(Items.WARPED_FUNGUS, 1)
                        .build()
                )
                .build());

        shop(TENpcEntities.ANGLER.getId()).addRecipe(builder()
                .add(TradeTask.create(
                        DynamicAnglerTradeTask.builder(
                                ItemTradeLootTable.builder()
                                        .setCost(Items.COD, 1)
                                        .setLootTable(TerraEntity.fromSpaceAndPath("minecraft", "entities/zombie"))
                                        .setSprite(TerraEntity.space("random_gift"))
                                        .build(),
                                        List.of(Items.DIRT.getDefaultInstance(), Items.ICE.getDefaultInstance(), Items.EMERALD.getDefaultInstance())
                                )
                                .addResult(1, List.of(Items.DIAMOND.getDefaultInstance(), Items.EMERALD.getDefaultInstance()))
                                .addResult(3, List.of(Items.ICE.getDefaultInstance(), Items.EMERALD.getDefaultInstance()))
//                                .setTitle("title.terra_entity.npc_trade.task.fishman")
                                .build()
                ))
                .build());

        shop(TENpcEntities.DRYAD.getId()).addRecipe(builder()
                .add(ItemTradeItem.of(Items.OAK_SAPLING, 2, Items.COAL,1))
                .add(ItemTradeItem.of(Items.SPRUCE_SAPLING, 2, Items.COAL,1))
                .add(ItemTradeItem.of(Items.BIRCH_SAPLING, 2, Items.COAL,1))
                .add(ItemTradeItem.of(Items.JUNGLE_SAPLING, 2, Items.COAL,1))
                .add(ItemTradeItem.of(Items.ACACIA_SAPLING, 2, Items.COAL,1))
                .add(ItemTradeItem.of(Items.DARK_OAK_SAPLING, 2, Items.COAL,1))
                .build());

        shop(TENpcEntities.MERCHANT.getId()).addRecipe(builder()
                .add(ItemTradeItem.of(Items.IRON_INGOT, 1, Items.COAL,2))
                .add(ItemTradeItem.of(Items.GOLD_INGOT, 1, Items.IRON_INGOT,2))
                .add(ItemTradeItem.of(Items.DIAMOND, 1, Items.GOLD_INGOT,2))
                .add(ItemTradeItem.of(Items.EMERALD, 1, Items.DIAMOND,2))
                .add(ItemTradeItem.of(Items.NETHERITE_INGOT, 1, Items.EMERALD,5))
                .add(ItemTradeItem.of(Items.COAL, 3, Items.IRON_INGOT,1))
                .add(ItemTradeItem.of(Items.IRON_INGOT, 3, Items.GOLD_INGOT,1))
                .add(ItemTradeItem.of(Items.GOLD_INGOT, 3, Items.DIAMOND,1))
                .add(ItemTradeItem.of(Items.DIAMOND, 3, Items.EMERALD,1))
                .add(ItemTradeItem.of(Items.EMERALD, 8, Items.NETHERITE_INGOT,1))


                .add(ItemListTradeItem.builder(TESpawnEggItems.KING_SLIME_SPAWN_EGG.get().getDefaultInstance())
                        .addCost(Items.DIAMOND,5)
                        .addCost(Items.EGG, 1)
                        .build()
                )
                .add(ItemListTradeItem.builder(TESpawnEggItems.EYE_OF_CTHULHU_SPAWN_EGG.get().getDefaultInstance())
                        .addCost(Items.REDSTONE,20)
                        .addCost(Items.EGG, 1)
                        .build()
                )

                .build());

        shop((TENpcEntities.ARMS_DEALER.getId())).addRecipe(builder()
                .add(ItemListTradeItem.builder(Items.BOW.getDefaultInstance())
                        .addCost(Items.ROTTEN_FLESH, 1)
                        .addCost(Items.STRING, 2)
                        .build()
                )
                .add(ItemTradeItem.of(Items.BONE, 1, Items.ARROW, 5))
                .add(ItemListTradeItem.builder(Items.CROSSBOW.getDefaultInstance())
                        .addCost(Items.BONE, 3)
                        .addCost(Items.STRING, 2)
                        .build()
                )
                .add(ItemListTradeItem.builder(TEItemUtil.make(Items.TIPPED_ARROW, 4, stack -> stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withPotion(Potions.POISON))))
                        .addCost(Items.ARROW, 4)
                        .addCost(Items.SPIDER_EYE, 1)
                        .build()
                )
                .add(ItemListTradeItem.builder(TEItemUtil.make(Items.TIPPED_ARROW, 4, stack -> stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withPotion(Potions.SLOWNESS))))
                        .addCost(Items.ARROW, 4)
                        .addCost(Items.SPIDER_EYE, 1)
                        .build()
                )
                .add(ItemListTradeItem.builder(TEItemUtil.make(Items.TIPPED_ARROW, 4, stack -> stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withPotion(Potions.SLOW_FALLING))))
                        .addCost(Items.ARROW, 4)
                        .addCost(Items.PHANTOM_MEMBRANE, 1)
                        .build()
                )
                .build());


        shop(TENpcEntities.GUIDE.getId()).addRecipe(builder()
                .add(TradeTask.create(new FixedMapTradeTask(
                        Map.of(
                                1, ItemTradeItemList.of(Items.APPLE.getDefaultInstance(),
                                        List.of(Items.DIAMOND.getDefaultInstance(), Items.EMERALD.getDefaultInstance())),
                                3, ItemTradeItemList.of(Items.APPLE.getDefaultInstance(),
                                        List.of(Items.ICE.getDefaultInstance(), Items.EMERALD.getDefaultInstance()))
                        ),
                        ItemTradeLootTable.builder()
                                .setCost(Items.APPLE, 1)
                                .setLootTable(TerraEntity.fromSpaceAndPath("minecraft", "entities/zombie"))
                                .setSprite(TerraEntity.space("random_gift"))
                                .setTranslationKey("apple_gift")
                                .build()
                )))

                .add(TradeTask.create(new DynamicPoolTradeTask(
                        ItemTradeLootTable.builder()
                                .setCost(Items.APPLE, 1)
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
                                Items.ICE.getDefaultInstance(),
                                Items.EMERALD.getDefaultInstance()
                        )
                )))
                .build());

    }

    protected Appender<NPCTradeManager> shop(ResourceLocation id) {
        return recipe(NPCTradeManager.CODEC, pathProvider().json(id));
    }

    protected Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<ITrade> trades;

        public Builder() {
            this.trades = new ArrayList<>();
        }

        public Builder add(ItemStack it, ItemStack cost) {
            trades.add(ItemTradeItem.of(it, cost));
            return this;
        }

        public Builder add(ITrade trade) {
            trades.add(trade);
            return this;
        }

        public NPCTradeManager build() {
            return new NPCTradeManager(trades);
        }
    }

    @Override
    protected PackOutput.PathProvider pathProvider() {
        return npcShopPathProvider;
    }

}