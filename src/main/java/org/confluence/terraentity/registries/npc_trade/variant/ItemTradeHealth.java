package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade.TradeProviderTypes;

public record ItemTradeHealth(ItemStack cost, int health) implements ITrade {

    public static final MapCodec<ItemTradeHealth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeHealth::cost),
            Codec.INT.fieldOf("health").forGetter(ItemTradeHealth::health)
    ).apply(instance, ItemTradeHealth::new));

    @Override
    public boolean canTrade(Player player) {
        return player.getHealth() < player.getMaxHealth() && player.getInventory().contains(cost);
    }

    @Override
    public void onTrade(ServerPlayer player) {
        player.heal(health);
        player.getInventory().removeItem(cost);
    }

    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_TRADE_HEALTH.get();
    }
}
