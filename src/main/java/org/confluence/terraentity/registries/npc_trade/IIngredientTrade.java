package org.confluence.terraentity.registries.npc_trade;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.lib.common.recipe.AmountIngredient;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import java.util.ArrayList;
import java.util.List;

import static org.confluence.terraentity.client.gui.container.TETradeScreen.MENU_LOCATION;

public interface IIngredientTrade extends ITrade{

    List<AmountIngredient> costs();

    @Override
    default boolean canTrade(Player player, ITradeHolder npc, int index) {
        return matches(player.getInventory().items);
    }

    @Override
    default void onTrade(ServerPlayer player, ITradeHolder npc, int index) {

        List<ItemStack> container = player.getInventory().items;
        for (AmountIngredient ingredient : costs()) {
            int count = ingredient.amount();

            for(ItemStack stack : container){
                if(ingredient.ingredient().test(stack)){
                    int hasCount = stack.getCount();
                    if(hasCount >= count){

                        stack.shrink(count);
                        break;
                    }
                    count -= hasCount;
                    stack.setCount(0);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default void renderCosts(ITradeHolder npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY){
        List<ItemStack> needs = new ArrayList<>();
        for(int k = 0; k < costs().size(); k++){
            Ingredient ingredient = costs().get(k).ingredient();
            var itlist = ingredient.getItems();
            int size = itlist.length;
            int i = (int) (npc.level().dayTime() / 40 % size);
            ItemStack target = itlist[i].copy();
            target.setCount(costs().get(k).amount());
            needs.add(target);
        }

        int line = 0;


        int lineCount = 0;
        if(needs.size() == 1){
            // 只有一个原料时
            guiGraphics.blit(MENU_LOCATION,startx + 113,starty + 16,434,0,78,57,512,256);
            ItemStack cost = needs.get(0);
            x += 30;
            y += 18;
            guiGraphics.renderItem(cost, x, y );
            guiGraphics.renderItemDecorations(font, cost, x, y);

        }else {
            // 有多个原料时
            x += 7;
            guiGraphics.blit(MENU_LOCATION,startx + 113,starty + 16,355,0,78,57,512,256);

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

    default boolean matches(List<ItemStack> container) {
        // container
        List<ItemStack> temp = new ArrayList<>();
        for(ItemStack stack : container){
            if(stack.isEmpty()){
                continue;
            }
            temp.add(stack.copy());
        }
        container = temp;

        // ingredients
        boolean matches = true;
        for (AmountIngredient ingredient : costs()) {
            int count = ingredient.amount();
            boolean found = false;
            for(ItemStack stack : container){
                if(ingredient.ingredient().test(stack)){
                    int hasCount = stack.getCount();
                    if(hasCount >= count){
                        found = true;
                        stack.shrink(count);
                        break;
                    }
                    count -= hasCount;
                    stack.setCount(0);
                }
            }
            if(!found){
                matches = false;
                break;
            }
        }

        return matches;
    }
}
