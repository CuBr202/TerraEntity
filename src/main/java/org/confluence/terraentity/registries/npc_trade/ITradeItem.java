package org.confluence.terraentity.registries.npc_trade;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static org.confluence.terraentity.client.gui.container.TETradeScreen.MENU_LOCATION;

/**
 * 当交易获得的物品是单个物品时继承这个接口
 */
public interface ITradeItem extends ITrade{

    ItemStack result();

    @Override
    default void onTrade(ServerPlayer player) {
        player.getInventory().add(result());
    }

    @OnlyIn(Dist.CLIENT)
    default void renderResult(GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY){
        var it = result();

        guiGraphics.renderItem(it, x , y );

        guiGraphics.renderItemDecorations(font, it, x, y);
    }

    @OnlyIn(Dist.CLIENT)
    default void renderResultHover(GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY){

        guiGraphics.renderTooltip(font, result(), mouseX, mouseY);
    }



    @OnlyIn(Dist.CLIENT)
    default void renderResultSlot(GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY, boolean canBuy, Slot slot){
        if(canBuy){
            slot.set(result().copy());
            guiGraphics.blit(MENU_LOCATION,x,y,276,0,35,17,512,256);
        }else{
            slot.set(ItemStack.EMPTY);
            guiGraphics.blit(MENU_LOCATION,x,y,276,17,35,17,512,256);
        }
    }


}
