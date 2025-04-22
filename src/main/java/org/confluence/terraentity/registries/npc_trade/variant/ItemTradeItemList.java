package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade.*;

import java.util.List;
import java.util.Optional;

public record ItemTradeItemList(ItemStack cost,
                                List<ItemStack> result,
                                TradeProperties properties
) implements IItemTrade, ITradeItemList {

    public static MapCodec<ItemTradeItemList> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeItemList::cost),
            Codec.list(ItemStack.CODEC).fieldOf("result").forGetter(ItemTradeItemList::result),
            TradeProperties.CODEC.optionalFieldOf("properties").forGetter(i->Optional.ofNullable(i.properties))
    ).apply(instance, (cost, result, lock)->new ItemTradeItemList(
            cost,
            result,
            lock.orElse(null)
    )));

    public static ItemTradeItemList of(ItemStack cost, List<ItemStack> result) {
        return new ItemTradeItemList(cost, result, null);
    }
    public static ItemTradeItemList of(ItemStack cost, List<ItemStack> result, TradeProperties properties) {
        return new ItemTradeItemList(cost, result, properties);
    }

    @Override
    public void onTrade(ServerPlayer player, ITradeHolder npc, int index) {
        IItemTrade.super.onTrade(player, npc, index);
        ITradeItemList.super.onTrade(player, npc, index);
    }


    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_TRADE_ITEM_LIST.get();
    }
}
