package org.confluence.terraentity.registries.npc_trade;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.terraentity.utils.TEUtils;

import java.util.List;

/**
 * 用多个物品交换
 */
public interface IItemListTrade extends ITrade{

    List<ItemStack> costs();

    @Override
    default boolean canTrade(Player player) {
        // todo 不能匹配分开的物品
        for (ItemStack cost : costs()) {
            if (!player.getInventory().hasAnyMatching(i->ItemStack.isSameItem(i, cost))) {
                return false;
            }
        }
        return true;
    }

    @Override
    default void onTrade(ServerPlayer player) {
        // todo
        for (ItemStack cost : costs()) {

            TEUtils.consumeItemCount(player.getInventory().items, cost.getItem(), cost.getCount());
        }
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    default void renderCosts(GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY){
        List<ItemStack> needs = costs();
        int line = 0;
        startx = x;
        int lineCount = 0;
        for (ItemStack need : needs) {
            lineCount++;
            guiGraphics.renderItem(need, x, y);
            guiGraphics.renderItemDecorations(font, need, x, y);
//            guiGraphics.drawString(this.font, String.valueOf(needs.get(k)), x+4, y+16 , Color.orange.getRGB(), true);
            x += 20;
            boolean inOdd = line % 2 == 1;
            int col = 3 + (inOdd ? 1 : 0);
            if (lineCount >= col) {
                y += 18;
                x = startx + (inOdd ? 0 : -10);
                line++;
                lineCount = 0;
            }
        }
    }

}
