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
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 生成单个NPC单个配方
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

        add(TENpcEntities.GUIDE.getId()).addRecipe(new Builder()
                .add(new ItemStack(Blocks.OAK_SAPLING.asItem(), 1), Items.ARROW.getDefaultInstance())
                .add(new ItemStack(Blocks.TORCH.asItem(), 10),  Items.ARROW.getDefaultInstance())
                .add(new ItemStack(Items.ARROW.asItem(), 10),  Items.ARROW.getDefaultInstance())
                .add(new ItemStack(TEWhipItems.LEATHER_WHIP.get(), 1),  Items.ARROW.getDefaultInstance())
                .build());

        add(TENpcEntities.DEMOLITIONIST.getId()).addRecipe(new Builder()
                .add(new ItemStack(Blocks.TNT.asItem(), 1), Items.EGG.getDefaultInstance())
                .build());
    }

    private Appender<NPCTrades> add(ResourceLocation id){
        return recipe(NPCTrades.CODEC, pathProvider().json(id));
    }


    public static class Builder {
        private final List<ItemTradeItem> trades;

        public Builder() {
            this.trades = new ArrayList<>();
        }

        public Builder add(ItemStack it, ItemStack cost) {
            trades.add(new ItemTradeItem(it, cost));
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