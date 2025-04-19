package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.registries.npc_trade.*;
import org.confluence.terraentity.registries.npc_trade_task.ITradeTask;

import javax.annotation.Nullable;

/**
 * <p>交易任务</p>
 *
 */
public record TradeTask(ITradeTask task) implements ITrade {

    public static final MapCodec<TradeTask> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ITradeTask.TYPED_CODEC.fieldOf("trade_task").forGetter(TradeTask::task)
    ).apply(instance, TradeTask::new));

    @Nullable
    ITrade getSelected(AbstractTerraNPC npc, int index){
        return task().getSelected(npc, index);
    }

    public static TradeTask create(ITradeTask task){
        return new TradeTask(task);
    }


    @Override
    public boolean canTrade(Player player, AbstractTerraNPC npc, int index) {

        ITrade selected = getSelected(npc, index);
        if(selected != null) {
            return selected.canTrade(player, npc, index);
        }
        return false;
    }

    @Override
    public void onTrade(ServerPlayer player, AbstractTerraNPC npc, int index) {
        ITrade selected = getSelected(npc, index);
        if(selected != null) {
            selected.onTrade(player, npc, index);
            task().onTrade(npc, index);
        }

    }


    @OnlyIn(Dist.CLIENT)
    public void renderResult(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY, int slotIndex){
        ITrade selected = getSelected(npc, slotIndex);
        if(selected != null) {
            selected.renderResult(npc, guiGraphics, font, x, y, startx, starty, mouseX, mouseY, slotIndex);

            String s = "o";
            guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
            guiGraphics.drawString(font, s,x, y , 0x1263bc, true);
        }else {
            String s = "√";
            guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
            guiGraphics.drawString(font, s, x, y, 0x1263bc, true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderResultHover(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY) {
        ITrade selected = getSelected(npc,npc.selectTradeIndex );
        if(selected != null) {
            selected.renderResultHover(npc, guiGraphics, font, x, y, startx, starty, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderResultSlot(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY, boolean canBuy, Slot slot) {

        ITrade selected = getSelected(npc,npc.selectTradeIndex);
        if(selected != null) {
            selected.renderResultSlot(npc,guiGraphics, font, x, y, startx, starty, mouseX, mouseY, canBuy, slot);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderCosts(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY) {
        ITrade selected = getSelected(npc,npc.selectTradeIndex);
        if(selected != null) {
            selected.renderCosts(npc, guiGraphics, font, x, y, startx, starty, mouseX, mouseY);
        }
    }

    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.TRADE_TASK.get();
    }

}