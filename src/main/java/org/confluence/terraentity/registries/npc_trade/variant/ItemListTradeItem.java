package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.registries.npc_trade.*;
import org.confluence.terraentity.utils.TEUtils;

import java.util.ArrayList;
import java.util.List;

public record ItemListTradeItem(ItemStack result, List<ItemStack> costs) implements ITradeItem, IItemListTrade {

    public static final MapCodec<ItemListTradeItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("result").forGetter(ItemListTradeItem::result),
            ItemStack.CODEC.listOf().fieldOf("costs").forGetter(ItemListTradeItem::costs)
    ).apply(instance, ItemListTradeItem::new));

    public static Builder builder(ItemStack result){
        return new Builder(result);
    }

    public static Builder builder(ItemLike result, int count){
        return new Builder(result, count);
    }

    public static class Builder{
        private final ItemStack result;
        private final List<ItemStack> costs;

        public Builder(ItemStack result){
            this.result = result;
            this.costs = new ArrayList<>();
        }

        public Builder(ItemLike result, int count){
            this(new ItemStack(result, count));
        }

        public Builder addCost(ItemStack cost){
            if(!cost.isEmpty())
                this.costs.add(cost);
            return this;
        }

        public Builder addCost(ItemLike cost, int count){
            return addCost(new ItemStack(cost, count));
        }

        public ItemListTradeItem build(){
            return new ItemListTradeItem(result, costs);
        }
    }

    @Override
    public void onTrade(ServerPlayer player, AbstractTerraNPC npc, int index) {
        ITradeItem.super.onTrade(player, npc, index);
        IItemListTrade.super.onTrade(player, npc, index);
    }

    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_LIST_TRADE_ITEM.get();
    }

}