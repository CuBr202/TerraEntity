package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.registries.npc_trade.*;

public record ItemTradeHealth(ItemStack cost, int health) implements IItemTrade, ITradeHealth {

    public static final MapCodec<ItemTradeHealth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeHealth::cost),
            Codec.INT.fieldOf("health").forGetter(ItemTradeHealth::health)
    ).apply(instance, ItemTradeHealth::new));

    public static ItemTradeHealth of(ItemStack cost, int health) {
        return new ItemTradeHealth(cost, health);
    }

    @Override
    public boolean canTrade(Player player, AbstractTerraNPC npc) {
        return ITradeHealth.super.canTrade(player, npc) && IItemTrade.super.canTrade(player, npc);
    }

    @Override
    public void onTrade(ServerPlayer player, AbstractTerraNPC npc) {
        ITradeHealth.super.onTrade(player, npc);
        IItemTrade.super.onTrade(player, npc);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderResultHover(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY) {

    }


    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_TRADE_HEALTH.get();
    }

}
