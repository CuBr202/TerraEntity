package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import org.confluence.terraentity.registries.npc_trade.*;
import org.confluence.terraentity.utils.TEUtils;

import static org.confluence.terraentity.client.gui.container.TETradeItemScreen.MENU_LOCATION;

public record ItemTradeHealth(ItemStack cost, int health) implements IItemTrade, ITradeHealth {

    public static final MapCodec<ItemTradeHealth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeHealth::cost),
            Codec.INT.fieldOf("health").forGetter(ItemTradeHealth::health)
    ).apply(instance, ItemTradeHealth::new));

    public static ItemTradeHealth of(ItemStack cost, int health) {
        return new ItemTradeHealth(cost, health);
    }

    @Override
    public boolean canTrade(Player player) {
        return ITradeHealth.super.canTrade(player) && IItemTrade.super.canTrade(player);
    }

    @Override
    public void onTrade(ServerPlayer player) {
        ITradeHealth.super.onTrade(player);
        IItemTrade.super.onTrade(player);
    }


    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_TRADE_HEALTH.get();
    }

}
