package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade.*;

import java.util.Optional;

public record ItemTradeHealth(ItemStack cost, int health, TradeProperties properties) implements IItemTrade, ITradeHealth {

    public static final MapCodec<ItemTradeHealth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeHealth::cost),
            Codec.INT.fieldOf("health").forGetter(ItemTradeHealth::health),
            TradeProperties.CODEC.optionalFieldOf("properties").forGetter(i-> Optional.ofNullable(i.properties))

    ).apply(instance, (cost, health, properties)->new ItemTradeHealth(
            cost,
            health,
            properties.orElse(null)
    )));

    public static ItemTradeHealth of(ItemStack cost, int health) {
        return new ItemTradeHealth(cost, health, null);
    }

    @Override
    public boolean canTrade(Player player, ITradeHolder npc, int index) {
        return ITradeHealth.super.canTrade(player, npc, index) && IItemTrade.super.canTrade(player, npc, index);
    }

    @Override
    public void onTrade(ServerPlayer player, ITradeHolder npc, int index) {
        ITradeHealth.super.onTrade(player, npc, index);
        IItemTrade.super.onTrade(player, npc, index);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderResultHover(ITradeHolder npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY) {

    }


    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_TRADE_HEALTH.get();
    }

}
