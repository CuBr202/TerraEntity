package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade.IItemTrade;
import org.confluence.terraentity.registries.npc_trade.ITradeItem;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade.TradeProviderTypes;
import org.confluence.terraentity.registries.npc_trade_lock.ITradeLock;

import java.util.Optional;

/**
 * 单个物品交换单个物品
 * @param result
 * @param cost
 */
public record ItemTradeItem(ItemStack result, ItemStack cost, ITradeLock lock) implements ITradeItem, IItemTrade {

    public static final MapCodec<ItemTradeItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("result").forGetter(ItemTradeItem::result),
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeItem::cost),
            ITradeLock.TYPED_CODEC.optionalFieldOf("lock").forGetter(i-> Optional.ofNullable(i.lock))
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


    public static ItemTradeItem of(ItemStack result, ItemStack cost, ITradeLock lock) {
        return new ItemTradeItem(result, cost, lock);
    }
    public static ItemTradeItem of(ItemLike result, int resultCount, ItemLike cost, int costCount, ITradeLock lock) {
        return new ItemTradeItem(new ItemStack(result.asItem(), resultCount), new ItemStack(cost, costCount), lock);
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