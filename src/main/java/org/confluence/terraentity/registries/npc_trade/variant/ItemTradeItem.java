package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade.TradeProviderTypes;

public record ItemTradeItem(ItemStack result, ItemStack cost) implements ITrade {

    public static final MapCodec<ItemTradeItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("result").forGetter(ItemTradeItem::result),
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeItem::cost)
    ).apply(instance, ItemTradeItem::new));


    @Override
    public boolean canTrade(Player player) {
        return true;
    }


    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_TRADE_ITEM.get();
    }
}