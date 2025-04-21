package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade.*;

import java.util.Optional;

/**
 * 单个物品交换单个物品
 * @param result
 * @param cost
 */
public record ItemTradeItem(ItemStack result, ItemStack cost, TradeProperties properties) implements ITradeItem, IItemTrade {

    public static final MapCodec<ItemTradeItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("result").forGetter(ItemTradeItem::result),
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeItem::cost),
            TradeProperties.CODEC.optionalFieldOf("properties").forGetter(i->Optional.ofNullable(i.properties))
    ).apply(instance, (result, cost, lock)-> new ItemTradeItem(
            result,
            cost,
            lock.orElse(null)
    )));

    public static ItemTradeItem of(ItemStack result, ItemStack cost) {
        return new ItemTradeItem(result, cost, null);
    }

    public static ItemTradeItem of(ItemLike result, int resultCount, ItemLike cost, int costCount) {
        return new ItemTradeItem(new ItemStack(result.asItem(), resultCount), new ItemStack(cost, costCount), null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private ItemStack result;
        private ItemStack cost;
        private TradeProperties properties;
        public Builder setResult(ItemStack result) {
            this.result = result;
            return this;
        }
        public Builder setResult(ItemLike result, int resultCount) {
            this.result = new ItemStack(result.asItem(), resultCount);
            return this;
        }

        public Builder setCost(ItemStack cost) {
            this.cost = cost;
            return this;
        }

        public Builder setCost(ItemLike cost, int costCount) {
            this.cost = new ItemStack(cost.asItem(), costCount);
            return this;
        }
        public Builder setProperties(TradeProperties properties) {
            this.properties = properties;
            return this;
        }
        public ItemTradeItem build() {
            return new ItemTradeItem(result, cost, properties);
        }
    }

    @Override
    public void onTrade(ServerPlayer player, ITradeHolder npc, int index) {
        ITradeItem.super.onTrade(player, npc, index);
        IItemTrade.super.onTrade(player, npc, index);
    }

    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_TRADE_ITEM.get();
    }

}