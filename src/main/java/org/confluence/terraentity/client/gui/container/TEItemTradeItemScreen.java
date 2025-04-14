package org.confluence.terraentity.client.gui.container;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.menu.TETradesMenu;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItem;

import java.util.List;

/**
 * 单个物品交易单个物品的界面
 */
public class TEItemTradeItemScreen extends TETradeScreen<ItemTradeItem> {

    public TEItemTradeItemScreen(TETradesMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    protected void renderCosts(GuiGraphics guiGraphics, int x, int y, int startx,  ItemTradeItem trade){
        List<ItemStack> needs = List.of(trade.cost());
        for(int k = 0; k < needs.size(); k++){
            guiGraphics.renderItem(needs.get(k), x, y );
            guiGraphics.renderItemDecorations(this.font, needs.get(k), x, y);
//            guiGraphics.drawString(this.font, String.valueOf(needs.get(k)), x+4, y+16 , Color.orange.getRGB(), true);
            x+=20;
            if( k % 3 == 2){
                y += 25;
                x = startx + 130;
            }
        }
    }

}
