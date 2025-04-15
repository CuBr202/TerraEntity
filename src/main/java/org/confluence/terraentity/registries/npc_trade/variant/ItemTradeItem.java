package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.confluence.terraentity.registries.npc_trade.IItemTrade;
import org.confluence.terraentity.registries.npc_trade.ITradeItem;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade.TradeProviderTypes;

/**
 * 单个物品交换单个物品
 * @param result
 * @param cost
 */
public record ItemTradeItem(ItemStack result, ItemStack cost) implements ITradeItem, IItemTrade {

    public static final MapCodec<ItemTradeItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("result").forGetter(ItemTradeItem::result),
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeItem::cost)
    ).apply(instance, ItemTradeItem::new));

    public static ItemTradeItem of(ItemStack result, ItemStack cost) {
        return new ItemTradeItem(result, cost);
    }
    public static ItemTradeItem of(ItemLike result, int resultCount, ItemLike cost, int costCount) {
        return new ItemTradeItem(new ItemStack(result.asItem(), resultCount), new ItemStack(cost, costCount));
    }

    @Override
    public void onTrade(ServerPlayer player) {
        ITradeItem.super.onTrade(player);
        IItemTrade.super.onTrade(player);
    }

    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_TRADE_ITEM.get();
    }

}