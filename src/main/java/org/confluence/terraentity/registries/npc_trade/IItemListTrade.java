package org.confluence.terraentity.registries.npc_trade;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.utils.TEUtils;

import java.util.List;

import static org.confluence.terraentity.client.gui.container.TETradeScreen.MENU_LOCATION;

/**
 * 用多个物品交换
 */
public interface IItemListTrade extends ITrade{

    List<ItemStack> costs();

    @Override
    default boolean canTrade(Player player, AbstractTerraNPC npc) {
        // todo 不能匹配分开的物品
        for (ItemStack cost : costs()) {
            if (!player.getInventory().hasAnyMatching(i->ItemStack.isSameItem(i, cost))) {
                return false;
            }
        }
        return true;
    }

    @Override
    default void onTrade(ServerPlayer player, AbstractTerraNPC npc) {
        // todo
        for (ItemStack cost : costs()) {

            TEUtils.consumeItemCount(player.getInventory().items, cost.getItem(), cost.getCount());
        }
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    default void renderCosts(GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY){
        guiGraphics.blit(MENU_LOCATION,startx + 113,starty + 16,355,0,78,57,512,256);


        List<ItemStack> needs = costs();
        int line = 0;
        x += 7;
        startx = x;
        int lineCount = 0;
        for (ItemStack need : needs) {
            lineCount++;
            guiGraphics.renderItem(need, x, y);
            guiGraphics.renderItemDecorations(font, need, x, y);
//            guiGraphics.drawString(this.font, String.valueOf(needs.get(k)), x+4, y+16 , Color.orange.getRGB(), true);

            boolean inOdd = line % 2 == 1;
            x += 20 + (inOdd ? -1 : 0);
            int col = 3 + (inOdd ? 1 : 0);
            if (lineCount >= col) {
                y += 18;
                x = startx + (inOdd ? 0 : -8);
                line++;
                lineCount = 0;
            }
        }
    }

}
