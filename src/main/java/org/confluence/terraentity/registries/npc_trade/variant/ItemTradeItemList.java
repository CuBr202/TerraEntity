package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.entity.npc.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade.IItemTrade;
import org.confluence.terraentity.registries.npc_trade.ITradeItemList;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade.TradeProviderTypes;

import java.util.List;

public record ItemTradeItemList(ItemStack cost, List<ItemStack> result) implements IItemTrade, ITradeItemList {

    public static MapCodec<ItemTradeItemList> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeItemList::cost),
            Codec.list(ItemStack.CODEC).fieldOf("result").forGetter(ItemTradeItemList::result)
    ).apply(instance, ItemTradeItemList::new));

    public static ItemTradeItemList of(ItemStack cost, List<ItemStack> result) {
        return new ItemTradeItemList(cost, result);
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
