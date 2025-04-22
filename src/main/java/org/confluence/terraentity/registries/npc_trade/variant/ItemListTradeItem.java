package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ItemListTradeItem(List<ItemStack> costs, ItemStack result, TradeProperties properties) implements ITradeItem, IItemListTrade {

    public static final MapCodec<ItemListTradeItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("costs").forGetter(ItemListTradeItem::costs),
            ItemStack.CODEC.fieldOf("result").forGetter(ItemListTradeItem::result),
            TradeProperties.CODEC.optionalFieldOf("properties").forGetter(i-> Optional.ofNullable(i.properties))

    ).apply(instance, (costs, result, properties)->new ItemListTradeItem(
            costs,
            result,
            properties.orElse(null)
    )));

    public static Builder builder(ItemStack result){
        return new Builder(result);
    }

    public static Builder builder(ItemLike result, int count){
        return new Builder(result, count);
    }

    public static class Builder{
        private final ItemStack result;
        private final List<ItemStack> costs;
        private TradeProperties properties;

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

        public Builder setProperties(TradeProperties properties){
            this.properties = properties;
            return this;
        }

        public ItemListTradeItem build(){
            return new ItemListTradeItem(costs, result, properties);
        }
    }

    @Override
    public void onTrade(ServerPlayer player, ITradeHolder npc, int index) {
        ITradeItem.super.onTrade(player, npc, index);
        IItemListTrade.super.onTrade(player, npc, index);
    }

    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_LIST_TRADE_ITEM.get();
    }

}