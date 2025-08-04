package org.confluence.terraentity.client.gui.container;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.misc.NPCDialogs;
import org.confluence.terraentity.entity.npc.mood.MoodInfo;
import org.confluence.terraentity.entity.npc.mood.NPCMood;
import org.confluence.terraentity.api.npc.trade.ITradeHolder;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.mixed.IPlayer;
import org.confluence.terraentity.network.c2s.ServerBoundEventPacket;

import java.util.List;

public class DialogScreen extends Screen {

    Button button;
    Button summonButton; // 仅老人有效
    Screen parent;
    private final boolean trade;
    ITradeHolder holder;
    Component dialogText;


    protected DialogScreen(Component title, Screen parent, boolean trade) {
        super(title);
        this.parent = parent;
        this.trade = trade;
    }

    @Override
    protected void init() {
        super.init();

        holder = ((IPlayer) Minecraft.getInstance().player).terra_entity$getTradeHolder();
        if (holder instanceof AbstractTerraNPC npc) {
            String s = NPCDialogs.Loader.getInstance().getRandomDialog(npc.getRandom(), npc.getType());
            if (s != null) {
                dialogText = Component.translatable(s);
            } else {
                dialogText = Component.empty();
            }
        }


        if (trade) {
            button = Button.builder(Component.literal("Trade"), p -> {
                if (minecraft != null) {
                    minecraft.setScreen(parent);
                }
            }).pos(width / 2 - 80, height / 2 + 25).build();


//        if(holder.getTradeManager() != null) {
            addRenderableWidget(button);
//        }
        }
        if(holder instanceof AbstractTerraNPC npc && npc.level().getDayTime()%24000>12000 && npc.getType() == TENpcEntities.OLD_MAN.get()){
            summonButton = Button.builder(Component.literal("Summon"), p->{
                ServerBoundEventPacket.summonSkeletron();
                minecraft.setScreen(null); // 关闭对话框
            }).width(50).pos(width/2 - 160, height / 2 + 25).build();
            addRenderableWidget(summonButton);
        }
    }

    @Override
    protected void rebuildWidgets() {
        if (button != null) {
            button.setPosition(width / 2 - 80, height / 2 + 25);
        }
        if (summonButton != null) {
            summonButton.setPosition(width/2 - 160, height / 2 + 25);
        }
    }

    @Override
    public void onClose() {
        super.onClose();

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if(dialogText != null){
            if (font.width(dialogText) > 128) {
                List<FormattedCharSequence> list = font.split(dialogText, 128);
                int l = height / 2 - list.size() * 9 / 2;
                for (FormattedCharSequence formattedcharsequence : list) {
                    guiGraphics.drawString(font, formattedcharsequence, 20, l, 0xFFFFFF);
                    l += 9;
                }
            } else {
                guiGraphics.drawString(font, dialogText, 20, height / 2 - 10, 0xFFFFFF);
            }
        }

        // todo draw
        if(holder.getMood() == null){
            return;
        }
        var list = holder.getMood().getMoodInfoList();
        for(int i = 0; i < list.size(); i++){
            ResourceLocation location = list.get(i);

            MoodInfo moodInfo = NPCMood.Loader.getInstance().getMoodInfo(location);

            if(moodInfo == null) continue;
            guiGraphics.drawString(font, Component.translatable(moodInfo.info()), 20, height / 2 - 100 + i * 10, 0xFFFFFF);
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (minecraft != null && minecraft.options.keyInventory.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode))) {
            minecraft.setScreen(trade ? parent : null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
