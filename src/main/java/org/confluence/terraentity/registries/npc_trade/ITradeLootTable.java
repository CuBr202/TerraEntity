package org.confluence.terraentity.registries.npc_trade;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;

import java.util.List;
import java.util.Optional;

import static org.confluence.terraentity.client.gui.container.TETradeScreen.MENU_LOCATION;

public interface ITradeLootTable extends ITrade{

    ResourceKey<LootTable> lootTable();

    Optional<ResourceLocation> sprite();

    @Override
    default void onTrade(ServerPlayer player, ITradeHolder npc, int index) {
        List<ItemStack> loot = player.level().getServer().reloadableRegistries()
                .getLootTable(lootTable())
                .getRandomItems(new LootParams.Builder((ServerLevel) player.level())
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.ORIGIN, player.position())
                        .create(LootContextParamSets.GIFT));
        for(ItemStack stack : loot){
            player.getInventory().placeItemBackInInventory(stack);
        }
    }

    @OnlyIn(Dist.CLIENT)
    default void renderResult(ITradeHolder npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY, int index){

        ResourceLocation sprite = sprite().orElse(TerraEntity.space("unknown"));
        guiGraphics.blitSprite(sprite,x,y,16,16);

    }

    @OnlyIn(Dist.CLIENT)
    default void renderResultHover(ITradeHolder npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY){

//        guiGraphics.renderTooltip(font, result(), mouseX, mouseY);
    }



    @OnlyIn(Dist.CLIENT)
    default void renderResultSlot(ITradeHolder npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY, boolean canBuy, Slot slot){
        if(canBuy){
            ResourceLocation sprite = sprite().orElse(TerraEntity.space("unknown"));
            guiGraphics.blitSprite(sprite,x+35,y+2,16,16);
            guiGraphics.blit(MENU_LOCATION,x,y,276,0,35,17,512,256);
        }else{

            guiGraphics.blit(MENU_LOCATION,x,y,276,17,35,17,512,256);
        }
        slot.set(ItemStack.EMPTY);
    }

}
