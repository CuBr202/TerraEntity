package org.confluence.terraentity.data.gen.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import org.confluence.lib.common.data.gen.AbstractRecipeProvider;
import org.confluence.terraentity.entity.npc.NPCTrades;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.init.item.TEWhipItems;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade.variant.ItemListTradeItem;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeHealth;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItem;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItemTask;
import org.confluence.terraentity.registries.npc_trade_task.variant.ProgressTradeTask;

import java.util.ArrayList;
import java.util.List;
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
        this.npcShopPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "npc_shop");

    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider holderLookup) {

        add(TENpcEntities.GUIDE.getId()).addRecipe(builder()
                .add(new ItemStack(Blocks.OAK_SAPLING.asItem(), 1), Items.ARROW.getDefaultInstance())
                .add(new ItemStack(Blocks.TORCH.asItem(), 10), Items.ARROW.getDefaultInstance())
                .add(new ItemStack(Items.ARROW.asItem(), 10), Items.ARROW.getDefaultInstance())
                .add(new ItemStack(TEWhipItems.LEATHER_WHIP.get(), 1), Items.ARROW.getDefaultInstance())
                .build());

        add(TENpcEntities.DEMOLITIONIST.getId()).addRecipe(builder()
                .add(new ItemStack(Blocks.TNT.asItem(), 1), Items.EGG.getDefaultInstance())
                .build());

        add(TENpcEntities.NURSE.getId()).addRecipe(builder()
                .add(ItemTradeHealth.of(Items.EMERALD.getDefaultInstance(), 10))
                .add(new ItemStack(Items.ARROW.asItem(), 10), Items.ARROW.getDefaultInstance())
                .add(ItemListTradeItem.builder(Items.ARROW.asItem(), 10)
                        .addCost(Items.IRON_GOLEM_SPAWN_EGG, 1)
                        .addCost(Items.WITHER_SKELETON_SKULL, 1)
                        .addCost(Items.ZOMBIE_HEAD, 20)
                        .addCost(Items.SKELETON_HORSE_SPAWN_EGG, 1)
                        .addCost(Items.CREEPER_HEAD, 1)
                        .addCost(Items.SPIDER_EYE, 1)
                        .addCost(Items.BLAZE_ROD, 1)
                        .addCost(Items.GHAST_TEAR, 1)
                        .addCost(Items.ENDER_PEARL, 1)
                        .addCost(Items.MAGMA_CREAM, 1)
                        .build()
                )
                .build());

        add(TENpcEntities.ANGLER.getId()).addRecipe(builder()
                .add(ItemTradeItemTask.create(new ProgressTradeTask(List.of(
                        ItemTradeItem.of(Items.DIAMOND.getDefaultInstance(), Items.EMERALD.getDefaultInstance()),
                        ItemTradeHealth.of(Items.EMERALD.getDefaultInstance(), 10),
                        ItemTradeHealth.of(Items.EMERALD.getDefaultInstance(), 20),
                        ItemTradeHealth.of(Items.EMERALD.getDefaultInstance(), 30),
                        ItemTradeHealth.of(Items.EMERALD.getDefaultInstance(), 40),
                        ItemTradeHealth.of(Items.EMERALD.getDefaultInstance(), 50)
                ))))
                .build());

    }

    protected Appender<NPCTrades> add(ResourceLocation id) {
        return recipe(NPCTrades.CODEC, pathProvider().json(id));
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
            trades.add(new ItemTradeItem(it, cost));
            return this;
        }

        public Builder add(ITrade trade) {
            trades.add(trade);
            return this;
        }

        public NPCTrades build() {
            return new NPCTrades(trades);
        }
    }

    @Override
    protected PackOutput.PathProvider pathProvider() {
        return npcShopPathProvider;
    }

}