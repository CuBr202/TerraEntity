package org.confluence.terraentity.registries.npc_trade;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.TerraEntity;
import org.jetbrains.annotations.Nullable;

import static org.confluence.terraentity.client.gui.container.TETradeScreen.MENU_LOCATION;

/**
 * 当交易的内容是恢复生命值时继承这个接口
 */
public interface ITradeHealth extends ITrade {

    int health();

    default int getHealth(@Nullable Player player){
        return health();
    }

    @Override
    default boolean canTrade(Player player) {
        return player.getHealth() < player.getMaxHealth();
    }

    @Override
    default void onTrade(ServerPlayer player) {
        player.heal(getHealth(player));
    }

    @Override
    default void renderResult(GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY) {
        ResourceLocation iconSprite = TerraEntity.defaultPath("hud/heart/full");
        guiGraphics.blitSprite(iconSprite, x, y, 16, 16);
        String s = "↑" + getHealth(Minecraft.getInstance().player);
        guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
        guiGraphics.drawString(font, s, x + 19 - 2 - font.width(s), y + 6 + 3, 0x12bc63, true);
    }

    @Override
    default void renderResultSlot(GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY, boolean canBuy, Slot slot) {
        slot.set(ItemStack.EMPTY);
        if(canBuy){
            renderResult(guiGraphics, font, x+35, y+2, startx, starty, mouseX, mouseY);
            guiGraphics.blit(MENU_LOCATION,x,y,276,0,35,17,512,256);
        }else{
            guiGraphics.blit(MENU_LOCATION,x,y,276,17,35,17,512,256);
        }
    }
}
