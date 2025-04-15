package org.confluence.terraentity.registries.npc_trade;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.terraentity.utils.TEUtils;

import java.util.List;

import static org.confluence.terraentity.client.gui.container.TETradeScreen.MENU_LOCATION;

/**
 * 这个接口表示使用单个物品交易其他东西，因此重写了renderCosts
 */
public interface IItemTrade extends ITrade{

    ItemStack cost();

    @Override
    default boolean canTrade(Player player) {
        return player.getInventory().hasAnyMatching(i->ItemStack.isSameItem(i, cost()));
    }

    @Override
    default void onTrade(ServerPlayer player) {
        TEUtils.consumeItemCount(player.getInventory().items, cost().getItem(), cost().getCount());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default void renderCosts(GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY){
        guiGraphics.blit(MENU_LOCATION,startx + 113,starty + 16,434,0,78,57,512,256);

        x += 30;
        y += 18;
        guiGraphics.renderItem(cost(), x, y );
        guiGraphics.renderItemDecorations(font, cost(), x, y);

        //            guiGraphics.drawString(this.font, String.valueOf(needs.get(k)), x+4, y+16 , Color.orange.getRGB(), true);
    }
}
