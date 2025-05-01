package org.confluence.terraentity.client.gui.container;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.misc.NPCDialogs;
import org.confluence.terraentity.entity.npc.mood.MoodInfo;
import org.confluence.terraentity.entity.npc.mood.NPCMoods;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.mixed.IPlayer;
import org.confluence.terraentity.network.c2s.ServerBoundEventPacket;
import org.jetbrains.annotations.NotNull;

public class DialogScreen extends Screen {

    Button button;
    Button summonButton; // 仅老人有效
    Screen parent;
    ITradeHolder holder;
    Component dialogText;


    protected DialogScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        holder = ((IPlayer)Minecraft.getInstance().player).terra_entity$getTradeHolder();
        String dialog = null;
        if(holder instanceof Entity e){
            dialog = NPCDialogs.getRandomDialog(BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()));
        }

        if(dialog!= null) {
            dialogText = Component.translatable(dialog);
        }

        button = Button.builder(Component.literal("Trade"), p->{
            if (minecraft != null) {
                minecraft.setScreen(parent);
            }
        }).width(50).pos(width/2 - 80, height / 2 + 25).build();


//        if(holder.getTradeManager() != null) {
            addRenderableWidget(button);
//        }
        if(holder instanceof AbstractTerraNPC npc && npc.getType() == TENpcEntities.OLD_MAN.get()){
            summonButton = Button.builder(Component.literal("Summon"), p->{
                ServerBoundEventPacket.summonSkeletron();

            }).width(50).pos(width/2 - 160, height / 2 + 25).build();
            addRenderableWidget(summonButton);
        }
    }

    @Override
    public void onClose() {
        super.onClose();

    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if(dialogText != null){
            guiGraphics.drawString(font, dialogText, 20, height / 2 -10, 0xFFFFFF);
        }

        // todo draw
        if(holder.getMood() == null){
            return;
        }
        var list = holder.getMood().getMoodInfoList();
        for(int i = 0; i < list.size(); i++){
            ResourceLocation location = list.get(i);

            MoodInfo moodInfo = NPCMoods.getMoodInfo(location);

            if(moodInfo == null) continue;
            guiGraphics.drawString(font, Component.translatable(moodInfo.info()), 20, height / 2 - 100 + i * 10, 0xFFFFFF);
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 69) { // E
            if (minecraft != null) {
                minecraft.setScreen(parent);
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
