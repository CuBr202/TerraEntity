package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade.TradeProviderTypes;

import java.util.List;

public record ItemTradeItem(ItemStack result, ItemStack cost) implements ITrade {

    public static final MapCodec<ItemTradeItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("result").forGetter(ItemTradeItem::result),
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeItem::cost)
    ).apply(instance, ItemTradeItem::new));


    @Override
    public boolean canTrade(Player player) {
        return player.getInventory().hasAnyMatching(i->ItemStack.isSameItem(i, cost));
    }

    @Override
    public void onTrade(ServerPlayer player) {
        // todo
        consumeItemCount(player.getInventory().items, cost.getItem(), cost.getCount());
        player.getInventory().add(result);
    }

    public static void consumeItemCount(List<ItemStack> have, Item item, int consumeCount) {
        int count = 0;
        for (ItemStack stack : have) {
            if (stack.is(item) && count < consumeCount) {
                int toConsume = Math.min(stack.getCount(), consumeCount - count);
                stack.shrink(toConsume);
                count += toConsume;
            }
        }
    }

    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_TRADE_ITEM.get();
    }
}