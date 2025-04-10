package org.confluence.terraentity.data.gen.recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.JavaOps;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.lib.common.recipe.AbstractRecipeProvider;

import org.confluence.terraentity.entity.npc.NPCTrades;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.init.item.TEWhipItems;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成单个NPC单个配方
 * @see org.confluence.terraentity.registries.npc_trade.ITrade
 */
public class TENPCShopProvider extends AbstractRecipeProvider {

    public TENPCShopProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void run() {

        gen(TENpcEntities.GUIDE)
                .add(new ItemStack(Blocks.OAK_SAPLING.asItem(), 1), Items.ARROW.getDefaultInstance())
                .add(new ItemStack(Blocks.TORCH.asItem(), 10),  Items.ARROW.getDefaultInstance())
                .add(new ItemStack(Items.ARROW.asItem(), 10),  Items.ARROW.getDefaultInstance())
                .add(new ItemStack(TEWhipItems.LEATHER_WHIP.get(), 1),  Items.ARROW.getDefaultInstance())
                .build();
    }

    private <T extends Entity>Builder gen(DeferredHolder<EntityType<?>,EntityType<T>> entityType){
        return new Builder(entityType.getId());
    }


    private void genRecipe(NPCTrades trades, ResourceLocation location){
        JsonElement res = parseCodec(NPCTrades.CODEC.encodeStart(JavaOps.INSTANCE,trades));
        addJson(res.getAsJsonObject(),ItemStack.EMPTY,location.toString());
    }

    private class Builder {
        ResourceLocation location;
        private int money;
        private List<ItemTradeItem> trades;
        public Builder(ResourceLocation location){
            this.location = location;
            trades = new ArrayList<>();
        }
        public Builder add(ItemStack it, ItemStack cost){
            trades.add(new ItemTradeItem(it,cost));
            return this;
        }

        public void build(){
            genRecipe(new NPCTrades(trades),location);
        }

    }


    @Override
    public String getName() {
        return "NPC Shop";
    }

    protected Path getRoot(ResourceLocation loc){
        return this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(loc.getNamespace()).resolve(NPCTrades.KEY);
    }

    protected Path getPath(ResourceLocation loc, String nameSuffix) {
        return getRoot(loc).resolve(loc.getPath()+".json");
    }
}