package org.confluence.terraentity.registries.npc_trade;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.terraentity.entity.npc.ITradeHolder;

import java.util.List;

import static org.confluence.terraentity.client.gui.container.TETradeScreen.MENU_LOCATION;

public interface ITradeItemList extends ITrade {

    List<ItemStack> result();

    @Override
    default void onTrade(ServerPlayer player, ITradeHolder npc, int index) {
        for(ItemStack stack : result()){{
            player.getInventory().placeItemBackInInventory(stack.copy());
        }}
    }

    @OnlyIn(Dist.CLIENT)
    default void renderResult(ITradeHolder npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY, int index){
        // todo 轮流切换
        var it = result().get(0);

        guiGraphics.renderItem(it, x , y );

        guiGraphics.renderItemDecorations(font, it, x, y);
    }

    @OnlyIn(Dist.CLIENT)
    default void renderResultHover(ITradeHolder npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY){
        // todo 轮流切换
        guiGraphics.renderTooltip(font, result().get(0), mouseX, mouseY);
    }



    @OnlyIn(Dist.CLIENT)
    default void renderResultSlot(ITradeHolder npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY, boolean canBuy, Slot slot){
        if(canBuy){
            // todo 轮流切换
            slot.set(result().get(0).copy());
            guiGraphics.blit(MENU_LOCATION,x,y,276,0,35,17,512,256);
        }else{
            slot.set(ItemStack.EMPTY);
            guiGraphics.blit(MENU_LOCATION,x,y,276,17,35,17,512,256);
        }
    }
}
